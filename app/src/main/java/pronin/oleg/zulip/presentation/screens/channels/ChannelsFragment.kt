package pronin.oleg.zulip.presentation.screens.channels

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import com.google.android.material.tabs.TabLayout
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
import pronin.oleg.zulip.databinding.FragmentChannelsBinding
import pronin.oleg.zulip.domain.enums.ChannelTabs
import pronin.oleg.zulip.presentation.adapters.SimpleListAdapter
import pronin.oleg.zulip.presentation.base.ElmBaseFragment
import pronin.oleg.zulip.presentation.dialogs.create_channel.CreateChannelDialogFragment
import pronin.oleg.zulip.presentation.dialogs.create_channel.CreateChannelDialogResult
import pronin.oleg.zulip.presentation.diff_utils.DelegateAdapterItemCallback
import pronin.oleg.zulip.presentation.navigation.Screens
import pronin.oleg.zulip.presentation.screens.channels.adapter_delegates.StreamDelegate
import pronin.oleg.zulip.presentation.screens.channels.adapter_delegates.TopicDelegate
import pronin.oleg.zulip.presentation.screens.channels.store.ChannelsEffect
import pronin.oleg.zulip.presentation.screens.channels.store.ChannelsEvent
import pronin.oleg.zulip.presentation.screens.channels.store.ChannelsState
import pronin.oleg.zulip.presentation.screens.channels.store.ChannelsStoreFactory
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChannelUI
import pronin.oleg.zulip.utils.lazyUnsafe
import pronin.oleg.zulip.utils.serializable
import pronin.oleg.zulip.utils.setSafeOnClickListener
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import javax.inject.Inject

class ChannelsFragment : ElmBaseFragment<
        FragmentChannelsBinding,
        ChannelsEffect,
        ChannelsState,
        ChannelsEvent
        >(FragmentChannelsBinding::inflate) {
    @Inject
    lateinit var router: Router

    @Inject
    lateinit var channelsStoreFactory: ChannelsStoreFactory

    override val store by elmStoreWithRenderer(elmRenderer = this) {
        channelsStoreFactory.create()
    }

    private val channelAdapter: SimpleListAdapter<ChannelUI> by lazyUnsafe {
        SimpleListAdapter(
            diffCallback = DelegateAdapterItemCallback(),
            TopicDelegate { streamId, topicName, lastMessageId ->
                store.accept(ChannelsEvent.UI.GoToChat(streamId, topicName, lastMessageId))
            },
            StreamDelegate(
                { id, color -> store.accept(ChannelsEvent.UI.LoadTopics(id, color)) },
                { id -> store.accept(ChannelsEvent.UI.HideTopics(id)) }
            )
        )
    }

    private val searchQuery = MutableSharedFlow<String>(extraBufferCapacity = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().channelComponent.inject(this)

        setFragmentResultListener(CreateChannelDialogResult.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != CreateChannelDialogResult.REQUEST_KEY)
                return@setFragmentResultListener

            val dialogResult = bundle.serializable<CreateChannelDialogResult>(
                CreateChannelDialogResult.RESULT_KEY
            )

            if (dialogResult != null)
                store.accept(ChannelsEvent.UI.ReloadStream)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBarColor(R.color.eerie_black)

        initToolbar()
        initTabs()
        initRV()
        initFab()

        listenToSearchQuery()
    }

    private fun initToolbar() {
        binding.apply {
            incSearchToolbar.apply {
                incSearch.searchText.addTextChangedListener {
                    it?.let { searchQuery.tryEmit(it.toString()) }
                }

                tabLayout.addOnTabSelectedListener(
                    object : TabLayout.OnTabSelectedListener {
                        override fun onTabSelected(p0: TabLayout.Tab) {
                            val isSubscribedTab =
                                ChannelTabs.entries[p0.position] == ChannelTabs.SUBSCRIBED

                            incSearch.searchText.setText("")

                            store.accept(ChannelsEvent.UI.ChangeTab(isSubscribedTab))

                            addChannelBtn.isVisible = !isSubscribedTab
                        }

                        override fun onTabUnselected(p0: TabLayout.Tab?) = Unit

                        override fun onTabReselected(p0: TabLayout.Tab?) = Unit
                    }
                )
            }
        }
    }

    private fun initTabs() {
        binding.incSearchToolbar.tabLayout.apply {
            ChannelTabs.entries.forEach {
                addTab(newTab().setText(it.getTitleRes()))
            }
        }
    }

    private fun initRV() {
        binding.channelRV.apply {
            adapter = channelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initFab() {
        binding.apply {
            channelRV.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY + 12 && addChannelBtn.isExtended) {
                    addChannelBtn.shrink()
                }

                if (scrollY < oldScrollY - 12 && !addChannelBtn.isExtended) {
                    addChannelBtn.extend()
                }
            }

            addChannelBtn.setSafeOnClickListener {
                CreateChannelDialogFragment.getInstance()
                    .show(parentFragmentManager, "Create_Emoji")
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun listenToSearchQuery() {
        searchQuery
            .distinctUntilChanged()
            .debounce(500)
            .mapLatest {
                store.accept(ChannelsEvent.UI.Search(it))
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun render(state: ChannelsState) {
        when (val data = state.screenState) {
            ScreenState.Init -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    channelRV.isVisible = false
                }
            }

            ScreenState.Loading -> {
                binding.apply {
                    incShimmer.root.isVisible = true
                    channelRV.isVisible = false
                }
            }

            is ScreenState.Content -> {
                channelAdapter.setItems(data.content)

                binding.apply {
                    incShimmer.root.isVisible = false
                    channelRV.isVisible = true
                }
            }

            is ScreenState.Error -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    channelRV.isVisible = true

                    showError(data.throwable.message) {
                        store.accept(ChannelsEvent.UI.ReloadStream)
                    }
                }
            }
        }
    }

    override fun handleEffect(effect: ChannelsEffect) = when (effect) {
        is ChannelsEffect.ShowError ->
            showError(effect.throwable.message) { store.accept(ChannelsEvent.UI.Init) }

        is ChannelsEffect.GoToChat ->
            router.navigateTo(
                Screens.Chat(effect.stream, effect.topicName, effect.lastMessageId)
            )
    }

    override fun onDestroyView() {
        binding.channelRV.adapter = null
        super.onDestroyView()
    }

    companion object {
        fun getInstance() = ChannelsFragment()
    }
}