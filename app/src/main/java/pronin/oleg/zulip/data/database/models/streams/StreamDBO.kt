package pronin.oleg.zulip.data.database.models.streams

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stream")
data class StreamDBO(
    @PrimaryKey
    val id: Int,
    val name: String,
    val color: String? = null,
    val isSubscribed: Boolean = false,
    val isPrivate: Boolean = false,
    val pinToTop: Boolean = false,
    val weeklyTraffic: Int = 0,
)
