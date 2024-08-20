package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.ChannelScope
import pronin.oleg.zulip.app.di.modules.ChannelModule
import pronin.oleg.zulip.presentation.screens.channels.ChannelsFragment

@ChannelScope
@Subcomponent(modules = [ChannelModule::class])
interface ChannelComponent {
    fun inject(channelsFragment: ChannelsFragment)
}
