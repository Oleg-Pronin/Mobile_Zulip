package pronin.oleg.zulip.domain.use_cases.streams

import kotlinx.coroutines.flow.Flow
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.presentation.states.RequestResultState

interface GetStreamsUseCase {
    suspend operator fun invoke(isSubscribed: Boolean): Flow<RequestResultState<List<StreamDomain>>>
}
