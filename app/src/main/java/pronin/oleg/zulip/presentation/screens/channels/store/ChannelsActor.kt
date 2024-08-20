package pronin.oleg.zulip.presentation.screens.channels.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pronin.oleg.zulip.domain.use_cases.streams.GetStreamsUseCase
import pronin.oleg.zulip.domain.use_cases.topics.GetTopicByStreamIdUseCase
import pronin.oleg.zulip.mappers.toUI
import pronin.oleg.zulip.presentation.states.RequestResultState
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ChannelsActor @Inject constructor(
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getTopicByStreamIdUseCase: GetTopicByStreamIdUseCase,
) : Actor<ChannelsCommand, ChannelsEvent>() {
    override fun execute(command: ChannelsCommand): Flow<ChannelsEvent> = flow {
        when (command) {

            is ChannelsCommand.LoadStreams -> getStreamsUseCase.invoke(command.isSubscribed)
                .mapEvents(
                    eventMapper = { requestResultState ->
                        when (requestResultState) {
                            is RequestResultState.InProgress ->
                                ChannelsEvent.Internal.ChannelsLoaded(
                                    requestResultState.data.map { it.toUI(command.isSubscribed) }
                                )

                            is RequestResultState.Success ->
                                ChannelsEvent.Internal.ChannelsLoaded(
                                    requestResultState.data.map { it.toUI(command.isSubscribed) }
                                )

                            is RequestResultState.Error ->
                                ChannelsEvent.Internal.Error(requestResultState.throwable)
                        }
                    },
                    errorMapper = { ChannelsEvent.Internal.Error(it) }
                ).collect {
                    emit(it)
                }

            is ChannelsCommand.LoadTopic -> getTopicByStreamIdUseCase.invoke(command.streamId)
                .mapEvents(
                    eventMapper = { requestResultState ->
                        when (requestResultState) {
                            is RequestResultState.InProgress ->
                                ChannelsEvent.Internal.TopicsLoaded(
                                    command.streamId,
                                    requestResultState.data.map {
                                        it.toUI(
                                            command.streamId,
                                            command.streamColor
                                        )
                                    }
                                )

                            is RequestResultState.Success ->
                                ChannelsEvent.Internal.TopicsLoaded(
                                    command.streamId,
                                    requestResultState.data.map {
                                        it.toUI(
                                            command.streamId,
                                            command.streamColor
                                        )
                                    }
                                )

                            is RequestResultState.Error ->
                                ChannelsEvent.Internal.Error(requestResultState.throwable)
                        }
                    },
                    errorMapper = { ChannelsEvent.Internal.Error(it) }
                ).collect {
                    emit(it)
                }
        }
    }
}
