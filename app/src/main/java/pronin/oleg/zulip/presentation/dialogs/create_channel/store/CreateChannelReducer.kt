package pronin.oleg.zulip.presentation.dialogs.create_channel.store

import pronin.oleg.zulip.domain.enums.ChannelPermission
import pronin.oleg.zulip.presentation.states.CreateChannelScreenState
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class CreateChannelReducer : ScreenDslReducer<
        CreateChannelEvent,
        CreateChannelEvent.UI,
        CreateChannelEvent.Internal,
        CreateChannelState,
        CreateChannelEffect,
        CreateChannelCommand
        >(CreateChannelEvent.UI::class, CreateChannelEvent.Internal::class) {

    override fun Result.internal(event: CreateChannelEvent.Internal) = when (event) {
        CreateChannelEvent.Internal.CreatedChannel -> {
            state {
                copy(
                    isCreateButtonLocked = false,
                    screenState = CreateChannelScreenState.Init
                )
            }

            effects { +CreateChannelEffect.DismissDialog(true) }
        }

        CreateChannelEvent.Internal.ChannelAlreadyExists -> state {
            copy(
                isCreateButtonLocked = true,
                screenState = CreateChannelScreenState.ErrorInChannelName
            )
        }

        is CreateChannelEvent.Internal.Error -> {
            state {
                copy(
                    isCreateButtonLocked = false,
                    screenState = CreateChannelScreenState.Init
                )
            }

            effects {
                +CreateChannelEffect.ShowError(event.throwable)
            }
        }
    }

    override fun Result.ui(event: CreateChannelEvent.UI) = when (event) {
        is CreateChannelEvent.UI.ChangeChannelName -> {
            state {
                copy(
                    channelName = event.channelName,
                    isCreateButtonLocked = event.channelName.isEmpty()
                )
            }
        }

        is CreateChannelEvent.UI.ChangeChannelDescription -> state {
            copy(channelDescription = event.channelDescription)
        }

        is CreateChannelEvent.UI.ChangeChannelPermissions -> state {
            when (event.channelPermission) {
                ChannelPermission.PUBLIC ->
                    copy(
                        inviteOnly = false,
                        historyPublicToSubscribers = true
                    )

                ChannelPermission.PRIVATE_SHARED_HISTORY ->
                    copy(
                        inviteOnly = true,
                        historyPublicToSubscribers = true
                    )

                ChannelPermission.PRIVATE_PROTECTED_HISTORY ->
                    copy(
                        inviteOnly = true,
                        historyPublicToSubscribers = false
                    )
            }
        }

        CreateChannelEvent.UI.CreateChannel -> {
            state {
                copy(
                    isCreateButtonLocked = true,
                    screenState = CreateChannelScreenState.ProcessOfCreation
                )
            }

            commands {
                +CreateChannelCommand.CreateChannel(
                    state.channelName,
                    state.channelDescription,
                    state.inviteOnly,
                    state.historyPublicToSubscribers
                )
            }
        }
    }
}
