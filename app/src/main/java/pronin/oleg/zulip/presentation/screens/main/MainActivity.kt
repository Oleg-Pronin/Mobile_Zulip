package pronin.oleg.zulip.presentation.screens.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.ActivityMainBinding
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.presentation.navigation.Screens
import pronin.oleg.zulip.utils.lazyUnsafe
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val binding by lazyUnsafe { ActivityMainBinding.inflate(layoutInflater) }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator by lazyUnsafe { AppNavigator(this, R.id.rootNavHostFragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().inject(this)

        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.channels -> {
                    router.newRootScreen(Screens.Channels())
                    true
                }

                R.id.people -> {
                    router.newRootScreen(Screens.People())
                    true
                }

                R.id.profile -> {
                    router.newRootScreen(Screens.Profile())
                    true
                }

                else -> false
            }
        }

        if (savedInstanceState == null)
            router.newRootScreen(Screens.Channels())
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}
