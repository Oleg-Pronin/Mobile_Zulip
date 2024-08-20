package pronin.oleg.zulip.presentation.screens.channels.store

import android.graphics.Color

sealed interface ChannelsCommand {
    data class LoadStreams(val isSubscribed: Boolean) : ChannelsCommand
    data class LoadTopic(val streamId: Int, val streamColor: Color?) : ChannelsCommand
}
