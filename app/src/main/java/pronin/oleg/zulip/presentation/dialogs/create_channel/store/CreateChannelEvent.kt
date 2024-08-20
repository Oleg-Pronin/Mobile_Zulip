package pronin.oleg.zulip.presentation.dialogs.create_channel.store

import pronin.oleg.zulip.domain.enums.ChannelPermission


sealed interface CreateChannelEvent {
    sealed interface UI : CreateChannelEvent {
        data class ChangeChannelName(val channelName: String) : UI
        data class ChangeChannelDescription(val channelDescription: String) : UI
        data class ChangeChannelPermissions(val channelPermission: ChannelPermission) : UI
        data object CreateChannel : UI
    }

    sealed interface Internal : CreateChannelEvent {
        data object CreatedChannel : Internal
        data object ChannelAlreadyExists : Internal
        data class Error(val throwable: Throwable) : Internal
    }
}
