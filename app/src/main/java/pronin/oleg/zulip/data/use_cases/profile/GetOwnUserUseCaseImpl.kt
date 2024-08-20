package pronin.oleg.zulip.data.use_cases.profile

import pronin.oleg.zulip.app.di.annotations.ProfileScope
import pronin.oleg.zulip.data.repository.UserRepository
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.domain.use_cases.profile.GetOwnUserUseCase
import javax.inject.Inject

@ProfileScope
class GetOwnUserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetOwnUserUseCase {
    override suspend operator fun invoke(): UserDomain =
        userRepository.getOwnUser()
}
