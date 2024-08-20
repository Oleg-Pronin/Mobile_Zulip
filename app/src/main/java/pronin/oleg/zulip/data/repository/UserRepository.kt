package pronin.oleg.zulip.data.repository

import pronin.oleg.zulip.data.network.api.ZulipChatApi
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.mappers.toDomain
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val api: ZulipChatApi
) {
    suspend fun getOwnUser(): UserDomain =
        api.getOwnUser().toDomain()

    suspend fun getUserById(userId: Int): UserDomain =
        api.getUserById(userId).user.toDomain()

    suspend fun getAllPeople(): List<UserDomain> =
        api.getAllUsers().users.map { it.toDomain() }
}
