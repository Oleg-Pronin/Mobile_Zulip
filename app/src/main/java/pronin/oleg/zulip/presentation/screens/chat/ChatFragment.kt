package pronin.oleg.zulip.presentation.screens.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toColorInt
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.terrakok.cicerone.Router
import pronin.oleg.zulip.R
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.databinding.FragmentChatBinding
import pronin.oleg.zulip.app.di.models.emojis.EmojiCodes
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.presentation.adapters.SimpleListAdapter
import pronin.oleg.zulip.presentation.base.ElmBaseFragment
import pronin.oleg.zulip.presentation.dialogs.emoji.EmojiDialogFragment
import pronin.oleg.zulip.presentation.dialogs.emoji.EmojiDialogResult
import pronin.oleg.zulip.presentation.diff_utils.DelegateAdapterItemCallback
import pronin.oleg.zulip.presentation.item_decorators.ListRecyclerMarginsDecoration
import pronin.oleg.zulip.presentation.screens.chat.adapter_delegates.DateAdapterDelegate
import pronin.oleg.zulip.presentation.screens.chat.adapter_delegates.MessageAdapterDelegate
import pronin.oleg.zulip.presentation.screens.chat.adapter_delegates.OwnMessageAdapterDelegate
import pronin.oleg.zulip.presentation.listeners.ScrollListener
import pronin.oleg.zulip.presentation.screens.chat.store.ChatEffect
import pronin.oleg.zulip.presentation.screens.chat.store.ChatEvent
import pronin.oleg.zulip.presentation.screens.chat.store.ChatState
import pronin.oleg.zulip.presentation.screens.chat.store.ChatStoreFactory
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.utils.argumentDelegate
import pronin.oleg.zulip.utils.lazyUnsafe
import pronin.oleg.zulip.utils.serializable
import pronin.oleg.zulip.utils.setSafeOnClickListener
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import javax.inject.Inject

class ChatFragment : ElmBaseFragment<
        FragmentChatBinding,
        ChatEffect,
        ChatState,
        ChatEvent
        >(FragmentChatBinding::inflate) {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var chatStoreFactory: ChatStoreFactory

    @Inject
    lateinit var emojiCodes: EmojiCodes

    private val stream by argumentDelegate<StreamDomain>()
    private val topicName by argumentDelegate<String>()
    private val lastMessageId by argumentDelegate<Int>()

    override val store by elmStoreWithRenderer(elmRenderer = this) {
        chatStoreFactory.create(stream.id, topicName, lastMessageId)
    }

    private val onClickAddEmoji: (messageId: Int) -> Unit = {
        EmojiDialogFragment
            .getInstance(messageId = it)
            .show(parentFragmentManager, "Emoji")
    }

    private val onClickEmoji: (
        messageId: Int,
        isSelected: Boolean,
        emojiCode: String,
        emojiName: String?,
    ) -> Unit = { messageId, isSelected, emojiCode, emojiName ->
        store.accept(
            ChatEvent.UI.ClickEmoji(
                messageId,
                isSelected,
                emojiCode,
                emojiName ?: ""
            )
        )
    }

    private val chatAdapter: SimpleListAdapter<ChatUI> by lazyUnsafe {
        SimpleListAdapter(
            DelegateAdapterItemCallback(),
            DateAdapterDelegate(),
            MessageAdapterDelegate(onClickEmoji, onClickAddEmoji),
            OwnMessageAdapterDelegate(onClickEmoji)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().chatComponent.inject(this)

        setFragmentResultListener(EmojiDialogResult.REQUEST_KEY) { requestKey, bundle ->
            if (requestKey != EmojiDialogResult.REQUEST_KEY)
                return@setFragmentResultListener

            val dialogResult = bundle.serializable<EmojiDialogResult>(
                EmojiDialogResult.RESULT_KEY
            )

            if (dialogResult != null)
                store.accept(
                    ChatEvent.UI.AddEmoji(
                        messageId = dialogResult.messageId,
                        emojiCode = dialogResult.emojiCode,
                        emojiName = dialogResult.emojiName
                    )
                )
        }

        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackground()
        initRV()
        initToolbar()
        initListeners()

        binding.topicName.text = getString(R.string.topic_text, this@ChatFragment.topicName)

        store.accept(ChatEvent.UI.Init)
    }

    private fun initBackground() {
        stream.color?.let {
            val colorInt = it.toColorInt()

            setBarColorInt(colorInt)
            binding.toolbarChat.setBackgroundColor(colorInt)
        }
    }

    private fun initRV() {
        binding.messageRV.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = true
            }

            addItemDecoration(
                ListRecyclerMarginsDecoration.verticalListMargins(
                    firstTop = resources.getDimensionPixelOffset(R.dimen.first_decoration_margin),
                    lastBottom = resources.getDimensionPixelOffset(R.dimen.last_decoration_margin),
                    vertical = resources.getDimensionPixelOffset(R.dimen.vertical_decoration_margin),
                    horizontal = resources.getDimensionPixelOffset(R.dimen.horizontal_decoration_margin)
                )
            )

            addOnScrollListener(
                ScrollListener(layoutManager as LinearLayoutManager) {
                    store.accept(ChatEvent.UI.ScrollItems(currentPosition = it))
                }
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initToolbar() {
        binding.toolbarChat.apply {
            logo = resources.getDrawable(
                if (stream.isPrivate)
                    R.drawable.ic_lock
                else
                    R.drawable.ic_hashtag,
                context.theme
            )

            title = stream.name

            setNavigationOnClickListener {
                router.exit()
            }
        }
    }

    private fun initListeners() {
        binding.editorMessages.doAfterTextChanged {
            store.accept(ChatEvent.UI.ChangeMessage(it?.toString() ?: ""))
        }
    }

    override fun render(state: ChatState) {
        renderMessage(state.message)
        renderScreenState(state.screenState)
    }

    private fun renderMessage(message: String) {
        binding.addMessageBtn.apply {
            if (message.isEmpty()) {
                setImageResource(R.drawable.ic_round_plus)

                setSafeOnClickListener { store.accept(ChatEvent.UI.AddImage) }
            } else {
                setImageResource(R.drawable.ic_send_message)

                setSafeOnClickListener {
                    store.accept(ChatEvent.UI.SendMessage(stream.id, topicName))
                }
            }
        }
    }

    private fun renderScreenState(screenState: ScreenState<List<ChatUI>>) {
        when (screenState) {
            ScreenState.Init -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    messageRV.isVisible = false
                    messageField.isVisible = false
                }
            }

            ScreenState.Loading -> {
                binding.apply {
                    incShimmer.root.isVisible = true
                    messageRV.isVisible = false
                    messageField.isVisible = false
                }
            }

            is ScreenState.Content -> {
                binding.apply {
                    chatAdapter.setItems(screenState.content)

                    incShimmer.root.isVisible = false
                    messageRV.isVisible = true
                    messageField.isVisible = true
                }
            }

            is ScreenState.Error -> {
                binding.apply {
                    incShimmer.root.isVisible = false
                    messageRV.isVisible = true
                }

                showError(screenState.throwable.message) {
                    store.accept(ChatEvent.UI.Init)
                }
            }
        }
    }

    override fun handleEffect(effect: ChatEffect) = when (effect) {
        ChatEffect.CleanEditText -> {
            binding.editorMessages.text = null
        }

        ChatEffect.ScrollDown -> {
            binding.messageRV.let { rv ->
                rv.layoutManager?.smoothScrollToPosition(
                    rv, null,
                    rv.adapter?.itemCount ?: 0
                )
            }
        }

        is ChatEffect.ShowEmojiDialog -> {}

        is ChatEffect.ShowError -> {
            showError(message = effect.throwable.message)
        }
    }

    override fun onDestroyView() {
        binding.messageRV.adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val STREAM = "stream"
        private const val TOPIC_NAME = "topicName"
        private const val LAST_MESSAGE_ID = "lastMessageId"

        fun getBundle(
            stream: StreamDomain,
            topicName: String,
            lastMessageId: Int,
        ) = Bundle().apply {
            putParcelable(STREAM, stream)
            putString(TOPIC_NAME, topicName)
            putInt(LAST_MESSAGE_ID, lastMessageId)
        }

        fun getInstance(
            stream: StreamDomain,
            topicName: String,
            lastMessageId: Int,
        ) = ChatFragment().apply {
            arguments = getBundle(stream, topicName, lastMessageId)
        }
    }
}
