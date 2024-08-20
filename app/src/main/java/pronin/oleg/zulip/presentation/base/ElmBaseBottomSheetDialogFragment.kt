package pronin.oleg.zulip.presentation.base

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_IMPLICIT_ONLY
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import pronin.oleg.zulip.R
import vivid.money.elmslie.android.renderer.ElmRendererDelegate
import vivid.money.elmslie.core.store.Store

abstract class ElmBaseBottomSheetDialogFragment<VB : ViewBinding, Effect : Any, ViewState : Any, Event : Any>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BottomSheetDialogFragment(), ElmRendererDelegate<Effect, ViewState> {

    private var _binding: VB? = null
    protected val binding get() = checkNotNull(_binding)

    abstract val store: Store<Event, Effect, ViewState>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflater(inflater, container, false)

        initBinding()

        return binding.root
    }

    abstract fun initBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { onShowDialog(it as BottomSheetDialog) }

        return dialog
    }

    private fun onShowDialog(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(
            com.google.android.material.R.id.design_bottom_sheet
        )!!

        BottomSheetBehavior.from(bottomSheet).apply {
            peekHeight = resources.displayMetrics.heightPixels
            state = BottomSheetBehavior.STATE_EXPANDED
            isDraggable = false
            skipCollapsed = false
        }
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

    fun showSuccess(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
            requireContext().let { context: Context ->
                setActionTextColor(context.getColor(R.color.lotion))
                setTextColor(context.getColor(R.color.lotion))
                setBackgroundTint(context.getColor(R.color.sea_green))
            }
        }.show()
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

    fun hideKeyboard() {
        val view = requireView()

        (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(view.windowToken, 0)

        view.clearFocus()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
