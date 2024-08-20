package pronin.oleg.zulip.presentation.screens.profile.store

import pronin.oleg.zulip.domain.models.users.User

sealed interface ProfileCommand {
    data class LoadProfile(val user: User) : ProfileCommand
}
