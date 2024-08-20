package pronin.oleg.zulip.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pronin.oleg.zulip.app.Const.MAX_COUNT_MESSAGES_IN_DB
import pronin.oleg.zulip.data.database.AppDatabase
import pronin.oleg.zulip.data.network.api.ZulipChatApi
import pronin.oleg.zulip.data.requests.NarrowRequest
import pronin.oleg.zulip.domain.models.messages.MessageDomain
import pronin.oleg.zulip.mappers.toDBO
import pronin.oleg.zulip.mappers.toDomain
import pronin.oleg.zulip.presentation.states.RequestResultState
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val api: ZulipChatApi,
    private val database: AppDatabase,
    private val json: Json,
) {
    suspend fun getTopicMessages(
        streamId: Int,
        topicName: String,
        lastMessageId: Int,
        numAfter: Int,
        numBefore: Int,
    ): Flow<RequestResultState<List<MessageDomain>>> = flow {
        database.chatDao().getMessages(lastMessageId = lastMessageId, limit = 20)
            .sortedBy { it.message.dateTime }
            .map { it.toDomain() }
            .apply { emit(RequestResultState.InProgress(this)) }

        api.getTopicMessages(
            lastMessageId = lastMessageId,
            numAfter = numAfter,
            numBefore = numBefore,
            narrow = "[${getNArrow(topicName, streamId)}]"
        )
            .messages
            .map { it.toDomain() }
            .sortedBy { it.dateTime }
            .apply {
                if (isNotEmpty())
                    updateMessageCache(this)

                emit(RequestResultState.Success(this))
            }
    }

    private fun getNArrow(topicName: String, streamId: Int) = json.encodeToString(
        NarrowRequest(
            negated = false,
            operator = "topic",
            operand = topicName
        )
    ) + "," + json.encodeToString(
        NarrowRequest(
            negated = false,
            operator = "stream",
            operand = streamId
        )
    )

    private fun updateMessageCache(message: List<MessageDomain>) {
        database.chatDao().apply {
            var countMessages = getCountMessage()

            if (countMessages < MAX_COUNT_MESSAGES_IN_DB) {
                message
                    .map { it.toDBO() }
                    .onEach {
                        addMessageWithReaction(it.message, it.reactions)

                        if (countMessages++ == MAX_COUNT_MESSAGES_IN_DB)
                            return
                    }
            }
        }
    }

    suspend fun sendMessage(
        streamId: Int,
        topicName: String,
        content: String,
    ): Int = api.sendMessage(
        type = "stream",
        to = streamId,
        topic = topicName,
        content = content
    ).id

    suspend fun addEmojiInMessage(messageId: Int, emojiName: String) =
        api.addEmojiInMessage(messageId, emojiName).result == "success"

    suspend fun deleteEmojiInMessage(messageId: Int, emojiName: String) =
        api.deleteEmojiInMessage(messageId, emojiName).result == "success"
}
