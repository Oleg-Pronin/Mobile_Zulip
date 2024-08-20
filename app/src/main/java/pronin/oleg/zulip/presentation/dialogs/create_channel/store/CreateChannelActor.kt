package pronin.oleg.zulip.presentation.dialogs.create_channel.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pronin.oleg.zulip.domain.use_cases.streams.SubscribeChannelUseCase
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class CreateChannelActor @Inject constructor(
    private val subscribeChannelUseCase: SubscribeChannelUseCase,
) : Actor<CreateChannelCommand, CreateChannelEvent>() {
    override fun execute(command: CreateChannelCommand): Flow<CreateChannelEvent> = flow {
        when (command) {
            is CreateChannelCommand.CreateChannel -> {
                val subscribe = mapOf(
                    "name" to command.name,
                    "description" to command.description
                )

                subscribeChannelUseCase.invoke(
                    subscriptions = arrayOf(subscribe),
                    inviteOnly = command.inviteOnly,
                    historyPublicToSubscribers = command.historyPublicToSubscribers
                ).apply {
                    if (this.subscribed.isNotEmpty())
                        emit(CreateChannelEvent.Internal.CreatedChannel)
                    else if (this.alreadySubscribed.isNotEmpty())
                        emit(CreateChannelEvent.Internal.ChannelAlreadyExists)
                }
            }
        }
    }.mapEvents(
        eventMapper = { it },
        errorMapper = { CreateChannelEvent.Internal.Error(it) }
    )
}
