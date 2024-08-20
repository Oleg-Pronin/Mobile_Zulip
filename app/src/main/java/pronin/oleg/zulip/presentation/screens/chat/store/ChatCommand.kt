package pronin.oleg.zulip.presentation.screens.chat.store

sealed interface ChatCommand {
    data object LoadOwnUser : ChatCommand

    data class LoadData(
        val streamId: Int,
        val topicName: String,
        val lastMessageId: Int,
        val ownUserId: Int,
    ) : ChatCommand

    data class LoadMore(
        val streamId: Int,
        val topicName: String,
        val lastMessageId: Int,
        val ownUserId: Int,
        val isScrollUp: Boolean,
    ) : ChatCommand

    data class RegisterEvent(val topicName: String) : ChatCommand

    data class GetEventsFromEventQueue(
        val queueId: String,
        val ownUserId: Int,
    ) : ChatCommand

    data class SendMessage(
        val streamId: Int,
        val topicName: String,
        val message: String,
    ) : ChatCommand

    data class AddEmojiInMessage(
        val ownUserId: Int,
        val messageId: Int,
        val emojiCode: String,
        val emojiName: String,
    ) : ChatCommand

    data class RemoveEmojiFromMessage(
        val ownUserId: Int,
        val messageId: Int,
        val emojiCode: String,
        val emojiName: String,
    ) : ChatCommand
}
