package pronin.oleg.zulip.app

import android.app.Application
import pronin.oleg.zulip.app.di.components.AppComponent
import pronin.oleg.zulip.app.di.components.DaggerAppComponent
import pronin.oleg.zulip.utils.lazyUnsafe

open class App : Application() {
    val appComponent: AppComponent by lazyUnsafe {
        DaggerAppComponent.builder()
            .context(this)
            .baseUrl(getBaseUri())
            .build()
    }

    open fun getBaseUri() = Const.BASE_URL

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}

fun appComponent() = App.INSTANCE.appComponent
