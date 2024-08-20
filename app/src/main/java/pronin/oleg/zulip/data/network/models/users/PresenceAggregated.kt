package pronin.oleg.zulip.data.network.models.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceAggregated(
    @SerialName("status")
    val status: String,

    @SerialName("timestamp")
    val timestamp: Long
)
