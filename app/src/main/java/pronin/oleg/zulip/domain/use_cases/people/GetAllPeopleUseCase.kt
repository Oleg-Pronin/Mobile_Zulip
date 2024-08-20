package pronin.oleg.zulip.domain.use_cases.people

import pronin.oleg.zulip.domain.models.users.UserDomain

interface GetAllPeopleUseCase {
    suspend operator fun invoke(): List<UserDomain>
}
