package pronin.oleg.zulip.presentation.dialogs.create_channel.store

import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject

class CreateChannelStoreFactory @Inject constructor(
    private val createChannelActor: CreateChannelActor
) {
    fun create() = ElmStore(
        initialState = CreateChannelState(),
        reducer = CreateChannelReducer(),
        actor = createChannelActor
    )
}
