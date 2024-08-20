package pronin.oleg.zulip.presentation.dialogs.emoji

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pronin.oleg.zulip.app.appComponent
import pronin.oleg.zulip.databinding.DialogEmojiBinding
import pronin.oleg.zulip.app.di.models.emojis.EmojiCodes
import pronin.oleg.zulip.utils.argumentDelegate
import pronin.oleg.zulip.utils.getUnicodeByEmoji
import javax.inject.Inject

class EmojiDialogFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var emojis: EmojiCodes

    private lateinit var binding: DialogEmojiBinding

    private val messageId by argumentDelegate<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().emojiComponent.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogEmojiBinding.inflate(inflater, container, false)

        binding.emojiPicker.setOnEmojiPickedListener {
            val emojiName = emojis.codepoinToName[getUnicodeByEmoji(it.emoji)] ?: ""

            setFragmentResult(
                EmojiDialogResult.REQUEST_KEY,
                bundleOf(
                    EmojiDialogResult.RESULT_KEY to EmojiDialogResult(
                        messageId, it.emoji, emojiName
                    )
                )
            )

            dismiss()
        }

        return binding.root
    }

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

    companion object {
        private const val MESSAGE_ID = "messageId"

        fun getInstance(messageId: Int) = EmojiDialogFragment().apply {
            arguments = Bundle().apply { putInt(MESSAGE_ID, messageId) }
        }
    }
}
