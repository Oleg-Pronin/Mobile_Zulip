package pronin.oleg.zulip.domain.use_cases.streams

import pronin.oleg.zulip.domain.models.streams.SubscribeDomain

interface SubscribeChannelUseCase {
    suspend operator fun invoke(
        subscriptions: Array<Map<String, String>>,
        inviteOnly: Boolean,
        historyPublicToSubscribers: Boolean
    ): SubscribeDomain
}
