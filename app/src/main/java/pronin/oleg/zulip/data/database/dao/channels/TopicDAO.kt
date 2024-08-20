package pronin.oleg.zulip.data.database.dao.channels

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pronin.oleg.zulip.data.database.models.streams.TopicDBO

@Dao
interface TopicDAO {

    @Query("select * from topic where streamId == :streamId")
    fun getAllTopics(streamId: Int) : List<TopicDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTopics(topics: List<TopicDBO>)

    @Query("Delete from topic where streamId == :streamId")
    fun removeAllTopic(streamId: Int)
}
