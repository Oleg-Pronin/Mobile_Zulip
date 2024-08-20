package pronin.oleg.zulip.domain.use_cases.messages

interface RemoveEmojiFromMessageUseCase {
    suspend operator fun invoke(messageId: Int, emojiName: String): Boolean
}
