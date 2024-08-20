package pronin.oleg.zulip.presentation.dialogs.create_channel.store

import pronin.oleg.zulip.presentation.states.CreateChannelScreenState

data class CreateChannelState(
    val channelName: String = "",
    val channelDescription: String = "",
    val inviteOnly: Boolean = false,
    val historyPublicToSubscribers: Boolean = true,
    val isCreateButtonLocked: Boolean = true,
    val screenState: CreateChannelScreenState = CreateChannelScreenState.Init
)
