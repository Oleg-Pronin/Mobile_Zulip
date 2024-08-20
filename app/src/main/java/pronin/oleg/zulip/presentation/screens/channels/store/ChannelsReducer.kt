package pronin.oleg.zulip.presentation.screens.channels.store

import pronin.oleg.zulip.mappers.toDomain
import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.ui.ChannelUI
import vivid.money.elmslie.core.store.dsl.ResultBuilder
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

private typealias ChannelResultBuilder = ResultBuilder<ChannelsState, ChannelsEffect, ChannelsCommand>

class ChannelsReducer : ScreenDslReducer<
        ChannelsEvent,
        ChannelsEvent.UI,
        ChannelsEvent.Internal,
        ChannelsState,
        ChannelsEffect,
        ChannelsCommand
        >(ChannelsEvent.UI::class, ChannelsEvent.Internal::class) {

    override fun Result.internal(event: ChannelsEvent.Internal) = when (event) {
        is ChannelsEvent.Internal.ChannelsLoaded -> state {
            if (event.channelsUI.isEmpty())
                copy(screenState = ScreenState.Loading)
            else
                copy(
                    items = event.channelsUI,
                    screenState = ScreenState.Content(event.channelsUI)
                )
        }

        is ChannelsEvent.Internal.TopicsLoaded ->
            if (event.topicsUI.isEmpty()) state {
                val content = items.map {
                    if (it.id == event.streamId && it is ChannelUI.StreamUI) {
                        it.copy(isLoading = true)
                    } else
                        it
                }

                copy(
                    items = content,
                    screenState = ScreenState.Content(content)
                )
            } else {
                state {
                    val content = items.map {
                        if (it.id == event.streamId && it is ChannelUI.StreamUI) {
                            it.copy(
                                isOpened = true,
                                isLoading = false
                            )
                        } else
                            it
                    }

                    copy(
                        items = content,
                        screenState = ScreenState.Content(content)
                    )
                }

                updateTopicContent(event)
            }

        is ChannelsEvent.Internal.Error -> effects {
            +ChannelsEffect.ShowError(event.throwable)
        }
    }

    override fun Result.ui(event: ChannelsEvent.UI) = when (event) {
        ChannelsEvent.UI.Init -> commands {
            +ChannelsCommand.LoadStreams(isSubscribed = true)
        }

        is ChannelsEvent.UI.ChangeTab -> {
            state {
                copy(isSubscribedTab = event.isSubscribedTab)
            }

            commands {
                +ChannelsCommand.LoadStreams(isSubscribed = event.isSubscribedTab)
            }
        }

        ChannelsEvent.UI.ReloadStream -> commands {
            +ChannelsCommand.LoadStreams(isSubscribed = state.isSubscribedTab)
        }

        is ChannelsEvent.UI.LoadTopics -> {
            state {
                val content = items.map {
                    if (it.id == event.streamId && it is ChannelUI.StreamUI) {
                        it.copy(isLoading = true)
                    } else
                        it
                }

                copy(
                    items = content,
                    screenState = ScreenState.Content(content)
                )
            }

            commands {
                +ChannelsCommand.LoadTopic(
                    streamId = event.streamId,
                    streamColor = event.streamColor
                )
            }
        }

        is ChannelsEvent.UI.HideTopics -> state {
            var newContent = items.map {
                if (it.id == event.streamId && it is ChannelUI.StreamUI) {
                    it.copy(isOpened = false)
                } else
                    it
            }

            val topics = items.filter {
                it is ChannelUI.TopicUI && it.parentId == event.streamId
            }

            if (topics.isNotEmpty())
                newContent = newContent.toMutableList().apply {
                    removeAll(topics)
                }

            copy(
                items = newContent,
                screenState = ScreenState.Content(newContent)
            )
        }

        is ChannelsEvent.UI.GoToChat -> effects {
            val stream = state.items.find {
                it.id == event.streamId && it is ChannelUI.StreamUI
            } as ChannelUI.StreamUI

            +ChannelsEffect.GoToChat(stream.toDomain(), event.topicName, event.lastMessageId)
        }

        is ChannelsEvent.UI.Search -> state {
            copy(
                screenState = ScreenState.Content(
                    this.items.filter {
                        it.name.contains(
                            event.searchQuery,
                            true
                        )
                    }
                )
            )
        }
    }

    private fun ChannelResultBuilder.updateTopicContent(
        event: ChannelsEvent.Internal.TopicsLoaded,
    ) = state {
        val content = mutableListOf<ChannelUI>()
        val topics = event.topicsUI.toMutableList()

        items.forEach { channelUI ->
            if (channelUI is ChannelUI.TopicUI && channelUI.parentId == event.streamId) {
                val topic = topics.find { it.id == channelUI.id }

                if (topic != null) {
                    content.add(topic)

                    topics.remove(topic)
                }
            } else
                content.add(channelUI)
        }

        if (topics.isNotEmpty()) {
            val parentIndex = items.indexOfLast { ui ->
                ui.id == event.streamId && ui is ChannelUI.StreamUI
            }

            content.addAll(parentIndex + 1, topics)
        }

        copy(
            items = content,
            screenState = ScreenState.Content(content)
        )
    }
}
