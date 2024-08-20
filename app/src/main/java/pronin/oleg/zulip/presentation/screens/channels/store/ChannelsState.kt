package pronin.oleg.zulip.presentation.screens.channels.store

import pronin.oleg.zulip.presentation.states.ListState
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChannelUI

data class ChannelsState(
    val isSubscribedTab: Boolean,
    override val items: List<ChannelUI>,
    override val screenState: ScreenState<List<ChannelUI>>
) : ListState<ChannelUI>
