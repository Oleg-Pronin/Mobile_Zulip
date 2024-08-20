package pronin.oleg.zulip.domain.use_cases.profile

import pronin.oleg.zulip.domain.models.users.UserDomain

interface GetOwnUserUseCase {
    suspend operator fun invoke(): UserDomain
}
