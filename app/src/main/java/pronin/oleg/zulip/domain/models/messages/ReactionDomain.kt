package pronin.oleg.zulip.domain.models.messages

data class ReactionDomain(
    val userId: Int,
    val emojiName: String,
    val emojiCode: String,
    val isSelected: Boolean = false
)
