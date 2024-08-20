package pronin.oleg.zulip.domain.use_cases.messages

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.presentation.states.RequestResultState

interface GetMessagesUseCase {
    suspend operator fun invoke(
        streamId: Int,
        topicName: String,
        lastMessageId: Int,
        numAfter: Int,
        numBefore: Int,
    ): Flow<RequestResultState<List<MessageDomain>>>
}
