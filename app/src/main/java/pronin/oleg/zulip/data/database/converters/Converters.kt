package pronin.oleg.zulip.data.database.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime
import pronin.oleg.zulip.utils.toLocalDateTime
import pronin.oleg.zulip.utils.toLong

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.toLocalDateTime()
    }

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.toLong()
    }
}
