package pronin.oleg.zulip.domain.models.events

import kotlinx.datetime.LocalDateTime
import pronin.oleg.zulip.domain.models.users.PresenceDomain

data class RegisterEventDomain(
    val queueId: String,
    val lastEventId: Int,
    val serverDateTime: LocalDateTime?,
    val unreadStreamMessages: List<UnreadStreamDomain>?,
    val presences: Map<String, PresenceDomain>?,
)
