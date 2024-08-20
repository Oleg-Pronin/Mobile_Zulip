package pronin.oleg.zulip.utils

import android.view.View

class SafeClickListener(
    private var defaultInterval: Long = DEFAULT_INTERVAL,
    private val onSafeCLick: (View) -> Unit
): View.OnClickListener {
    private var windowStartTime = System.currentTimeMillis()

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        val delta = currentTime - windowStartTime

        if (delta >= defaultInterval) {
            windowStartTime = currentTime

            onSafeCLick(v)
        } else
            return
    }

    companion object {
        const val DEFAULT_INTERVAL = 600L
    }
}
