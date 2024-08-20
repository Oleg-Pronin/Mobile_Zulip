package pronin.oleg.zulip.data.network.models.streams

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    @SerialName("max_id")
    val lastMessageId: Int,

    @SerialName("name")
    val name: String
)
