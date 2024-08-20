package pronin.oleg.zulip.domain.use_cases.profile

import pronin.oleg.zulip.domain.models.users.User
import pronin.oleg.zulip.domain.models.users.UserDomain

interface GetProfileUseCase {
    suspend operator fun invoke(user: User): UserDomain
}
