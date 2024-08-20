package pronin.oleg.zulip.presentation.screens.profile.store

import pronin.oleg.zulip.domain.models.users.User
import pronin.oleg.zulip.presentation.ui.UserUI

sealed interface ProfileEvent {
    sealed interface UI : ProfileEvent {
        data class Init(val user: User) : UI
    }

    sealed interface Internal : ProfileEvent {
        data class ProfileLoaded(val userUI: UserUI) : Internal
        data class Error(val throwable: Throwable) : Internal
    }
}
