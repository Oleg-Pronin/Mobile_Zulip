package pronin.oleg.zulip.data.database.models.streams

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic")
data class TopicDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val streamId : Int,
    val name: String,
    val lastMessageId: Int,
    val unreadCount: Int
)
