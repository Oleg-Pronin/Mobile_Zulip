package pronin.oleg.zulip.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pronin.oleg.zulip.data.database.converters.Converters
import pronin.oleg.zulip.data.database.dao.channels.StreamDAO
import pronin.oleg.zulip.data.database.dao.channels.TopicDAO
import pronin.oleg.zulip.data.database.dao.chat.ChatDAO
import pronin.oleg.zulip.data.database.models.chat.MessageDBO
import pronin.oleg.zulip.data.database.models.chat.ReactionDBO
import pronin.oleg.zulip.data.database.models.streams.StreamDBO
import pronin.oleg.zulip.data.database.models.streams.TopicDBO

@Database(
    entities = [
        StreamDBO::class,
        TopicDBO::class,
        MessageDBO::class,
        ReactionDBO::class
    ],
    version = 7
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun streamDao(): StreamDAO

    abstract fun topicDao(): TopicDAO

    abstract fun chatDao(): ChatDAO
}
