package pronin.oleg.zulip.presentation.screens.people.store

import pronin.oleg.zulip.presentation.states.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject

class PeopleStoreFactory @Inject constructor(
    private val peopleActor: PeopleActor,
) {
    fun create() = ElmStore(
        initialState = PeopleState(items = emptyList(), screenState = ScreenState.Loading),
        reducer = PeopleReducer(),
        actor = peopleActor
    )
}
