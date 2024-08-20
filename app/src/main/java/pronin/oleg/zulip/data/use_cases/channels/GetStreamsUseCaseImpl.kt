package pronin.oleg.zulip.data.use_cases.channels

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.app.di.annotations.ChannelScope
import pronin.oleg.zulip.data.repository.StreamRepository
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.domain.use_cases.streams.GetStreamsUseCase
import pronin.oleg.zulip.presentation.states.RequestResultState
import javax.inject.Inject

@ChannelScope
class GetStreamsUseCaseImpl @Inject constructor(
    private val streamRepository: StreamRepository,
) : GetStreamsUseCase {
    override suspend operator fun invoke(isSubscribed: Boolean): Flow<RequestResultState<List<StreamDomain>>> =
        when (isSubscribed) {
            true -> streamRepository.getSubscribedStreams()

            false -> streamRepository.getAllStreams()
        }
}
