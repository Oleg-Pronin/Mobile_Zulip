package pronin.oleg.zulip.domain.models.streams

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StreamDomain(
    val id: Int,
    val name: String,
    val color: String?,
    val isSubscribed: Boolean,
    val isPrivate: Boolean = false,
    val pinToTop: Boolean,
    val weeklyTraffic: Int,
) : Parcelable
