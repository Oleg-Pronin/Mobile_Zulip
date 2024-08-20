package pronin.oleg.zulip.presentation.screens.people.store

sealed interface PeopleCommand {
    data object LoadPeople : PeopleCommand
}
