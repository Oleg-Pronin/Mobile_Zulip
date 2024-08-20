package pronin.oleg.zulip.data.network.models.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val id: Int,

    @SerialName("email")
    val email: String?,

    @SerialName("full_name")
    val fullName: String?,

    @SerialName("avatar_url")
    val avatarUrl: String?
)
