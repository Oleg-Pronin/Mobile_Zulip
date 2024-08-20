package pronin.oleg.zulip.presentation.screens.chat.store

sealed interface ChatEffect {
    data object CleanEditText : ChatEffect
    data object ScrollDown : ChatEffect
    data class ShowEmojiDialog(val messageId: String) : ChatEffect
    data class ShowError(val throwable: Throwable) : ChatEffect
}
