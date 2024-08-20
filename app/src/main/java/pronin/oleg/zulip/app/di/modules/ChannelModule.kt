package pronin.oleg.zulip.app.di.modules

import dagger.Binds
import dagger.Module
import pronin.oleg.zulip.data.use_cases.channels.GetStreamsUseCaseImpl
import pronin.oleg.zulip.data.use_cases.channels.GetTopicByStreamIdUseCaseImpl
import pronin.oleg.zulip.domain.use_cases.streams.GetStreamsUseCase
import pronin.oleg.zulip.domain.use_cases.topics.GetTopicByStreamIdUseCase

@Module
interface ChannelModule {
    @Binds
    fun bindGetStreamsUseCase(getStreamsUseCaseImpl: GetStreamsUseCaseImpl): GetStreamsUseCase

    @Binds
    fun bindGetTopicByStreamIdUseCase(getTopicByStreamIdUseCaseImpl: GetTopicByStreamIdUseCaseImpl): GetTopicByStreamIdUseCase
}
