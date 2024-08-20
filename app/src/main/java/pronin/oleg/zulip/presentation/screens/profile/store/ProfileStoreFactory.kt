package pronin.oleg.zulip.presentation.screens.profile.store

import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.states.State
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject

class ProfileStoreFactory @Inject constructor(
    private val profileActor: ProfileActor,
) {
    fun create() = ElmStore(
        initialState = State(screenState = ScreenState.Loading),
        reducer = ProfileReducer(),
        actor = profileActor
    )
}
