package pronin.oleg.zulip.data.use_cases.messages

import pronin.oleg.zulip.data.repository.MessageRepository
import pronin.oleg.zulip.domain.use_cases.messages.RemoveEmojiFromMessageUseCase
import javax.inject.Inject

class RemoveEmojiInMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : RemoveEmojiFromMessageUseCase {
    override suspend fun invoke(messageId: Int, emojiName: String) =
        messageRepository.deleteEmojiInMessage(messageId, emojiName)
}
