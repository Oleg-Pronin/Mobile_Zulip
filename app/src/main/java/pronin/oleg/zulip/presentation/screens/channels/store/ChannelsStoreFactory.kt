package pronin.oleg.zulip.presentation.screens.channels.store

import pronin.oleg.zulip.presentation.states.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject

class ChannelsStoreFactory @Inject constructor(
    private val channelsActor: ChannelsActor,
) {
    fun create() = ElmStore(
        initialState = ChannelsState(
            isSubscribedTab = true,
            items = emptyList(),
            screenState = ScreenState.Init
        ),
        reducer = ChannelsReducer(),
        actor = channelsActor
    )
}
