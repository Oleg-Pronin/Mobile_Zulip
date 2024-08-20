package pronin.oleg.zulip.data.network.models.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Presence(
    @SerialName("aggregated")
    val aggregated: PresenceAggregated
)