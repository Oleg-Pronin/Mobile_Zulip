package pronin.oleg.zulip.presentation.dialogs.create_channel.store

sealed interface CreateChannelCommand {
    data class CreateChannel(
        val name: String,
        val description: String,
        val inviteOnly: Boolean,
        val historyPublicToSubscribers: Boolean,
    ) : CreateChannelCommand
}
