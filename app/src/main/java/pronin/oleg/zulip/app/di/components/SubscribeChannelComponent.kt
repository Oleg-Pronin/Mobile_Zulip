package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.CreateChatScope
import pronin.oleg.zulip.app.di.modules.ChatModule
import pronin.oleg.zulip.app.di.modules.SubscribeChannelModule
import pronin.oleg.zulip.presentation.dialogs.create_channel.CreateChannelDialogFragment

@CreateChatScope
@Subcomponent(modules = [ChatModule::class, SubscribeChannelModule::class])
interface SubscribeChannelComponent {
    fun inject(createChannelDialogFragment: CreateChannelDialogFragment)
}
