package pronin.oleg.zulip.app.di.components

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pronin.oleg.zulip.app.di.modules.AppModule
import pronin.oleg.zulip.app.di.modules.EventModule
import pronin.oleg.zulip.presentation.screens.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, EventModule::class])
interface AppComponent {

    val channelComponent: ChannelComponent

    val chatComponent: ChatComponent

    val createChatComponent: SubscribeChannelComponent

    val peopleComponent: PeopleComponent

    val profileComponent: ProfileComponent

    val emojiComponent: EmojiComponent

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun baseUrl(baseUrl: String): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}
