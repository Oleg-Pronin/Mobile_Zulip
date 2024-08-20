package pronin.oleg.zulip.domain.models.streams

data class SubscribeDomain(
    val subscribed: Map<String, List<String>>,
    val alreadySubscribed: Map<String, List<String>>,
)
