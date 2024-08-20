package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.events.Event

@Serializable
data class ResponseQueueEvent(
    @SerialName("events")
    val events: List<Event>,
)
