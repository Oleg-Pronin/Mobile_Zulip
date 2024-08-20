package pronin.oleg.zulip.presentation.ui

import android.graphics.Color
import pronin.oleg.zulip.presentation.base.BaseItemUI

sealed class ChannelUI : BaseItemUI {
    abstract override val id: Int
    abstract val name: String

    data class StreamUI(
        override val id: Int,
        override val name: String,
        var isPrivate: Boolean = false,
        var isLoading: Boolean = false,
        var isOpened: Boolean = false,
        var noClickDownloadTopics: Boolean = false,
        val color: Color?,
    ) : ChannelUI()

    data class TopicUI(
        override val id: Int,
        override val name: String,
        val parentId: Int,
        val lastMessageId: Int,
        val count: Int,
        val color: Color?,
    ) : ChannelUI()
}
