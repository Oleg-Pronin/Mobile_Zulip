package pronin.oleg.zulip.domain.models.events

import pronin.oleg.zulip.domain.enums.EmojiAction

data class ReactionEventDomain(
    val action: EmojiAction,
    val userId: Int,
    val messageId: Int,
    val emojiName: String,
    val emojiCode: String,
)
