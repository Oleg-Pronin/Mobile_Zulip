package pronin.oleg.zulip.presentation.screens.people

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import pronin.oleg.zulip.R
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.databinding.FragmentPeopleBinding
import pronin.oleg.zulip.presentation.adapters.SimpleListAdapter
import pronin.oleg.zulip.presentation.base.ElmBaseFragment
import pronin.oleg.zulip.presentation.diff_utils.DelegateAdapterItemCallback
import pronin.oleg.zulip.presentation.item_decorators.ListRecyclerMarginsDecoration
import pronin.oleg.zulip.presentation.navigation.Screens
import pronin.oleg.zulip.presentation.screens.people.adapter_delegates.PeopleDelegate
import pronin.oleg.zulip.presentation.screens.people.store.PeopleEffect
import pronin.oleg.zulip.presentation.screens.people.store.PeopleEvent
import pronin.oleg.zulip.presentation.screens.people.store.PeopleState
import pronin.oleg.zulip.presentation.screens.people.store.PeopleStoreFactory
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.UserUI
import pronin.oleg.zulip.utils.lazyUnsafe
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import javax.inject.Inject

class PeopleFragment : ElmBaseFragment<
        FragmentPeopleBinding,
        PeopleEffect,
        PeopleState,
        PeopleEvent
        >(FragmentPeopleBinding::inflate) {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var peopleStoreFactory: PeopleStoreFactory

    override val store by elmStoreWithRenderer(elmRenderer = this) {
       peopleStoreFactory.create()
    }

    private val peopleAdapter: SimpleListAdapter<UserUI> by lazyUnsafe {
        SimpleListAdapter(
            diffCallback = DelegateAdapterItemCallback(),
            PeopleDelegate {
                store.accept(PeopleEvent.UI.GoToProfile(it))
            }
        )
    }

    private val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().peopleComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarColor(R.color.eerie_black)

        binding.apply {
            peopleRV.apply {
                adapter = peopleAdapter
                layoutManager = LinearLayoutManager(requireContext())

                addItemDecoration(
                    ListRecyclerMarginsDecoration.verticalListMargins(
                        firstTop = resources.getDimensionPixelOffset(R.dimen.first_decoration_margin),
                        lastBottom = resources.getDimensionPixelOffset(R.dimen.last_decoration_margin),
                        vertical = resources.getDimensionPixelOffset(R.dimen.vertical_decoration_margin),
                        horizontal = resources.getDimensionPixelOffset(R.dimen.horizontal_decoration_margin)
                    )
                )
            }

            incSearchToolbar.incSearch.searchText.addTextChangedListener {
                it?.let { searchQuery.tryEmit(it.toString()) }
            }
        }

        listenToSearchQuery()

        store.accept(PeopleEvent.UI.Init)
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenToSearchQuery() {
        searchQuery
            .distinctUntilChanged()
            .debounce(500)
            .mapLatest {
                store.accept(PeopleEvent.UI.Search(it))
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun render(state: PeopleState) {
        when (val data = state.screenState) {
            ScreenState.Init -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    peopleRV.isVisible = false
                }
            }

            ScreenState.Loading -> {
                binding.apply {
                    incShimmer.root.isVisible = true
                    peopleRV.isVisible = false
                }
            }

            is ScreenState.Content -> {
                peopleAdapter.setItems(data.content)

                binding.apply {
                    incShimmer.root.isVisible = false
                    peopleRV.isVisible = true
                }
            }

            is ScreenState.Error -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    peopleRV.isVisible = false

                    showError(data.throwable.message) {
                        store.accept(PeopleEvent.UI.Init)
                    }
                }
            }
        }
    }

    override fun handleEffect(effect: PeopleEffect) = when (effect) {
        is PeopleEffect.GoToProfile ->
            router.navigateTo(Screens.Profile(effect.userId))

        is PeopleEffect.ShowError ->
            showError(effect.throwable.message) { store.accept(PeopleEvent.UI.Init) }
    }

    override fun onDestroyView() {
        binding.peopleRV.adapter = null
        super.onDestroyView()
    }

    companion object {
        fun getInstance() = PeopleFragment()
    }
}