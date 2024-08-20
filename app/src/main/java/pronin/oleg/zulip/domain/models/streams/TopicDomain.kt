package pronin.oleg.zulip.domain.models.streams

data class TopicDomain(
    val id: Int?,
    val lastMessageId: Int,
    val name: String,
    val unreadCount: Int = 0
)
