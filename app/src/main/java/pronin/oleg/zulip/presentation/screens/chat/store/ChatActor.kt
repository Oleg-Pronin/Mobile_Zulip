package pronin.oleg.zulip.presentation.screens.chat.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.toKotlinLocalDateTime
import pronin.oleg.zulip.app.Const.BASE_COUNT_MESSAGES_IN_DB
import pronin.oleg.zulip.app.Const.COUNT_LOAD_MORE_MESSAGES_IN_DB
import pronin.oleg.zulip.domain.enums.EmojiAction
import pronin.oleg.zulip.domain.enums.EventType
import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.domain.use_cases.ConstEventType
import pronin.oleg.zulip.domain.use_cases.events.GetEventsUseCase
import pronin.oleg.zulip.domain.use_cases.events.RegisterEventUseCase
import pronin.oleg.zulip.domain.use_cases.messages.AddEmojiInMessageUseCase
import pronin.oleg.zulip.domain.use_cases.messages.RemoveEmojiFromMessageUseCase
import pronin.oleg.zulip.domain.use_cases.messages.GetMessagesUseCase
import pronin.oleg.zulip.domain.use_cases.messages.SendMessageUseCase
import pronin.oleg.zulip.domain.use_cases.profile.GetOwnUserUseCase
import pronin.oleg.zulip.mappers.toChatUI
import pronin.oleg.zulip.presentation.states.RequestResultState
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.utils.runCatchingNonCancellation
import vivid.money.elmslie.core.store.Actor
import java.time.LocalDateTime
import javax.inject.Inject

class ChatActor @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getOwnUserUseCase: GetOwnUserUseCase,
    private val registerEventUseCase: RegisterEventUseCase,
    private val getEventsUseCase: GetEventsUseCase,
    private val addEmojiInMessageUseCase: AddEmojiInMessageUseCase,
    private val removeEmojiFromMessageUseCase: RemoveEmojiFromMessageUseCase,
) : Actor<ChatCommand, ChatEvent>() {
    override fun execute(command: ChatCommand): Flow<ChatEvent> = flow {
        when (command) {
            ChatCommand.LoadOwnUser -> getOwnUserUseCase.invoke().apply {
                emit(ChatEvent.Internal.OwnUserLoaded(this))
            }

            is ChatCommand.LoadData -> getMessagesUseCase.invoke(
                command.streamId,
                command.topicName,
                command.lastMessageId,
                BASE_COUNT_MESSAGES_IN_DB,
                BASE_COUNT_MESSAGES_IN_DB
            ).mapEvents(
                eventMapper = { requestResult ->
                    when (requestResult) {
                        is RequestResultState.InProgress -> ChatEvent.Internal.CacheDataLoaded(
                            requestResult.data.toChatUI(
                                command.ownUserId
                            )
                        )

                        is RequestResultState.Success -> ChatEvent.Internal.DataLoaded(
                            requestResult.data.toChatUI(
                                command.ownUserId
                            )
                        )

                        is RequestResultState.Error ->
                            ChatEvent.Internal.Error(requestResult.throwable)
                    }
                }
            ).collect {
                emit(it)
            }

            is ChatCommand.LoadMore -> getMessagesUseCase.invoke(
                command.streamId,
                command.topicName,
                command.lastMessageId,
                if (command.isScrollUp) 0 else COUNT_LOAD_MORE_MESSAGES_IN_DB,
                if (command.isScrollUp) COUNT_LOAD_MORE_MESSAGES_IN_DB else 0,
            ).mapEvents(
                eventMapper = { requestResult ->
                    when (requestResult) {
                        is RequestResultState.InProgress -> ChatEvent.Internal.MoreDataLoaded(
                            command.isScrollUp,
                            requestResult.data.toChatUI(command.ownUserId)
                        )

                        is RequestResultState.Success -> ChatEvent.Internal.MoreDataLoaded(
                            command.isScrollUp,
                            requestResult.data.toChatUI(command.ownUserId)
                        )

                        is RequestResultState.Error ->
                            ChatEvent.Internal.Error(requestResult.throwable)
                    }
                }
            ).collect {
                emit(it)
            }

            is ChatCommand.SendMessage -> runCatchingNonCancellation {
                sendMessageUseCase.invoke(command.streamId, command.topicName, command.message)
            }.onSuccess { messageId ->
                val message = ChatUI.MessageUI(
                    id = messageId,
                    isOwnMessage = true,
                    dateTime = LocalDateTime.now().toKotlinLocalDateTime(),
                    senderAvatar = "",
                    senderFullName = "",
                    content = command.message,
                )

                emit(ChatEvent.Internal.MessageSent(message))
            }

            is ChatCommand.RegisterEvent -> runCatchingNonCancellation {
                registerEventUseCase.invoke(
                    eventTypes = ConstEventType.CHAT,
                    narrow = listOf(arrayOf("topic", command.topicName))
                )
            }.onSuccess { event ->
                emit(ChatEvent.Internal.EventRegistered(event.queueId))
            }

            is ChatCommand.GetEventsFromEventQueue -> runCatchingNonCancellation {
                getEventsUseCase.invoke(command.queueId)
            }.onSuccess { events ->
                events.forEach { event ->
                    when (event.type) {
                        EventType.MESSAGE -> if (event.message != null)
                            emit(
                                ChatEvent.Internal.ReceivedEventForMessage(
                                    ChatUI.createMessageUI(event.message, command.ownUserId)
                                )
                            )

                        EventType.REACTION -> if (event.reaction != null)
                            emit(ChatEvent.Internal.ReceivedEventForReaction(event.reaction))

                        EventType.UNKNOWN -> Unit
                    }
                }

                emit(ChatEvent.Internal.EventReceived)
            }

            is ChatCommand.AddEmojiInMessage -> flow {
                emit(addEmojiInMessageUseCase.invoke(command.messageId, command.emojiName))
            }.mapEvents(
                eventMapper = { result ->
                    if (result)
                        ChatEvent.Internal.EmojiAdded(
                            ReactionEventDomain(
                                action = EmojiAction.ADD,
                                userId = command.ownUserId,
                                messageId = command.messageId,
                                emojiName = command.emojiName,
                                emojiCode = command.emojiCode
                            )
                        )
                    else
                        ChatEvent.Internal.Error(Throwable("An error occurred while adding emoji"))
                }
            ).collect { it }

            is ChatCommand.RemoveEmojiFromMessage -> flow {
                emit(removeEmojiFromMessageUseCase.invoke(command.messageId, command.emojiName))
            }.mapEvents(
                eventMapper = { result ->
                    if (result)
                        ChatEvent.Internal.EmojiRemoved(
                            ReactionEventDomain(
                                action = EmojiAction.REMOVE,
                                userId = command.ownUserId,
                                messageId = command.messageId,
                                emojiName = command.emojiName,
                                emojiCode = command.emojiCode
                            )
                        )
                    else
                        ChatEvent.Internal.Error(Throwable("An error occurred while removing emoji"))
                }
            ).collect { it }
        }
    }.mapEvents(
        eventMapper = { it },
        errorMapper = { ChatEvent.Internal.Error(it) }
    )
}
