package pronin.oleg.zulip.presentation.ui

import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.presentation.base.BaseItemUI

data class UserUI(
    override val id: Int,
    val fullName: String,
    val email: String,
    val avatar: String = "",
    val status: StatusPresence,
) : BaseItemUI
