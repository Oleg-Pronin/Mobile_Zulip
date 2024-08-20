package pronin.oleg.zulip.data.network.api

import pronin.oleg.zulip.data.network.models.responses.ResponseMessage
import pronin.oleg.zulip.data.network.models.responses.ResponseQueueEvent
import pronin.oleg.zulip.data.network.models.responses.ResponseStreams
import pronin.oleg.zulip.data.network.models.responses.ResponseUser
import pronin.oleg.zulip.data.network.models.users.User
import pronin.oleg.zulip.data.network.models.responses.ResponseRegisterEvent
import pronin.oleg.zulip.data.network.models.responses.ResponseMessages
import pronin.oleg.zulip.data.network.models.responses.ResponseResult
import pronin.oleg.zulip.data.network.models.responses.ResponseSubscribe
import pronin.oleg.zulip.data.network.models.responses.ResponseSubscriptions
import pronin.oleg.zulip.data.network.models.responses.ResponseTopics
import pronin.oleg.zulip.data.network.models.responses.ResponseUsers
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ZulipChatApi {
    @GET("streams")
    suspend fun getAllStreams(
        @Query("include_public")
        includePublic: Boolean = true,

        @Query("include_subscribed")
        includeSubscribed: Boolean = true,
    ): ResponseStreams

    @GET("users/me/subscriptions")
    suspend fun getSubscribedStreams(): ResponseSubscriptions

    @FormUrlEncoded
    @POST("users/me/subscriptions")
    suspend fun subscribeChannel(
        @Field("subscriptions") subscriptions: String,
        @Field("invite_only") inviteOnly: Boolean,
        @Field("history_public_to_subscribers") historyPublicToSubscribers: Boolean,
    ): ResponseSubscribe

    @GET("users")
    suspend fun getAllUsers(): ResponseUsers

    @GET("users/me")
    suspend fun getOwnUser(): User

    @GET("users/{user_id}")
    suspend fun getUserById(
        @Path("user_id") userId: Int,
    ): ResponseUser

    @GET("users/me/{stream_id}/topics")
    suspend fun getTopics(
        @Path("stream_id") streamId: Int,
    ): ResponseTopics

    @FormUrlEncoded
    @POST("register")
    suspend fun registerAnEventQueue(
        @Field("event_types") body: String,
        @Field("narrow") narrow: String? = null,
    ): ResponseRegisterEvent

    @GET("events")
    suspend fun getEvents(
        @Query("queue_id") queueId: String,
        @Query("last_event_id") lastEventId: Int = -1,
    ): ResponseQueueEvent

    @GET("messages")
    suspend fun getTopicMessages(
        @Query("anchor")
        lastMessageId: Int,

        @Query("num_after")
        numAfter: Int = 0,

        @Query("num_before")
        numBefore: Int = 0,

        @Query("apply_markdown")
        applyMarkdown: Boolean = false,

        @Query("narrow")
        narrow: String,
    ): ResponseMessages

    @FormUrlEncoded
    @POST("messages")
    suspend fun sendMessage(
        @Field("type") type: String,
        @Field("to") to: Int,
        @Field("topic") topic: String,
        @Field("content") content: String,
    ): ResponseMessage

    @FormUrlEncoded
    @POST("messages/{message_id}/reactions")
    suspend fun addEmojiInMessage(
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ): ResponseResult

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "messages/{message_id}/reactions", hasBody = true)
    suspend fun deleteEmojiInMessage(
        @Path("message_id") messageId: Int,
        @Field("emoji_name") emojiName: String,
    ): ResponseResult
}
