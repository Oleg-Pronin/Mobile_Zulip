package pronin.oleg.zulip.presentation.screens.people.store

sealed interface PeopleEffect {
    data class GoToProfile(val userId: Int) : PeopleEffect
    data class ShowError(val throwable: Throwable) : PeopleEffect
}
