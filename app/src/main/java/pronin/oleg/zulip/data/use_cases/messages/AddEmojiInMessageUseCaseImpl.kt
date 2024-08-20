package pronin.oleg.zulip.data.use_cases.messages

import pronin.oleg.zulip.data.repository.MessageRepository
import pronin.oleg.zulip.domain.use_cases.messages.AddEmojiInMessageUseCase
import javax.inject.Inject

class AddEmojiInMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : AddEmojiInMessageUseCase {
    override suspend fun invoke(messageId: Int, emojiName: String) =
        messageRepository.addEmojiInMessage(messageId, emojiName)
}
