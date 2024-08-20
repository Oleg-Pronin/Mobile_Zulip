package pronin.oleg.zulip.data.use_cases.events

import pronin.oleg.zulip.data.repository.RealTimeRepository
import pronin.oleg.zulip.domain.models.events.EventDomain
import pronin.oleg.zulip.domain.use_cases.events.GetEventsUseCase
import javax.inject.Inject

class GetEventsUseCaseImpl @Inject constructor(
    private val realTimeRepository: RealTimeRepository,
) : GetEventsUseCase {
    override suspend operator fun invoke(queryId: String): List<EventDomain> =
        realTimeRepository.getEvents(queueId = queryId)
}
