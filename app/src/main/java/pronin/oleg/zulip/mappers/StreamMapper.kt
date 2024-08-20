package pronin.oleg.zulip.mappers

import android.graphics.Color
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import pronin.oleg.zulip.data.database.models.streams.StreamDBO
import pronin.oleg.zulip.data.database.models.streams.TopicDBO
import pronin.oleg.zulip.data.network.models.responses.ResponseSubscribe
import pronin.oleg.zulip.data.network.models.streams.Stream
import pronin.oleg.zulip.data.network.models.streams.SubscribedStream
import pronin.oleg.zulip.data.network.models.streams.Topic
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.domain.models.streams.SubscribeDomain
import pronin.oleg.zulip.domain.models.streams.TopicDomain
import pronin.oleg.zulip.presentation.ui.ChannelUI

fun Stream.toDomain() = StreamDomain(
    id = id,
    name = name,
    color = null,
    isSubscribed = false,
    isPrivate = isPrivate,
    pinToTop = false,
    weeklyTraffic = weeklyTraffic ?: 0
)

fun SubscribedStream.toDomain() = StreamDomain(
    id = id,
    name = name,
    color = color,
    isSubscribed = true,
    isPrivate = isPrivate,
    pinToTop = pinToTop,
    weeklyTraffic = weeklyTraffic ?: 0
)

fun StreamDBO.toDomain() = StreamDomain(
    id = id,
    name = name,
    color = color,
    isSubscribed = isSubscribed,
    isPrivate = isPrivate,
    pinToTop = pinToTop,
    weeklyTraffic = weeklyTraffic
)

fun Topic.toDomain() = TopicDomain(
    id = null,
    lastMessageId = lastMessageId,
    name = name
)

fun TopicDBO.toDomain() = TopicDomain(
    id = id,
    lastMessageId = lastMessageId,
    name = name,
    unreadCount = unreadCount
)

fun ChannelUI.StreamUI.toDomain() = StreamDomain(
    id = id,
    name = name,
    color = color?.toArgb()?.let { String.format("#%06X", 0xFFFFFF and it) },
    isSubscribed = false,
    isPrivate = isPrivate,
    pinToTop = false,
    weeklyTraffic = 0
)

fun StreamDomain.toUI(isSubscribed: Boolean) = ChannelUI.StreamUI(
    id = id,
    isOpened = false,
    isPrivate = isPrivate,
    name = name,
    color = color?.toColorInt()?.toColor(),
    noClickDownloadTopics = !isSubscribed
)

fun StreamDomain.toDBO() = StreamDBO(
    id = id,
    name = name,
    color = color,
    isSubscribed = isSubscribed,
    isPrivate = isPrivate,
    pinToTop = pinToTop,
    weeklyTraffic = weeklyTraffic
)

fun TopicDomain.toUI(
    parentId: Int,
    color: Color?,
) = ChannelUI.TopicUI(
    id = lastMessageId,
    parentId = parentId,
    lastMessageId = lastMessageId,
    name = name,
    count = unreadCount,
    color = color
)

fun TopicDomain.toDBO(
    streamId: Int,
) = TopicDBO(
    id = id,
    streamId = streamId,
    name = name,
    lastMessageId = lastMessageId,
    unreadCount = unreadCount,
)

fun ResponseSubscribe.toDomain() = SubscribeDomain(
    subscribed, alreadySubscribed
)
