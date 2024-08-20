package pronin.oleg.zulip.data.use_cases.channels

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.app.di.annotations.ChannelScope
import pronin.oleg.zulip.data.repository.StreamRepository
import pronin.oleg.zulip.domain.models.streams.TopicDomain
import pronin.oleg.zulip.domain.use_cases.topics.GetTopicByStreamIdUseCase
import pronin.oleg.zulip.presentation.states.RequestResultState
import javax.inject.Inject

@ChannelScope
class GetTopicByStreamIdUseCaseImpl @Inject constructor(
    private val streamRepository: StreamRepository,
) : GetTopicByStreamIdUseCase {
    override suspend operator fun invoke(streamId: Int): Flow<RequestResultState<List<TopicDomain>>> =
        streamRepository.getTopics(streamId)
}
