package pronin.oleg.zulip.data.repository

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pronin.oleg.zulip.data.network.api.ZulipChatApi
import pronin.oleg.zulip.domain.models.events.EventDomain
import pronin.oleg.zulip.domain.models.events.RegisterEventDomain
import pronin.oleg.zulip.mappers.toDomain
import javax.inject.Inject

class RealTimeRepository @Inject constructor(
    private val api: ZulipChatApi,
    private val json: Json
) {
    suspend fun registerEventForTopic(
        eventTypes: List<String>,
        narrow: List<Array<String>>? = null
    ): RegisterEventDomain = api.registerAnEventQueue(
        json.encodeToString(eventTypes),
        narrow?.let { json.encodeToString(it) }
    ).toDomain()

    suspend fun getEvents(
        queueId: String,
    ): List<EventDomain> = api.getEvents(queueId).events.map { it.toDomain() }
}
