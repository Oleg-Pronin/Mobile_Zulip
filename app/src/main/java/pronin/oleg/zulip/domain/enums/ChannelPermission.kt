package pronin.oleg.zulip.domain.enums

import androidx.annotation.StringRes
import pronin.oleg.zulip.R

enum class ChannelPermission(
    @StringRes val title: Int,
    @StringRes val description: Int
) {
    PUBLIC(
        title = R.string.public_permission,
        description = R.string.public_permission_desc
    ),

    PRIVATE_SHARED_HISTORY(
        title = R.string.permission_private_shared_history,
        description = R.string.permission_private_shared_history_desc
    ),

    PRIVATE_PROTECTED_HISTORY(
        title = R.string.permission_private_protected_history,
        description = R.string.permission_private_protected_history_desc
    )
}
