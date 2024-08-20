package pronin.oleg.zulip.data.network.models.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("id")
    val id: Int,

    @SerialName("content")
    val content: String,

    @SerialName("reactions")
    val reactions: List<Reaction>,

    @SerialName("subject")
    val subject: String,

    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("avatar_url")
    val avatarUrl: String,

    @SerialName("content_type")
    val contentType: String,

    @SerialName("display_recipient")
    val displayRecipient: String,

    @SerialName("is_me_message")
    val isMeMessage: Boolean,

    @SerialName("recipient_id")
    val recipientId: Int,

    @SerialName("sender_id")
    val senderId: Int,

    @SerialName("sender_email")
    val senderEmail: String,

    @SerialName("sender_full_name")
    val senderFullName: String,

    @SerialName("sender_realm_str")
    val senderRealmStr: String,

    @SerialName("stream_id")
    val streamId: Int,
)
