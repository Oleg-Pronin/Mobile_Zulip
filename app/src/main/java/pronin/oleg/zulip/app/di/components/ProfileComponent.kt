package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.ProfileScope
import pronin.oleg.zulip.app.di.modules.ProfileModule
import pronin.oleg.zulip.presentation.screens.profile.ProfileFragment

@ProfileScope
@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)
}
