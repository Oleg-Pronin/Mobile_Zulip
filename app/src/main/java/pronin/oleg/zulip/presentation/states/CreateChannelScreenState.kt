package pronin.oleg.zulip.presentation.states

sealed interface CreateChannelScreenState {
    data object Init: CreateChannelScreenState
    data object ProcessOfCreation: CreateChannelScreenState
    data object ErrorInChannelName: CreateChannelScreenState
    data class Error(val throwable: Throwable) : CreateChannelScreenState
}
