package pronin.oleg.zulip.presentation.dialogs.create_channel

import kotlinx.serialization.Serializable

@Serializable
data class CreateChannelDialogResult(
    val isSuccessCreateChannel: Boolean
): java.io.Serializable {
    companion object {
        const val REQUEST_KEY = "create_channel_dialog_request_key"
        const val RESULT_KEY = "create_channel_dialog_result_key"
    }
}
