package pronin.oleg.zulip.presentation.screens.channels.store

import android.graphics.Color
import pronin.oleg.zulip.presentation.ui.ChannelUI


sealed interface ChannelsEvent {
    sealed interface UI : ChannelsEvent {
        data object Init : UI
        data class ChangeTab(val isSubscribedTab: Boolean) : UI
        data object ReloadStream : UI
        data class LoadTopics(val streamId: Int, val streamColor: Color?) : UI
        data class HideTopics(val streamId: Int) : UI
        data class GoToChat(val streamId: Int, val topicName: String, val lastMessageId: Int) : UI
        data class Search(val searchQuery: String) : UI
    }

    sealed interface Internal : ChannelsEvent {
        data class ChannelsLoaded(val channelsUI: List<ChannelUI.StreamUI>) : Internal
        data class TopicsLoaded(val streamId: Int, val topicsUI: List<ChannelUI.TopicUI>) : Internal
        data class Error(val throwable: Throwable) : Internal
    }
}
