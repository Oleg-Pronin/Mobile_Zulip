package pronin.oleg.zulip.presentation.screens.chat.store

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import pronin.oleg.zulip.domain.enums.EmojiAction
import pronin.oleg.zulip.presentation.screens.chat.data.ChatReducerTestData
import pronin.oleg.zulip.presentation.states.ScreenState
import java.util.UUID

class ChatReducerTest : BehaviorSpec({
    ChatReducerTestData().apply {
        Given("ChatReducer") {
            val reducer = ChatReducer()

            When("Reduce") {
                And("UI event is Init") {
                    actual = reducer.reduce(
                        event = ChatEvent.UI.Init,
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.LoadOwnUser

                    Then("should initialize state and return the result of the command ChatCommand.$externalCommand") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("UI event is ChangeMessage") {
                    actual = reducer.reduce(
                        event = ChatEvent.UI.ChangeMessage(message),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    Then("should return state with the 'message' field modified") {
                        actual.state.message shouldBe message
                    }
                }

                And("Internal event is OwnUserLoaded") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.OwnUserLoaded(ownUser),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.LoadData(
                        streamId, topicName, lastMessageId, ownUser.id
                    )

                    Then("should return from state with modified 'ownUser' field and command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.state.ownUser shouldBe ownUser
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is CacheDataLoaded") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.CacheDataLoaded(messagesUI),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    Then("should return state with 'items' and 'screenState' fields changed") {
                        actual.state.items shouldBe messagesUI
                        actual.state.screenState shouldBe ScreenState.Content(messagesUI)
                    }
                }

                And("Internal event is DataLoaded") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.DataLoaded(messagesUI),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.RegisterEvent(topicName)

                    Then("should return state with 'items' and 'screenState' fields changed and command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.state.items shouldBe messagesUI
                        actual.state.screenState shouldBe ScreenState.Content(messagesUI)
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is MoreDataLoaded") {
                    And("event isScrollUp == true") {
                        actual = reducer.reduce(
                            event = ChatEvent.Internal.MoreDataLoaded(
                                isScrollUp = true,
                                externalMoreMessagesUI
                            ),
                            state = getChatStateWith(messagesUI = messagesUI)
                        )

                        val externalItems = externalMoreMessagesUI + messagesUI

                        Then("should return state with 'items' and 'screenState' fields changed") {
                            actual.state.items shouldBe externalItems
                            actual.state.screenState shouldBe ScreenState.Content(externalItems)
                        }
                    }

                    And("event isScrollUp == false") {
                        actual = reducer.reduce(
                            event = ChatEvent.Internal.MoreDataLoaded(
                                isScrollUp = false,
                                externalMoreMessagesUI
                            ),
                            state = getChatStateWith(messagesUI = messagesUI)
                        )

                        val externalItems = messagesUI + externalMoreMessagesUI

                        Then("should return state with 'items' and 'screenState' fields changed") {
                            actual.state.items shouldBe externalItems
                            actual.state.screenState shouldBe ScreenState.Content(externalItems)
                        }
                    }
                }

                And("Internal event is EventRegistered") {
                    val queueId = UUID.randomUUID().toString()

                    actual = reducer.reduce(
                        event = ChatEvent.Internal.EventRegistered(queueId),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.GetEventsFromEventQueue(queueId, ownUser.id)

                    Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is EventReceived") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.EventReceived,
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.RegisterEvent(topicName)

                    Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is ReceivedEventForMessage") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.ReceivedEventForMessage(newMessageUI),
                        state = getChatStateWith(messagesUI = messagesUI)
                    )

                    val externalEffect = ChatEffect.ScrollDown

                    Then("should return state with 'items' and 'screenState' fields changed and effects '${externalEffect}'") {
                        actual.state.items shouldBe messagesUIWithNewMessage
                        actual.state.screenState shouldBe ScreenState.Content(
                            messagesUIWithNewMessage
                        )
                        actual.effects shouldContain externalEffect
                    }
                }

                And("Internal event is ReceivedEventForReaction") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.ReceivedEventForReaction(
                            reaction = getReactionEvent(EmojiAction.ADD)
                        ),
                        state = getChatStateWith(messagesUI = messagesUI)
                    )

                    Then("should return state with 'items' and 'screenState' fields changed") {
                        actual.state.items shouldBe messagesUIWithReactions
                        actual.state.screenState shouldBe ScreenState.Content(
                            messagesUIWithReactions
                        )
                    }
                }

                And("UI event is SendMessage") {
                    actual = reducer.reduce(
                        event = ChatEvent.UI.SendMessage(streamId, topicName),
                        state = getChatStateWith(message = message)
                    )

                    externalCommand = ChatCommand.SendMessage(streamId, topicName, message)

                    Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is MessageSent") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.MessageSent(newMessageUI),
                        state = getChatStateWith(message = message, messagesUI = messagesUI)
                    )

                    val externalEffects = listOf(ChatEffect.CleanEditText, ChatEffect.ScrollDown)

                    Then("should return state with 'message', 'items', 'screenState' fields changed and effects") {
                        actual.state.items shouldBe messagesUIWithNewMessage
                        actual.state.screenState shouldBe ScreenState.Content(
                            messagesUIWithNewMessage
                        )
                        actual.effects shouldBe externalEffects
                    }
                }

                And("Internal event is Error") {
                    val error = Throwable("error")

                    actual = reducer.reduce(
                        event = ChatEvent.Internal.Error(error),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    val externalEffect = ChatEffect.ShowError(error)

                    Then("should return effect ChatEffect.${externalEffect.javaClass.simpleName}") {
                        actual.effects shouldContain externalEffect
                    }
                }

                And("UI event is AddEmoji") {
                    actual = reducer.reduce(
                        event = ChatEvent.UI.AddEmoji(messageId, emojiCode, emojiName),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.AddEmojiInMessage(
                        ownUserId = ownUser.id,
                        messageId = messageId,
                        emojiCode = emojiCode,
                        emojiName = emojiName
                    )

                    Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is EmojiAdded") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.EmojiAdded(
                            reaction = getReactionEvent(EmojiAction.ADD)
                        ),
                        state = getChatStateWith(messagesUI = messagesUI)
                    )

                    Then("should return state with 'items' and 'screenState' fields changed") {
                        actual.state.items shouldBe messagesUIWithReactions
                        actual.state.screenState shouldBe ScreenState.Content(
                            messagesUIWithReactions
                        )
                    }
                }

                And("UI event is RemoveEmoji") {
                    actual = reducer.reduce(
                        event = ChatEvent.UI.RemoveEmoji(messageId, emojiCode, emojiName),
                        state = getChatStateWith(message = "", messagesUI = emptyList())
                    )

                    externalCommand = ChatCommand.RemoveEmojiFromMessage(
                        ownUserId = ownUser.id,
                        messageId = messageId,
                        emojiCode = emojiCode,
                        emojiName = emojiName
                    )

                    Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                        actual.commands shouldContain externalCommand
                    }
                }

                And("Internal event is EmojiRemoved") {
                    actual = reducer.reduce(
                        event = ChatEvent.Internal.EmojiAdded(
                            reaction = getReactionEvent(EmojiAction.REMOVE)
                        ),
                        state = getChatStateWith(messagesUI = messagesUIWithReactions)
                    )

                    Then("should return state with 'items' and 'screenState' fields changed") {
                        actual.state.items shouldBe messagesUI
                        actual.state.screenState shouldBe ScreenState.Content(messagesUI)
                    }
                }

                And("UI event is ClickEmoji") {
                    And("event isSelected == true") {
                        actual = reducer.reduce(
                            event = ChatEvent.UI.ClickEmoji(
                                messageId = messageId,
                                isSelected = true,
                                emojiCode = emojiCode,
                                emojiName = emojiName
                            ),
                            state = getChatStateWith(message = "", messagesUI = emptyList())
                        )

                        externalCommand = ChatCommand.RemoveEmojiFromMessage(
                            ownUserId = ownUser.id,
                            messageId = messageId,
                            emojiCode = emojiCode,
                            emojiName = emojiName
                        )

                        Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                            actual.commands shouldContain externalCommand
                        }
                    }

                    And("event isSelected == false") {
                        actual = reducer.reduce(
                            event = ChatEvent.UI.ClickEmoji(
                                messageId = messageId,
                                isSelected = false,
                                emojiCode = emojiCode,
                                emojiName = emojiName
                            ),
                            state = getChatStateWith(message = "", messagesUI = emptyList())
                        )

                        externalCommand = ChatCommand.AddEmojiInMessage(
                            ownUserId = ownUser.id,
                            messageId = messageId,
                            emojiCode = emojiCode,
                            emojiName = emojiName
                        )

                        Then("should return the command 'ChatCommand.${externalCommand.javaClass.simpleName}'") {
                            actual.commands shouldContain externalCommand
                        }
                    }
                }
            }
        }
    }
})