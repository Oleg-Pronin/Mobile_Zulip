package pronin.oleg.zulip.domain.models.users

data class UserDomain(
    val id: Int = 0,
    val fullName: String? = null,
    val email: String = "",
    val avatarUrl: String? = null,
    val isOnline: Boolean = false
)
