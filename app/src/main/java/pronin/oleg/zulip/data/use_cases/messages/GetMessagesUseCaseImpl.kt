package pronin.oleg.zulip.data.use_cases.messages

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.app.di.annotations.ChatScope
import pronin.oleg.zulip.data.repository.MessageRepository
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.domain.use_cases.messages.GetMessagesUseCase
import pronin.oleg.zulip.presentation.states.RequestResultState
import javax.inject.Inject

@ChatScope
class GetMessagesUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository,
) : GetMessagesUseCase {
    override suspend operator fun invoke(
        streamId: Int,
        topicName: String,
        lastMessageId: Int,
        numAfter: Int,
        numBefore: Int,
    ): Flow<RequestResultState<List<MessageDomain>>> =
        messageRepository.getTopicMessages(streamId, topicName, lastMessageId, numAfter, numBefore)
}
