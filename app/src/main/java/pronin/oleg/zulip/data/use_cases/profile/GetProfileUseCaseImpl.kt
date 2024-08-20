package pronin.oleg.zulip.data.use_cases.profile

import pronin.oleg.zulip.app.di.annotations.ProfileScope
import pronin.oleg.zulip.data.repository.UserRepository
import pronin.oleg.zulip.domain.models.users.User
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.domain.use_cases.profile.GetProfileUseCase
import javax.inject.Inject

@ProfileScope
class GetProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
) : GetProfileUseCase {
    override suspend operator fun invoke(user: User): UserDomain =
        when (user) {
            is User.Other -> userRepository.getUserById(user.id)
            User.Own -> userRepository.getOwnUser()
        }
}
