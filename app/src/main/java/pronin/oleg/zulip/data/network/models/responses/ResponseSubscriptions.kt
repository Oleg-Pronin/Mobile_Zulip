package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.streams.SubscribedStream

@Serializable
data class ResponseSubscriptions(
    @SerialName("subscriptions")
    val subscriptions: List<SubscribedStream>,
)
