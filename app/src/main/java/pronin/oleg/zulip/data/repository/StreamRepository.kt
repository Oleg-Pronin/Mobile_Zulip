package pronin.oleg.zulip.data.repository

import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pronin.oleg.zulip.data.database.AppDatabase
import pronin.oleg.zulip.data.network.api.ZulipChatApi
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.domain.models.streams.TopicDomain
import pronin.oleg.zulip.domain.use_cases.ConstEventType
import pronin.oleg.zulip.mappers.toDBO
import pronin.oleg.zulip.mappers.toDomain
import pronin.oleg.zulip.presentation.states.RequestResultState
import pronin.oleg.zulip.utils.asyncAwait
import javax.inject.Inject

class StreamRepository @Inject constructor(
    private val api: ZulipChatApi,
    private val database: AppDatabase,
    private val json: Json
) {
    suspend fun getSubscribedStreams() = flow {
        database.streamDao().getAllSubscribedStreams()
            .sortedByDescending { it.weeklyTraffic }
            .map { it.toDomain() }
            .apply { emit(RequestResultState.InProgress(this)) }

        api.getSubscribedStreams().subscriptions
            .sortedByDescending { it.weeklyTraffic }
            .map { it.toDomain() }
            .apply {
                if (isNotEmpty())
                    updateStreamCache(this)

                emit(RequestResultState.Success(this))
            }
    }

    suspend fun getAllStreams() = flow {
        database.streamDao().getAllStreams()
            .sortedByDescending { it.weeklyTraffic }
            .map { it.toDomain() }
            .apply { emit(RequestResultState.InProgress(this)) }

        api.getAllStreams().streams
            .sortedByDescending { it.weeklyTraffic }
            .map { it.toDomain() }
            .apply {
                if (isNotEmpty())
                    updateStreamCache(this)

                emit(RequestResultState.Success(this))
            }
    }

    private fun updateStreamCache(listStreams: List<StreamDomain>) {
        database.streamDao().apply {
            removeAllStreams()

            listStreams
                .map { it.toDBO() }
                .apply { insertStream(this) }
        }
    }

    suspend fun getTopics(streamId: Int) = flow {
        database.topicDao().getAllTopics(streamId)
            .map { it.toDomain() }
            .apply { emit(RequestResultState.Success(this)) }

        asyncAwait(
            { api.getTopics(streamId).topics.map { it.toDomain() } },
            { api.registerAnEventQueue(json.encodeToString(ConstEventType.UNREAD_MESSAGES)).toDomain() }
        ) { topics, event ->
            topics
                .map { topic ->
                    val unreadCount = event
                        .unreadStreamMessages
                        ?.firstOrNull { it.topic == topic.name }
                        ?.unreadMessageIds
                        ?.count() ?: 0

                    topic.copy(unreadCount = unreadCount)
                }.apply {
                    if (isNotEmpty())
                        updateTopicCache(streamId, this)

                    emit(RequestResultState.Success(this))
                }
        }
    }

    private fun updateTopicCache(
        streamId: Int,
        listTopics: List<TopicDomain>,
    ) {
        database.topicDao().apply {
            removeAllTopic(streamId)

            listTopics
                .map { it.toDBO(streamId) }
                .apply { insertTopics(this) }
        }
    }

    suspend fun subscribeChannel(
        subscriptions: Array<Map<String, String>>,
        inviteOnly: Boolean,
        historyPublicToSubscribers: Boolean,
    ) = api
        .subscribeChannel(json.encodeToString(subscriptions), inviteOnly, historyPublicToSubscribers)
        .toDomain()
}
