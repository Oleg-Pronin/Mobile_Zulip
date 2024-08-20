package pronin.oleg.zulip.domain.use_cases.events

import pronin.oleg.zulip.domain.models.events.RegisterEventDomain

interface RegisterEventUseCase {
    suspend operator fun invoke(
        eventTypes: List<String>,
        narrow: List<Array<String>>? = null,
    ): RegisterEventDomain
}
