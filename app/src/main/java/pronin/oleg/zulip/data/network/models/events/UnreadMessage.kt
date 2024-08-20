package pronin.oleg.zulip.data.network.models.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnreadMessage(
    @SerialName("streams")
    val streams: List<UnreadStream>?
)
