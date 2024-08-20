package pronin.oleg.zulip.domain.use_cases.events

import pronin.oleg.zulip.domain.models.events.EventDomain

interface GetEventsUseCase {
    suspend operator fun invoke(queryId: String): List<EventDomain>
}
