package pronin.oleg.zulip.presentation.screens.chat.store

import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.presentation.states.ScreenState
import vivid.money.elmslie.core.store.ElmStore
import javax.inject.Inject

class ChatStoreFactory @Inject constructor(
    private val chatActor: ChatActor,
) {
    fun create(
        streamId: Int,
        topicName: String,
        lastMessageId: Int,
    ) = ElmStore(
        initialState = ChatState(
            streamId = streamId,
            topicName = topicName,
            lastMessageId = lastMessageId,
            ownUser = UserDomain(),
            message = "",
            items = emptyList(),
            screenState = ScreenState.Loading
        ),
        reducer = ChatReducer(),
        actor = chatActor
    )
}
