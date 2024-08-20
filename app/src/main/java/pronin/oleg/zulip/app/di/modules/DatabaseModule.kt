package pronin.oleg.zulip.app.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import pronin.oleg.zulip.data.database.AppDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "homework")
            .fallbackToDestructiveMigration()
            .build()
    }
}
