package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.ChatScope
import pronin.oleg.zulip.app.di.annotations.EmojiScope
import pronin.oleg.zulip.app.di.annotations.ProfileScope
import pronin.oleg.zulip.app.di.modules.ChatModule
import pronin.oleg.zulip.app.di.modules.EmojiModule
import pronin.oleg.zulip.app.di.modules.ProfileModule
import pronin.oleg.zulip.presentation.screens.chat.ChatFragment

@ChatScope
@EmojiScope
@ProfileScope
@Subcomponent(modules = [ChatModule::class, ProfileModule::class, EmojiModule::class])
interface ChatComponent {
    fun inject(chatFragment: ChatFragment)
}
