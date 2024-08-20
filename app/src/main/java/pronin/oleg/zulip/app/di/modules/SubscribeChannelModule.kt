package pronin.oleg.zulip.app.di.modules

import dagger.Binds
import dagger.Module
import pronin.oleg.zulip.data.use_cases.channels.SubscribeChannelUseCaseImp
import pronin.oleg.zulip.domain.use_cases.streams.SubscribeChannelUseCase

@Module
interface SubscribeChannelModule {
    @Binds
    fun bindSubscribeChannelUseCase(createChannelUseCase: SubscribeChannelUseCaseImp): SubscribeChannelUseCase
}
