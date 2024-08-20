package pronin.oleg.zulip.data.network.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.messages.Message
import pronin.oleg.zulip.data.network.models.users.Presence

@Serializable
data class Event(
    @SerialName("id")
    val id: Int,

    @SerialName("type")
    val type: String,

    @SerialName("message")
    val message: Message? = null,

    @SerialName("presences")
    val presences: Presence? = null,

    @SerialName("op")
    val action: String? = null,

    @SerialName("user_id")
    val userId: Int? = null,

    @SerialName("message_id")
    val messageId: Int? = null,

    @SerialName("emoji_name")
    val emojiName: String? = null,

    @SerialName("emoji_code")
    val emojiCode: String? = null,
)
