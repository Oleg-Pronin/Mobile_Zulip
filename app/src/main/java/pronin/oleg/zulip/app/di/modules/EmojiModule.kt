package pronin.oleg.zulip.app.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import pronin.oleg.zulip.app.di.annotations.EmojiScope
import pronin.oleg.zulip.app.di.models.emojis.EmojiCodes
import pronin.oleg.zulip.utils.getJsonDataFromAsset

@Module
object EmojiModule {

    @Provides
    @EmojiScope
    fun provideEmoji(context: Context, json: Json): EmojiCodes =
        getJsonDataFromAsset(context, "emoji_codes.json")
            .let {
                json.decodeFromString<EmojiCodes>(it)
            }
}
