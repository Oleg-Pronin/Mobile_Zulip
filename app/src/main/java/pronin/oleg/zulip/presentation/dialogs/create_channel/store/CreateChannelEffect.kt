package pronin.oleg.zulip.presentation.dialogs.create_channel.store

sealed interface CreateChannelEffect {
    data class DismissDialog(val result: Boolean) : CreateChannelEffect
    data class ShowSuccess(val message: String) : CreateChannelEffect
    data class ShowError(val throwable: Throwable) : CreateChannelEffect
}
