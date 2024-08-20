package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSubscribe(
    @SerialName("subscribed")
    val subscribed: Map<String, List<String>>,

    @SerialName("already_subscribed")
    val alreadySubscribed: Map<String, List<String>>,
)
