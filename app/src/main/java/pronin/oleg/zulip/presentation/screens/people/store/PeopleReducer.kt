package pronin.oleg.zulip.presentation.screens.people.store

import pronin.oleg.zulip.presentation.states.ScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class PeopleReducer : ScreenDslReducer<
        PeopleEvent,
        PeopleEvent.UI,
        PeopleEvent.Internal,
        PeopleState,
        PeopleEffect,
        PeopleCommand
        >(PeopleEvent.UI::class, PeopleEvent.Internal::class) {

    override fun Result.internal(event: PeopleEvent.Internal) = when (event) {
        is PeopleEvent.Internal.PeopleLoaded -> state {
            copy(
                items = event.peopleUI,
                screenState = ScreenState.Content(event.peopleUI)
            )
        }

        is PeopleEvent.Internal.Error -> effects {
            +PeopleEffect.ShowError(event.throwable)
        }
    }

    override fun Result.ui(event: PeopleEvent.UI) = when (event) {
        PeopleEvent.UI.Init -> commands {
            +PeopleCommand.LoadPeople
        }

        is PeopleEvent.UI.GoToProfile -> effects {
            +PeopleEffect.GoToProfile(event.userId)
        }

        is PeopleEvent.UI.Search -> state {
            copy(
                screenState = ScreenState.Content(
                    this.items.filter {
                        it.fullName.contains(
                            event.searchQuery,
                            true
                        )
                    }
                )
            )
        }
    }
}
