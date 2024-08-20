package pronin.oleg.zulip.domain.use_cases.topics

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.domain.models.streams.TopicDomain
import pronin.oleg.zulip.presentation.states.RequestResultState

interface GetTopicByStreamIdUseCase {
    suspend operator fun invoke(streamId: Int): Flow<RequestResultState<List<TopicDomain>>>
}
