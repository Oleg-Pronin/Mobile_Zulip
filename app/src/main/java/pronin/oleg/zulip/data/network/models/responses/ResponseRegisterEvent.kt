package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.events.UnreadMessage
import pronin.oleg.zulip.data.network.models.users.Presence

@Serializable
data class ResponseRegisterEvent(
    @SerialName("queue_id")
    val queueId: String,

    @SerialName("last_event_id")
    val lastEventId: Int?,

    @SerialName("server_timestamp")
    val serverTimestamp: Float? = null,

    @SerialName("unread_msgs")
    val unreadMessages: UnreadMessage? = null,

    @SerialName("presences")
    val presences: Map<String, Presence>? = null,
)
