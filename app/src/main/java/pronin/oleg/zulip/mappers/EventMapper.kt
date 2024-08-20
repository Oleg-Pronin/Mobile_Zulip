package pronin.oleg.zulip.mappers

import pronin.oleg.zulip.data.network.models.events.Event
import pronin.oleg.zulip.data.network.models.responses.ResponseRegisterEvent
import pronin.oleg.zulip.data.network.models.events.UnreadStream
import pronin.oleg.zulip.domain.enums.EmojiAction
import pronin.oleg.zulip.domain.enums.EventType
import pronin.oleg.zulip.domain.models.events.EventDomain
import pronin.oleg.zulip.domain.models.events.ReactionEventDomain
import pronin.oleg.zulip.domain.models.events.RegisterEventDomain
import pronin.oleg.zulip.domain.models.events.UnreadStreamDomain
import pronin.oleg.zulip.utils.getEmojiByUnicode
import pronin.oleg.zulip.utils.toLocalDateTime

fun ResponseRegisterEvent.toDomain() = RegisterEventDomain(
    queueId = queueId,
    lastEventId = lastEventId ?: -1,
    serverDateTime = serverTimestamp?.toLong()?.toLocalDateTime(),
    unreadStreamMessages = unreadMessages?.streams?.map { it.toDomain() },
    presences = presences?.mapValues { it.value.toDomain() }
)

fun UnreadStream.toDomain() = UnreadStreamDomain(
    streamId, topic, unreadMessageIds
)

fun Event.toDomain(): EventDomain {
    val eventType = EventType.byCode(type)

    return EventDomain(
        id = id,
        type = eventType,
        message = if (eventType == EventType.MESSAGE) message?.toDomain() else null,
        reaction = if (eventType == EventType.REACTION)
            ReactionEventDomain(
                action = EmojiAction.byCode(action),
                userId = userId ?: 0,
                messageId = messageId ?: 0,
                emojiName = emojiName ?: "",
                emojiCode = getEmojiByUnicode(emojiCode ?: "")
            )
        else
            null,
        presence = null
    )
}
