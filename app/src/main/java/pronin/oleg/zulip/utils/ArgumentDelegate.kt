package pronin.oleg.zulip.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

@Suppress("DEPRECATION")
inline fun <F, reified T> argumentDelegate(
    crossinline provideArguments: (F) -> Bundle?,
): LazyProvider<F, T> = object : LazyProvider<F, T> {
    override fun provideDelegate(thisRef: F, prop: KProperty<*>) =
        lazy {
            val bundle = provideArguments(thisRef)
            bundle?.get(prop.name) as T
        }
}

interface LazyProvider<A, T> {
    operator fun provideDelegate(thisRef: A, prop: KProperty<*>): Lazy<T>
}

inline fun <reified T> argumentDelegate(): LazyProvider<Fragment, T> {
    return argumentDelegate { it.arguments }
}
