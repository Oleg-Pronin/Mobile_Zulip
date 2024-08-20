package pronin.oleg.zulip.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class NarrowRequest<T>(
    val negated: Boolean,
    val operator: String,
    val operand: T,
)
