package pronin.oleg.zulip.domain.enums

import androidx.annotation.StringRes
import pronin.oleg.zulip.R

enum class ChannelTabs {
    SUBSCRIBED, ALL_STREAMS;

    @StringRes
    fun getTitleRes(): Int = when(this) {
        SUBSCRIBED -> R.string.subscribed
        ALL_STREAMS -> R.string.all_streams
    }
}
