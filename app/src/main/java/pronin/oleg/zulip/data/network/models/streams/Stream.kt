package pronin.oleg.zulip.data.network.models.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stream(
    @SerialName("stream_id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("stream_weekly_traffic")
    val weeklyTraffic: Int? = 0,

    @SerialName("invite_only")
    val isPrivate: Boolean = false,
)
