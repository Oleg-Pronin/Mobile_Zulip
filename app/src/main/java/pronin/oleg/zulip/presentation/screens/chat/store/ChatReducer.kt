package pronin.oleg.zulip.presentation.screens.chat.store

import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChatUI
import vivid.money.elmslie.core.store.dsl.ResultBuilder
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

private typealias ChatResultBuilder = ResultBuilder<ChatState, ChatEffect, ChatCommand>

class ChatReducer : ScreenDslReducer<
        ChatEvent,
        ChatEvent.UI,
        ChatEvent.Internal,
        ChatState,
        ChatEffect,
        ChatCommand
        >(ChatEvent.UI::class, ChatEvent.Internal::class) {

    override fun Result.internal(event: ChatEvent.Internal) = when (event) {
        is ChatEvent.Internal.OwnUserLoaded -> {
            state {
                copy(ownUser = event.ownUser)
            }

            commands {
                +ChatCommand.LoadData(
                    streamId = state.streamId,
                    topicName = state.topicName,
                    lastMessageId = state.lastMessageId,
                    ownUserId = event.ownUser.id
                )
            }
        }

        is ChatEvent.Internal.CacheDataLoaded -> state {
            copy(
                items = event.messagesUI,
                screenState = ScreenState.Content(event.messagesUI)
            )
        }

        is ChatEvent.Internal.DataLoaded -> {
            state {
                copy(
                    items = event.messagesUI,
                    screenState = ScreenState.Content(event.messagesUI)
                )
            }

            commands { +ChatCommand.RegisterEvent(topicName = state.topicName) }
        }

        is ChatEvent.Internal.MoreDataLoaded -> onMoreDataLoaded(event.isScrollUp, event.messagesUI)

        is ChatEvent.Internal.EventRegistered -> commands {
            +ChatCommand.GetEventsFromEventQueue(
                queueId = event.queueId,
                ownUserId = state.ownUser.id
            )
        }

        ChatEvent.Internal.EventReceived -> commands {
            +ChatCommand.RegisterEvent(topicName = state.topicName)
        }

        is ChatEvent.Internal.ReceivedEventForMessage -> onEventForMessage(event.messageUI)

        is ChatEvent.Internal.ReceivedEventForReaction -> onEventForReaction(event.reaction)

        is ChatEvent.Internal.MessageSent -> {
            val messages = state.items + event.messages

            state {
                copy(
                    message = "",
                    items = messages,
                    screenState = ScreenState.Content(messages)
                )
            }

            effects {
                +ChatEffect.CleanEditText
                +ChatEffect.ScrollDown
            }
        }

        is ChatEvent.Internal.Error -> effects { +ChatEffect.ShowError(event.throwable) }
        is ChatEvent.Internal.EmojiAdded -> onEventForReaction(event.reaction)
        is ChatEvent.Internal.EmojiRemoved -> onEventForReaction(event.reaction)
    }

    override fun Result.ui(event: ChatEvent.UI) = when (event) {
        is ChatEvent.UI.Init -> {
            commands { +ChatCommand.LoadOwnUser }
        }

        is ChatEvent.UI.ChangeMessage -> state {
            copy(message = event.message)
        }

        is ChatEvent.UI.SendMessage -> commands {
            +ChatCommand.SendMessage(event.streamId, event.topicName, state.message)
        }

        ChatEvent.UI.AddImage -> Unit

        is ChatEvent.UI.ScrollItems -> commands {
            if (event.currentPosition == 5)
                +ChatCommand.LoadMore(
                    streamId = state.streamId,
                    topicName = state.topicName,
                    lastMessageId = state.items.first { it is ChatUI.MessageUI }.id,
                    ownUserId = state.ownUser.id,
                    isScrollUp = true
                )
            else if (state.items.size - event.currentPosition == 5)
                +ChatCommand.LoadMore(
                    streamId = state.streamId,
                    topicName = state.topicName,
                    lastMessageId = state.items.last { it is ChatUI.MessageUI }.id,
                    ownUserId = state.ownUser.id,
                    isScrollUp = false
                )
        }

        is ChatEvent.UI.ClickEmoji -> commands {
            if (event.isSelected)
                +ChatCommand.RemoveEmojiFromMessage(
                    ownUserId = state.ownUser.id,
                    messageId = event.messageId,
                    emojiCode = event.emojiCode,
                    emojiName = event.emojiName
                )
            else
                +ChatCommand.AddEmojiInMessage(
                    ownUserId = state.ownUser.id,
                    messageId = event.messageId,
                    emojiCode = event.emojiCode,
                    emojiName = event.emojiName
                )
        }

        is ChatEvent.UI.AddEmoji -> commands {
            +ChatCommand.AddEmojiInMessage(
                ownUserId = state.ownUser.id,
                messageId = event.messageId,
                emojiCode = event.emojiCode,
                emojiName = event.emojiName
            )
        }

        is ChatEvent.UI.RemoveEmoji -> commands {
            +ChatCommand.RemoveEmojiFromMessage(
                ownUserId = state.ownUser.id,
                messageId = event.messageId,
                emojiCode = event.emojiCode,
                emojiName = event.emojiName
            )
        }
    }

    private fun ChatResultBuilder.onEventForMessage(
        messageUI: ChatUI.MessageUI,
    ) {
        if (state.items.find { it.id == messageUI.id } == null) {
            val content = state.items.toMutableList() + messageUI

            state {
                copy(
                    items = content,
                    screenState = ScreenState.Content(content)
                )
            }
        }

        effects {
            +ChatEffect.ScrollDown
        }
    }

    private fun ChatResultBuilder.onEventForReaction(
        reaction: ReactionEventDomain,
    ) {
        val newItems = state.items.map {
            when (it) {
                is ChatUI.MessageUI -> if (it.id == reaction.messageId)
                    it.changeReaction(reaction, reaction.userId == state.ownUser.id)
                else
                    it

                else -> it
            }
        }

        state {
            copy(
                items = newItems,
                screenState = ScreenState.Content(newItems)
            )
        }
    }

    private fun ChatResultBuilder.onMoreDataLoaded(
        isScrollUp: Boolean,
        messagesUI: List<ChatUI>,
    ) {
        val newItems = (
                if (isScrollUp)
                    messagesUI + state.items
                else
                    state.items + messagesUI
                ).distinct()

        state {
            copy(
                items = newItems,
                screenState = ScreenState.Content(newItems)
            )
        }
    }
}
