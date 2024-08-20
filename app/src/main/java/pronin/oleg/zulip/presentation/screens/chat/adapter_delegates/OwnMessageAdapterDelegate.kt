package pronin.oleg.zulip.presentation.screens.chat.adapter_delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.databinding.ItemOwnMessageBinding
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.presentation.custom_view.emoji.EmojiView
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI

class OwnMessageAdapterDelegate(
    private val onClickEmoji: ((
        messageId: Int,
        isSelected: Boolean,
        emojiCode: String,
        emojiName: String?,
    ) -> Unit),
) : AdapterDelegate<ChatUI> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemOwnMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickEmoji
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
        item is ChatUI.MessageUI && item.isOwnMessage

    class ViewHolder(
        private val binding: ItemOwnMessageBinding,
        private val onClickEmoji: ((
            messageId: Int,
            isSelected: Boolean,
            emojiCode: String,
            emojiName: String?,
        ) -> Unit),
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChatUI.MessageUI) {
            binding.apply { setupUi(model) }
        }

        private fun ItemOwnMessageBinding.setupUi(model: ChatUI.MessageUI) {
            messageLayout.apply {
                messageInfoContent = model.content
                messageInfoUserFullName = model.senderFullName

                clearEmoji()

                model.reactions.groupBy { it.emojiCode }
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