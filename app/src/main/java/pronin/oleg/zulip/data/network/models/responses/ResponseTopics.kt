package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.streams.Topic

@Serializable
data class ResponseTopics(
    @SerialName("topics")
    val topics: List<Topic>,
)
