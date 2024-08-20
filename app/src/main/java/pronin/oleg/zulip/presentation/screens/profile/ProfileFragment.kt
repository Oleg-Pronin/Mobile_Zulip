package pronin.oleg.zulip.presentation.screens.profile

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.github.terrakok.cicerone.Router
import pronin.oleg.zulip.R
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.databinding.FragmentProfileBinding
import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.domain.models.users.User
import pronin.oleg.zulip.presentation.base.ElmBaseFragment
import pronin.oleg.zulip.presentation.screens.profile.store.ProfileEffect
import pronin.oleg.zulip.presentation.screens.profile.store.ProfileEvent
import pronin.oleg.zulip.presentation.screens.profile.store.ProfileStoreFactory
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.states.State
import pronin.oleg.zulip.presentation.ui.UserUI
import pronin.oleg.zulip.utils.argumentDelegate
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import javax.inject.Inject

class ProfileFragment : ElmBaseFragment<
        FragmentProfileBinding,
        ProfileEffect,
        State<UserUI>,
        ProfileEvent
        >(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var profileStoreFactory: ProfileStoreFactory

    private val userId by argumentDelegate<Int?>()

    override val store by elmStoreWithRenderer(elmRenderer = this) {
        profileStoreFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().profileComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isCurrentUser = userId == null

        binding.apply {
            toolbarProfile.apply {
                isGone = isCurrentUser

                setNavigationOnClickListener {
                    router.backTo(null)
                }
            }
        }

        store.accept(
            ProfileEvent.UI.Init(
                if (isCurrentUser)
                    User.Own
                else
                    User.Other(userId!!)
            )
        )
    }

    override fun render(state: State<UserUI>) {
        when (val item = state.screenState) {
            ScreenState.Init -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    profileLayout.isVisible = false
                }
            }

            ScreenState.Loading -> {
                binding.apply {
                    incShimmer.root.isVisible = true
                    profileLayout.isVisible = false
                }
            }

            is ScreenState.Content -> {
                binding.apply {
                    Glide.with(requireContext())
                        .asBitmap()
                        .load(item.content.avatar)
                        .error(R.drawable.ic_avatar)
                        .into(avatarUser)

                    fullNameUser.text = item.content.fullName

                    profileStatus.apply {
                        text = getString(
                            when (item.content.status) {
                                StatusPresence.ACTIVE -> R.string.online
                                StatusPresence.IDLE -> R.string.idle
                                StatusPresence.OFFLINE -> R.string.offline
                            }
                        )

                        setTextColor(
                            context.getColor(
                                when (item.content.status) {
                                    StatusPresence.ACTIVE -> R.color.apple
                                    StatusPresence.IDLE -> R.color.princeton_orange
                                    StatusPresence.OFFLINE -> R.color.naughty_red
                                }
                            )
                        )
                    }

                    incShimmer.root.isVisible = false
                    profileLayout.isVisible = true
                }
            }

            is ScreenState.Error -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    profileLayout.isVisible = false

                    showError(item.throwable.message) {
                        store.accept(
                            ProfileEvent.UI.Init(
                                if (userId == null)
                                    User.Own
                                else
                                    User.Other(userId!!)
                            )
                        )
                    }
                }
            }
        }
    }

    override fun handleEffect(effect: ProfileEffect) = when (effect) {
        is ProfileEffect.ShowError ->
            showError(effect.throwable.message) {
                store.accept(
                    ProfileEvent.UI.Init(
                        if (userId == null)
                            User.Own
                        else
                            User.Other(userId!!)
                    )
                )
            }
    }

    companion object {
        private const val USER_ID = "userId"

        fun getInstance(userId: Int? = null) = ProfileFragment().apply {
            if (userId != null)
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
        }
    }
}