package pronin.oleg.zulip.data.use_cases.events

import pronin.oleg.zulip.data.repository.RealTimeRepository
import pronin.oleg.zulip.domain.models.events.RegisterEventDomain
import pronin.oleg.zulip.domain.use_cases.events.RegisterEventUseCase
import javax.inject.Inject

class RegisterEventUseCaseImpl @Inject constructor(
    private val realTimeRepository: RealTimeRepository,
) : RegisterEventUseCase {
    override suspend operator fun invoke(eventTypes: List<String>, narrow: List<Array<String>>?): RegisterEventDomain =
        realTimeRepository.registerEventForTopic(eventTypes, narrow)
}
