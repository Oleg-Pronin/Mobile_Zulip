package pronin.oleg.zulip.app.di.modules

import dagger.Module

@Module(
    includes = [
        NavigationModule::class,
        NetworkModule::class,
        JsonModule::class,
        DatabaseModule::class
    ]
)
interface AppModule
