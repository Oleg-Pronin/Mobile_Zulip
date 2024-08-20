package pronin.oleg.zulip.data.network.models.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscribedStream(
    @SerialName("stream_id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("color")
    val color: String,

    @SerialName("pin_to_top")
    val pinToTop: Boolean = false,

    @SerialName("stream_weekly_traffic")
    val weeklyTraffic: Int? = 0,

    @SerialName("invite_only")
    val isPrivate: Boolean = false,
)
