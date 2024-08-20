package pronin.oleg.zulip.mappers

import pronin.oleg.zulip.data.database.models.chat.MessageAndReactionDBO
import pronin.oleg.zulip.data.database.models.chat.MessageDBO
import pronin.oleg.zulip.data.database.models.chat.ReactionDBO
import pronin.oleg.zulip.data.network.models.messages.Message
import pronin.oleg.zulip.data.network.models.messages.Reaction
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.domain.models.messages.ReactionDomain
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.utils.getEmojiByUnicode
import pronin.oleg.zulip.utils.toLocalDateTime

fun Message.toDomain() = MessageDomain(
    id = id,
    dateTime = timestamp.toLocalDateTime(),
    senderId = senderId,
    senderAvatar = avatarUrl,
    senderFullName = senderFullName,
    content = content,
    reactions = reactions.map { it.toDomain() },
    streamId = streamId,
    subject = subject
)

fun Reaction.toDomain() = ReactionDomain(
    userId = userId,
    emojiCode = getEmojiByUnicode(emojiCode),
    emojiName = emojiName,
    isSelected = false
)

fun MessageAndReactionDBO.toDomain() = MessageDomain(
    id = message.id,
    dateTime = message.dateTime,
    senderId = message.senderId,
    senderAvatar = message.senderAvatar,
    senderFullName = message.senderFullName,
    content = message.content,
    reactions = reactions.map { it.toDomain() },
    streamId = message.streamId,
    subject = message.subject
)

fun ReactionDBO.toDomain() = ReactionDomain(
    userId = userId,
    emojiName = emojiName,
    emojiCode = emojiCode,
    isSelected = isSelected
)

fun MessageDomain.toDBO() = MessageAndReactionDBO(
    message = this.toMessageDBO(),
    reactions = reactions.map { it.toReactionDBO() }
)

fun MessageDomain.toMessageDBO() = MessageDBO(
    id = id,
    dateTime = dateTime,
    senderId = senderId,
    senderAvatar = senderAvatar,
    senderFullName = senderFullName,
    content = content,
    streamId = streamId,
    subject = subject
)

fun ReactionDomain.toReactionDBO() = ReactionDBO(
    id = 0,
    messageId = 0,
    userId = userId,
    emojiName = emojiName,
    emojiCode = emojiCode,
    isSelected = isSelected
)

fun List<MessageDomain>.toChatUI(ownUserId: Int): List<ChatUI> {
    val resultUI: MutableList<ChatUI> = mutableListOf()

    this.groupBy { it.dateTime.date }.forEach { (date, messages) ->
        resultUI += ChatUI.DateUI(
            id = date.hashCode(),
            date = date
        )

        messages.forEach { message ->
            resultUI += ChatUI.MessageUI(
                id = message.id,
                isOwnMessage = message.senderId == ownUserId,
                dateTime = message.dateTime,
                senderAvatar = message.senderAvatar,
                senderFullName = message.senderFullName,
                content = message.content,
                reactions = message.reactions.map { reaction ->
                    if (reaction.userId == ownUserId)
                        reaction.copy(isSelected = true)
                    else
                        reaction
                })
        }
    }

    return resultUI
}
