package pronin.oleg.zulip.presentation.screens.chat.store

import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.presentation.ui.ChatUI

sealed interface ChatEvent {
    sealed interface UI : ChatEvent {
        data object Init : UI
        data object AddImage : UI

        data class ChangeMessage(val message: String) : UI
        data class ScrollItems(val currentPosition: Int) : UI
        data class SendMessage(val streamId: Int, val topicName: String) : UI

        data class AddEmoji(
            val messageId: Int,
            val emojiCode: String,
            val emojiName: String
        ) : UI

        data class RemoveEmoji(
            val messageId: Int,
            val emojiCode: String,
            val emojiName: String
        ) : UI

        data class ClickEmoji(
            val messageId: Int,
            val isSelected: Boolean,
            val emojiCode: String,
            val emojiName: String
        ) : UI
    }

    sealed interface Internal : ChatEvent {
        data object EventReceived : Internal

        data class OwnUserLoaded(val ownUser: UserDomain) : Internal
        data class CacheDataLoaded(val messagesUI: List<ChatUI>) : Internal
        data class DataLoaded(val messagesUI: List<ChatUI>) : Internal
        data class MoreDataLoaded(val isScrollUp: Boolean, val messagesUI: List<ChatUI>) : Internal
        data class MessageSent(val messages: ChatUI) : Internal

        data class EmojiAdded(val reaction: ReactionEventDomain) : Internal
        data class EmojiRemoved(val reaction: ReactionEventDomain) : Internal

        data class EventRegistered(val queueId: String) : Internal
        data class ReceivedEventForMessage(val messageUI: ChatUI.MessageUI) : Internal
        data class ReceivedEventForReaction(val reaction: ReactionEventDomain) : Internal

        data class Error(val throwable: Throwable) : Internal
    }
}
