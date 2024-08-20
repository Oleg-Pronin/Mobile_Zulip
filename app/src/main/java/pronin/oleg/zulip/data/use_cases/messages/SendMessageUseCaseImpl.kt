package pronin.oleg.zulip.data.use_cases.messages

import pronin.oleg.zulip.app.di.annotations.ChatScope
import pronin.oleg.zulip.data.repository.MessageRepository
import pronin.oleg.zulip.domain.use_cases.messages.SendMessageUseCase
import javax.inject.Inject

@ChatScope
class SendMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : SendMessageUseCase {
    override suspend operator fun invoke(streamId: Int, topicName: String, content: String) =
        messageRepository.sendMessage(streamId, topicName, content)
}
