package pronin.oleg.zulip.domain.models.events

import pronin.oleg.zulip.domain.enums.EventType
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.domain.models.users.PresenceDomain

data class EventDomain(
    val id: Int,
    val type: EventType,
    val message: MessageDomain?,
    val reaction: ReactionEventDomain?,
    val presence: PresenceDomain?,
)
