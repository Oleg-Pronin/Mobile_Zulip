package pronin.oleg.zulip.domain.use_cases.messages

interface AddEmojiInMessageUseCase {
    suspend operator fun invoke(messageId: Int, emojiName: String): Boolean
}
