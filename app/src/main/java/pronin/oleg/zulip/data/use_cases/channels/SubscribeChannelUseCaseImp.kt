package pronin.oleg.zulip.data.use_cases.channels

import pronin.oleg.zulip.app.di.annotations.CreateChatScope
import pronin.oleg.zulip.data.repository.StreamRepository
import pronin.oleg.zulip.domain.use_cases.streams.SubscribeChannelUseCase
import javax.inject.Inject

@CreateChatScope
class SubscribeChannelUseCaseImp @Inject constructor(
    private val streamRepository: StreamRepository,
) : SubscribeChannelUseCase {
    override suspend fun invoke(
        subscriptions: Array<Map<String, String>>,
        inviteOnly: Boolean,
        historyPublicToSubscribers: Boolean,
    ) = streamRepository.subscribeChannel(subscriptions, inviteOnly, historyPublicToSubscribers)
}
