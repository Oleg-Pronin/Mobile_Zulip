package pronin.oleg.zulip.presentation.screens.chat.store

import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChatUI

data class ChatState (
    val streamId: Int,
    val topicName: String,
    val lastMessageId: Int,
    val ownUser: UserDomain = UserDomain(),
    val message: String = "",
    var items: List<ChatUI> = emptyList(),
    val screenState: ScreenState<List<ChatUI>> = ScreenState.Loading
)
