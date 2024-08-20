package pronin.oleg.zulip.presentation.dialogs.create_channel

import android.os.Bundle
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isInvisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import pronin.oleg.zulip.R
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.databinding.DialogChannelCreateBinding
import pronin.oleg.zulip.domain.enums.ChannelPermission
import pronin.oleg.zulip.presentation.base.ElmBaseBottomSheetDialogFragment
import pronin.oleg.zulip.presentation.dialogs.create_channel.store.CreateChannelEffect
import pronin.oleg.zulip.presentation.dialogs.create_channel.store.CreateChannelEvent
import pronin.oleg.zulip.presentation.dialogs.create_channel.store.CreateChannelState
import pronin.oleg.zulip.presentation.dialogs.create_channel.store.CreateChannelStoreFactory
import pronin.oleg.zulip.presentation.states.CreateChannelScreenState
import pronin.oleg.zulip.utils.setSafeOnClickListener
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import javax.inject.Inject


class CreateChannelDialogFragment : ElmBaseBottomSheetDialogFragment<
        DialogChannelCreateBinding,
        CreateChannelEffect,
        CreateChannelState,
        CreateChannelEvent
        >(DialogChannelCreateBinding::inflate) {

    @Inject
    lateinit var createChannelStoreFactory: CreateChannelStoreFactory

    override val store by elmStoreWithRenderer(elmRenderer = this) {
        createChannelStoreFactory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().createChatComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun initBinding() {
        initRadioGroup()
        initListeners()
    }

    private fun initRadioGroup() {
        binding.apply {
            permissionGroup.apply {
                clearCheck()

                ChannelPermission.entries.forEachIndexed { index, channelPermission ->
                    addView(
                        RadioButton(
                            this.context,
                            null,
                            0,
                            R.style.RadioButtonItem
                        ).apply {
                            id = index
                            text = getString(channelPermission.title)
                        }
                    )
                }

                check(
                    ChannelPermission.entries.indexOf(ChannelPermission.PUBLIC)
                )
            }

            descPermission.text = getString(R.string.public_permission_desc)
        }
    }

    private fun initListeners() {
        binding.apply {
            channelName.doAfterTextChanged {
                channelNameInput.error = null

                store.accept(
                    CreateChannelEvent.UI.ChangeChannelName(it?.toString() ?: "")
                )
            }

            channelDesc.doAfterTextChanged {
                store.accept(
                    CreateChannelEvent.UI.ChangeChannelDescription(it?.toString() ?: "")
                )
            }

            permissionGroup.setOnCheckedChangeListener { _, checkedId ->
                hideKeyboard()

                val permission = ChannelPermission.entries[checkedId]

                descPermission.text = getString(permission.description)

                store.accept(CreateChannelEvent.UI.ChangeChannelPermissions(permission))
            }

            createChannelBtn.setSafeOnClickListener {
                hideKeyboard()

                store.accept(CreateChannelEvent.UI.CreateChannel)
            }
        }
    }

    override fun render(state: CreateChannelState) {
        renderCreateButton(state.isCreateButtonLocked)
        renderState(state.screenState)
    }

    private fun renderCreateButton(isCreateButtonLocked: Boolean) {
        binding.createChannelBtn.apply {
            isEnabled = !isCreateButtonLocked
            isClickable = !isCreateButtonLocked
        }
    }

    private fun renderState(screenState: CreateChannelScreenState) {
        when (screenState) {
            CreateChannelScreenState.Init -> binding.apply {
                channelNameInput.error = null
                channelName.isEnabled = true
                channelDesc.isEnabled = true
                permissionGroup.children.forEach { it.isEnabled = true }
                progress.isInvisible = true
            }

            CreateChannelScreenState.ProcessOfCreation -> binding.apply {
                channelNameInput.error = null
                channelName.isEnabled = false
                channelDesc.isEnabled = false
                permissionGroup.children.forEach { it.isEnabled = false }
                progress.isInvisible = false
            }

            is CreateChannelScreenState.Error -> binding.apply {
                channelNameInput.error = null
                channelName.isEnabled = true
                channelDesc.isEnabled = true
                permissionGroup.children.forEach { it.isEnabled = true }
                progress.isInvisible = true
            }

            CreateChannelScreenState.ErrorInChannelName -> binding.apply {
                channelNameInput.error = getString(R.string.error_channel_name)
                channelName.isEnabled = true
                channelDesc.isEnabled = true
                permissionGroup.children.forEach { it.isEnabled = true }
                progress.isInvisible = true
            }
        }
    }

    override fun handleEffect(effect: CreateChannelEffect) = when (effect) {
        is CreateChannelEffect.ShowSuccess -> showSuccess(effect.message)

        is CreateChannelEffect.ShowError -> showError(effect.throwable.message)

        is CreateChannelEffect.DismissDialog -> {
            setFragmentResult(
                CreateChannelDialogResult.REQUEST_KEY,
                bundleOf(
                    CreateChannelDialogResult.RESULT_KEY to CreateChannelDialogResult(
                        isSuccessCreateChannel = true
                    )
                )
            )

            dismiss()
        }
    }

    companion object {
        fun getInstance() = CreateChannelDialogFragment()
    }
}
