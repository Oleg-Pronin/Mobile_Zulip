package pronin.oleg.zulip.presentation.screens.channels.store

import pronin.oleg.zulip.domain.models.streams.StreamDomain

sealed interface ChannelsEffect {
    data class GoToChat(
        val stream: StreamDomain,
        val topicName: String,
        val lastMessageId: Int,
    ) : ChannelsEffect

    data class ShowError(val throwable: Throwable) : ChannelsEffect
}
