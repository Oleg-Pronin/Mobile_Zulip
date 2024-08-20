package pronin.oleg.zulip.presentation.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import pronin.oleg.zulip.R
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class ElmBaseFragment<VB : ViewBinding, Effect : Any, ViewState : Any, Event : Any>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
) : Fragment(), ElmRendererDelegate<Effect, ViewState> {

    private var _binding: VB? = null
    protected val binding get() = checkNotNull(_binding)

    abstract val store: Store<Event, Effect, ViewState>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflater(inflater, container, false)

        return binding.root
    }

    fun setBarColor(@ColorRes colorId: Int) {
        requireActivity().window.apply {
            statusBarColor = requireContext().getColor(colorId)
        }
    }

    fun setBarColorInt(@ColorInt colorInt: Int) {
        requireActivity().window.apply {
            statusBarColor = colorInt
        }
    }

    fun showError(
        message: String?,
        onClickRetry: (() -> Unit)? = null
    ) {
        Snackbar.make(requireView(), message ?: "", Snackbar.LENGTH_LONG).apply {
            if (message == null)
                setText(R.string.error_message)

            requireContext().let { context: Context ->
                setActionTextColor(context.getColor(R.color.lotion))
                setTextColor(context.getColor(R.color.lotion))
                setBackgroundTint(context.getColor(R.color.naughty_red))

                if (onClickRetry != null)
                    setAction(context.getText(R.string.error_retry)) { onClickRetry() }
            }
        }.show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
