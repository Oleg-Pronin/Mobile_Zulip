package pronin.oleg.zulip.presentation.screens.profile.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.domain.use_cases.ConstEventType
import pronin.oleg.zulip.domain.use_cases.events.RegisterEventUseCase
import pronin.oleg.zulip.domain.use_cases.profile.GetProfileUseCase
import pronin.oleg.zulip.mappers.toUI
import pronin.oleg.zulip.utils.asyncAwait
import pronin.oleg.zulip.utils.runCatchingNonCancellation
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class ProfileActor @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val registerEventUseCase: RegisterEventUseCase,
) : Actor<ProfileCommand, ProfileEvent>() {
    override fun execute(command: ProfileCommand): Flow<ProfileEvent> = flow {
        when (command) {
            is ProfileCommand.LoadProfile -> runCatchingNonCancellation {
                asyncAwait(
                    { getProfileUseCase.invoke(command.user) },
                    { registerEventUseCase.invoke(ConstEventType.PRESENCE) },
                ) { profile, event ->
                    profile.toUI(
                        status = StatusPresence.getStatus(
                            event.serverDateTime,
                            event.presences?.get(profile.email)
                        )
                    )
                }
            }.onSuccess { emit(ProfileEvent.Internal.ProfileLoaded(it)) }
        }
    }.mapEvents(
        eventMapper = { it },
        errorMapper = { ProfileEvent.Internal.Error(it) }
    )
}
