package pronin.oleg.zulip.domain.models.messages

import kotlinx.datetime.LocalDateTime

data class MessageDomain(
    val id: Int,
    val dateTime: LocalDateTime,
    val senderId: Int,
    val senderAvatar: String? = null,
    val senderFullName: String,
    val content: String,
    val reactions: List<ReactionDomain> = emptyList(),
    val streamId: Int,
    val subject: String
)
