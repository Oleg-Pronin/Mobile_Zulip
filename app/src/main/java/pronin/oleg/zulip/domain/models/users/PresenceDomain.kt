package pronin.oleg.zulip.domain.models.users

import kotlinx.datetime.LocalDateTime
import pronin.oleg.zulip.domain.enums.StatusPresence

data class PresenceDomain(
    val status: StatusPresence,
    val dateTime: LocalDateTime
)
