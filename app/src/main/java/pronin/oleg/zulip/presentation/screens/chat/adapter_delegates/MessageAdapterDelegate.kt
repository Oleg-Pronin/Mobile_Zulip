package pronin.oleg.zulip.presentation.screens.chat.adapter_delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.databinding.ItemMessageBinding
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.presentation.custom_view.emoji.EmojiView
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI
import pronin.oleg.zulip.utils.setSafeOnClickListener

class MessageAdapterDelegate(
    private val onClickEmoji: ((
        messageId: Int,
        isSelected: Boolean,
        emojiCode: String,
        emojiName: String?,
    ) -> Unit),
    private val onClickAddEmoji: ((messageId: Int) -> Unit),
) : AdapterDelegate<ChatUI> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickEmoji,
            onClickAddEmoji
        )

    override fun onBindViewHolder(
        item: ChatUI,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    ) {
        (holder as ViewHolder).bind(item as ChatUI.MessageUI)
    }

    override fun isForViewType(item: BaseItemUI): Boolean =
        item is ChatUI.MessageUI && !item.isOwnMessage

    class ViewHolder(
        private val binding: ItemMessageBinding,
        private val onClickEmoji: ((
            messageId: Int,
            isSelected: Boolean,
            emojiCode: String,
            emojiName: String?,
        ) -> Unit),
        private val onClickAddEmoji: ((Int) -> Unit),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatUI.MessageUI) {
            binding.apply { setupUi(model) }
        }

        private fun ItemMessageBinding.setupUi(model: ChatUI.MessageUI) {
            messageLayout.apply {
                setOnLongClickListener {
                    if (model.reactions.isEmpty())
                        onClickAddEmoji.invoke(model.id)

                    true
                }

                setSafeOnClickListener {
                    onClickAddEmoji.invoke(model.id)
                }

                messageInfoContent = model.content
                messageInfoUserFullName = model.senderFullName
                messageUserAvatar = model.senderAvatar

                clearEmoji()

                model.reactions
                    .groupBy { it.emojiCode }
                    .forEach { (emojiCode, listEmoji) ->
                        addEmoji(
                            emojiCode = emojiCode,
                            emojiName = listEmoji.first().emojiName,
                            emojiCount = listEmoji.size,
                            isSelectedEmoji = listEmoji.any { it.isSelected },
                            onClickListener = {
                                if (it is EmojiView)
                                    onClickEmoji.invoke(
                                        model.id,
                                        it.isSelected,
                                        it.emojiCode,
                                        it.emojiName
                                    )
                            }
                        )
                    }
            }
        }
    }
}