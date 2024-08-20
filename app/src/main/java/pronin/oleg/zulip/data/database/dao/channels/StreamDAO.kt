package pronin.oleg.zulip.data.database.dao.channels

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pronin.oleg.zulip.data.database.models.streams.StreamDBO

@Dao
interface StreamDAO {
    @Query("Select * from stream")
    fun getAllStreams(): List<StreamDBO>

    @Query("Select * from stream where isSubscribed = 1")
    fun getAllSubscribedStreams(): List<StreamDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStream(streams: List<StreamDBO>)

    @Query("Delete from stream")
    fun removeAllStreams()
}
