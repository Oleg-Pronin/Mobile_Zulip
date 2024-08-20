package pronin.oleg.zulip.presentation.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import pronin.oleg.zulip.domain.enums.EmojiAction
import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.domain.models.messages.ReactionDomain
import pronin.oleg.zulip.presentation.base.BaseItemUI

sealed class ChatUI : BaseItemUI {
    abstract override val id: Int

    data class DateUI(
        override val id: Int,
        val date: LocalDate,
    ) : ChatUI()

    data class MessageUI(
        override val id: Int,
        val isOwnMessage: Boolean = false,
        val dateTime: LocalDateTime,
        val senderAvatar: String? = null,
        val senderFullName: String,
        val content: String,
        val reactions: List<ReactionDomain> = emptyList(),
    ) : ChatUI() {
        fun changeReaction(
            reaction: ReactionEventDomain,
            isSelectedReaction: Boolean,
        ) = this.copy(
            reactions = this.reactions.toMutableList().apply {
                when (reaction.action) {
                    EmojiAction.ADD -> add(
                        ReactionDomain(
                            userId = reaction.userId,
                            emojiCode = reaction.emojiCode,
                            emojiName = reaction.emojiName,
                            isSelected = isSelectedReaction
                        )
                    )

                    EmojiAction.REMOVE -> removeIf { deletedItem ->
                        deletedItem.emojiCode == reaction.emojiCode
                                && deletedItem.userId == reaction.userId
                    }

                    EmojiAction.UNKNOWN -> Unit
                }
            }.toList()
        )
    }

    companion object {
        fun createMessageUI(message: MessageDomain, ownUserId: Int) = MessageUI(
            id = message.id,
            isOwnMessage = message.senderId == ownUserId,
            dateTime = message.dateTime,
            senderAvatar = message.senderAvatar,
            senderFullName = message.senderFullName,
            content = message.content
        )
    }
}
