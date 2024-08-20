package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.streams.Stream

@Serializable
data class ResponseStreams(
    @SerialName("streams")
    val streams: List<Stream>,
)
