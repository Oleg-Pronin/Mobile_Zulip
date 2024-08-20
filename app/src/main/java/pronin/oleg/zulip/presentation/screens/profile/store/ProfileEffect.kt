package pronin.oleg.zulip.presentation.screens.profile.store

sealed interface ProfileEffect {
    data class ShowError(val throwable: Throwable) : ProfileEffect
}
