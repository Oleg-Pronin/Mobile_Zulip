package pronin.oleg.zulip.app.di.models.emojis

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmojiCodes(
    @SerialName("codepoint_to_name")
    val codepoinToName: Map<String, String>,
)
