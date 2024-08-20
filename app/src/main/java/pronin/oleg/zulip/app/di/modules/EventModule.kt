package pronin.oleg.zulip.app.di.modules

import dagger.Binds
import dagger.Module
import pronin.oleg.zulip.data.use_cases.events.GetEventsUseCaseImpl
import pronin.oleg.zulip.data.use_cases.events.RegisterEventUseCaseImpl
import pronin.oleg.zulip.domain.use_cases.events.GetEventsUseCase
import pronin.oleg.zulip.domain.use_cases.events.RegisterEventUseCase

@Module
interface EventModule {
    @Binds
    fun bindGetEventsUseCase(getEventsUseCaseImpl: GetEventsUseCaseImpl): GetEventsUseCase

    @Binds
    fun bindRegisterEventUseCase(registerEventUseCaseImpl: RegisterEventUseCaseImpl): RegisterEventUseCase
}
