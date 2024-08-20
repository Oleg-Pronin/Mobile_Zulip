package pronin.oleg.zulip.data.network.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pronin.oleg.zulip.data.network.models.messages.Message

@Serializable
data class ResponseMessages(
    @SerialName("messages")
    val messages: List<Message> = emptyList(),
)
