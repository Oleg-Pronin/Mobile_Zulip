package pronin.oleg.zulip.data.network.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadStream(
    @SerialName("stream_id")
    val streamId: Int,

    @SerialName("topic")
    val topic: String,

    @SerialName("unread_message_ids")
    val unreadMessageIds : List<Int>
)
