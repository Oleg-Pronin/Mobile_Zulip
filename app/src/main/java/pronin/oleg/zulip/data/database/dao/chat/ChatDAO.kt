package pronin.oleg.zulip.data.database.dao.chat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import pronin.oleg.zulip.data.database.models.chat.MessageAndReactionDBO
import pronin.oleg.zulip.data.database.models.chat.MessageDBO
import pronin.oleg.zulip.data.database.models.chat.ReactionDBO

@Dao
interface ChatDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMessage(message: MessageDBO): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReaction(reactions: List<ReactionDBO>)

    @Transaction
    fun addMessageWithReaction(message: MessageDBO, reactions: List<ReactionDBO>) {
        val messageId = addMessage(message)

        reactions.forEach { it.messageId = messageId }

        addReaction(reactions)
    }

    @Transaction
    @Query(
        "Select * from messages where id <= :lastMessageId " +
                "order by id desc " +
                "limit :limit "
    )
    fun getMessages(lastMessageId: Int, limit: Int): List<MessageAndReactionDBO>

    @Query("Select count(id) from messages")
    fun getCountMessage(): Int

    @Query("Delete from messages where streamId == :streamId and subject == :topicName")
    fun removeAllMessages(streamId: Int, topicName: String)
}
