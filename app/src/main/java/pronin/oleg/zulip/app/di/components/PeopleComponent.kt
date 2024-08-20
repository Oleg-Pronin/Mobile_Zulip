package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.PeopleScope
import pronin.oleg.zulip.app.di.modules.PeopleModule
import pronin.oleg.zulip.app.di.modules.ProfileModule
import pronin.oleg.zulip.presentation.screens.people.PeopleFragment

@PeopleScope
@Subcomponent(modules = [PeopleModule::class, ProfileModule::class])
interface PeopleComponent {
    fun inject(peopleFragment: PeopleFragment)
}
