package pronin.oleg.zulip.domain.models.users

sealed class User {
    data object Own : User()
    data class Other(val id: Int) : User()
}
