package pronin.oleg.zulip.presentation.dialogs.emoji

import kotlinx.serialization.Serializable

@Serializable
data class EmojiDialogResult(
    val messageId: Int,
    val emojiCode: String,
    val emojiName: String,
) : java.io.Serializable {
    companion object {
        const val REQUEST_KEY = "emoji_dialog_request_key"
        const val RESULT_KEY = "emoji_dialog_result_key"
    }
}
