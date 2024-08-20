package pronin.oleg.zulip.presentation.screens.people.store

import pronin.oleg.zulip.presentation.states.ListState
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.UserUI

data class PeopleState(
    override val items: List<UserUI>,
    override val screenState: ScreenState<List<UserUI>>
) : ListState<UserUI>
