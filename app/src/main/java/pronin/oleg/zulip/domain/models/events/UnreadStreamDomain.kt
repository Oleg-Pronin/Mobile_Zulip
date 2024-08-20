package pronin.oleg.zulip.domain.models.events

data class UnreadStreamDomain(
    val streamId: Int,
    val topic: String,
    val unreadMessageIds : List<Int>
)
