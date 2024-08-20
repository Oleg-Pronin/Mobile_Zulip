package pronin.oleg.zulip.data.database.models.chat

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "messages")
data class MessageDBO(
    @PrimaryKey
    val id: Int,
    val dateTime: LocalDateTime,
    val senderId: Int,
    val senderAvatar: String? = null,
    val senderFullName: String,
    val content: String,
    val streamId: Int,
    val subject: String,
)

@Entity(
    tableName = "reactions",
    foreignKeys = [
        ForeignKey(
            entity = MessageDBO::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("messageId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ReactionDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(index = true)
    var messageId: Long,
    val userId: Int,
    val emojiName: String,
    val emojiCode: String,
    val isSelected: Boolean = false,
)

@Entity(tableName = "messages_and_reactions")
data class MessageAndReactionDBO(
    @Embedded
    val message: MessageDBO,

    @Relation(
        parentColumn = "id",
        entityColumn = "messageId"
    )
    val reactions: List<ReactionDBO>
)
