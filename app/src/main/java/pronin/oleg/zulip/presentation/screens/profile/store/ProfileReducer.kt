package pronin.oleg.zulip.presentation.screens.profile.store

import pronin.oleg.zulip.presentation.states.ScreenState
import pronin.oleg.zulip.presentation.states.State
import pronin.oleg.zulip.presentation.ui.UserUI
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

class ProfileReducer : ScreenDslReducer<
        ProfileEvent,
        ProfileEvent.UI,
        ProfileEvent.Internal,
        State<UserUI>,
        ProfileEffect,
        ProfileCommand
        >(ProfileEvent.UI::class, ProfileEvent.Internal::class) {

    override fun Result.internal(event: ProfileEvent.Internal) = when (event) {
        is ProfileEvent.Internal.ProfileLoaded -> state {
            copy(screenState = ScreenState.Content(event.userUI))
        }

        is ProfileEvent.Internal.Error -> effects {
            +ProfileEffect.ShowError(event.throwable)
        }
    }

    override fun Result.ui(event: ProfileEvent.UI) = when (event) {
        is ProfileEvent.UI.Init -> commands {
            +ProfileCommand.LoadProfile(event.user)
        }
    }
}
