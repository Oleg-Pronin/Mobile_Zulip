package pronin.oleg.zulip.data.use_cases.people

import pronin.oleg.zulip.app.di.annotations.PeopleScope
import pronin.oleg.zulip.data.repository.UserRepository
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.domain.use_cases.people.GetAllPeopleUseCase
import javax.inject.Inject

@PeopleScope
class GetAllPeopleUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetAllPeopleUseCase {
    override suspend operator fun invoke(): List<UserDomain> =
        userRepository.getAllPeople()
}
