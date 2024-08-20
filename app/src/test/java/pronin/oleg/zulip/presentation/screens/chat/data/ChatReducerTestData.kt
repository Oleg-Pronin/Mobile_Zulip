package pronin.oleg.zulip.presentation.screens.chat.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import pronin.oleg.zulip.domain.enums.EmojiAction
import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.domain.models.messages.ReactionDomain
import pronin.oleg.zulip.domain.models.users.UserDomain
import pronin.oleg.zulip.presentation.screens.chat.store.ChatCommand
import pronin.oleg.zulip.presentation.screens.chat.store.ChatEffect
import pronin.oleg.zulip.presentation.screens.chat.store.ChatState
import pronin.oleg.zulip.presentation.ui.ChatUI
import vivid.money.elmslie.core.store.Result

class ChatReducerTestData {
    lateinit var actual: Result<ChatState, ChatEffect, ChatCommand>
    lateinit var externalCommand: ChatCommand

    val streamId = 0
    val topicName = "Topic test"
    val lastMessageId = 1

    private val fullNameUser = "Test Testov"
    val ownUser = UserDomain(1, fullNameUser, "test@test.ru")

    val messageId = 1
    val message = "Test message"

    val emojiCode = "1f92f"
    val emojiName = "exploding_head"

    private val date = LocalDate(2024, 5, 10)
    private val time = LocalTime(16, 48)
    private val dateTime = LocalDateTime(date, time)

    val newMessageUI = ChatUI.MessageUI(
        id = 2,
        isOwnMessage = false,
        dateTime = dateTime,
        senderAvatar = null,
        senderFullName = fullNameUser,
        content = message,
        reactions = emptyList()
    )

    val messagesUI = listOf(
        ChatUI.DateUI(
            id = 1,
            date = date
        ),
        ChatUI.MessageUI(
            id = messageId,
            isOwnMessage = false,
            dateTime = dateTime,
            senderAvatar = null,
            senderFullName = fullNameUser,
            content = message,
            reactions = emptyList()
        )
    )

    val messagesUIWithNewMessage = messagesUI + newMessageUI

    val messagesUIWithReactions = listOf(
        ChatUI.DateUI(
            id = 1,
            date = date
        ),
        ChatUI.MessageUI(
            id = messageId,
            isOwnMessage = false,
            dateTime = dateTime,
            senderAvatar = null,
            senderFullName = fullNameUser,
            content = message,
            reactions = listOf(
                ReactionDomain(
                    userId = ownUser.id,
                    emojiName = emojiName,
                    emojiCode = emojiCode,
                    isSelected = true
                )
            )
        )
    )

    val externalMoreMessagesUI = listOf(
        ChatUI.MessageUI(
            id = 3,
            isOwnMessage = false,
            dateTime = dateTime,
            senderAvatar = null,
            senderFullName = fullNameUser,
            content = message,
            reactions = emptyList()
        ),
        ChatUI.MessageUI(
            id = 4,
            isOwnMessage = false,
            dateTime = dateTime,
            senderAvatar = null,
            senderFullName = fullNameUser,
            content = message,
            reactions = emptyList()
        )
    )

    fun getChatStateWith(message: String = "", messagesUI: List<ChatUI> = emptyList()) =
        ChatState(
            streamId = streamId,
            topicName = topicName,
            lastMessageId = lastMessageId,
            ownUser = ownUser,
            message = message,
            items = messagesUI
        )

    fun getReactionEvent(action: EmojiAction) = ReactionEventDomain(
        action = action,
        userId = ownUser.id,
        messageId = messageId,
        emojiCode = emojiCode,
        emojiName = emojiName
    )
}