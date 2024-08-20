package pronin.oleg.zulip.presentation.screens.people.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.domain.use_cases.ConstEventType
import pronin.oleg.zulip.domain.use_cases.events.RegisterEventUseCase
import pronin.oleg.zulip.domain.use_cases.people.GetAllPeopleUseCase
import pronin.oleg.zulip.mappers.toUI
import pronin.oleg.zulip.utils.asyncAwait
import pronin.oleg.zulip.utils.runCatchingNonCancellation
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

class PeopleActor @Inject constructor(
    private val getAllPeopleUseCase: GetAllPeopleUseCase,
    private val registerEventUseCase: RegisterEventUseCase,
) : Actor<PeopleCommand, PeopleEvent>() {
    override fun execute(command: PeopleCommand): Flow<PeopleEvent> = flow {
        when (command) {
            PeopleCommand.LoadPeople -> runCatchingNonCancellation {
                asyncAwait(
                    { getAllPeopleUseCase.invoke() },
                    { registerEventUseCase.invoke(ConstEventType.PRESENCE) },
                ) { users, event ->
                    val serverDateTime = event.serverDateTime

                    users
                        .map { user ->
                            val presence = event.presences?.get(user.email)

                            user.toUI(
                                status = StatusPresence.getStatus(serverDateTime, presence)
                            )
                        }
                        .sortedBy { it.status }
                }
            }.onSuccess { emit(PeopleEvent.Internal.PeopleLoaded(it)) }
        }
    }.mapEvents(
        eventMapper = { it },
        errorMapper = { PeopleEvent.Internal.Error(it) }
    )
}
