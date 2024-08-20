package pronin.oleg.zulip.domain.enums

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import pronin.oleg.zulip.domain.models.users.PresenceDomain
import java.time.Duration

enum class StatusPresence {
    ACTIVE, IDLE, OFFLINE;

    companion object {
        fun byCode(code: String) = when (code) {
            "active" -> ACTIVE
            "idle" -> IDLE
            else -> OFFLINE
        }

        fun getStatus(serverDateTime: LocalDateTime?, presence: PresenceDomain?) =
            if (serverDateTime == null || presence == null)
                OFFLINE
            else {
                val duration = Duration.between(
                    presence.dateTime.toJavaLocalDateTime(),
                    serverDateTime.toJavaLocalDateTime()
                )

                if (duration <= Duration.ofMinutes(15))
                    if (presence.status == ACTIVE)
                        ACTIVE
                    else
                        IDLE
                else
                    OFFLINE
            }
    }
}
