package pronin.oleg.zulip.presentation.screens.people.store

import pronin.oleg.zulip.presentation.ui.UserUI

sealed interface PeopleEvent {
    sealed interface UI : PeopleEvent {
        data object Init : UI
        data class Search(val searchQuery: String) : UI
        data class GoToProfile(val userId: Int) : UI
    }

    sealed interface Internal : PeopleEvent {
        data class PeopleLoaded(val peopleUI: List<UserUI>) : Internal
        data class Error(val throwable: Throwable) : Internal
    }
}
