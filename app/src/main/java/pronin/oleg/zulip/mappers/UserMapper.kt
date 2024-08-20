package pronin.oleg.zulip.mappers

import pronin.oleg.zulip.data.network.models.users.Presence
import pronin.oleg.zulip.data.network.models.users.User
import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.domain.models.users.PresenceDomain
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.presentation.ui.UserUI
import pronin.oleg.zulip.utils.toLocalDateTime

fun User.toDomain() = UserDomain(
    id = id,
    fullName = fullName,
    email = email ?: "",
    avatarUrl = avatarUrl
)

fun UserDomain.toUI(status: StatusPresence) = UserUI(
    id = id,
    fullName = fullName ?: "Неизвестный пользователь",
    email = email,
    avatar = avatarUrl ?: "",
    status = status
)

fun Presence.toDomain() = PresenceDomain(
    status = StatusPresence.byCode(aggregated.status),
    dateTime = aggregated.timestamp.toLocalDateTime()
)
