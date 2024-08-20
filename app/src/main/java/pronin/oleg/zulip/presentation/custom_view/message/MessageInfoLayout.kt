package pronin.oleg.zulip.presentation.custom_view.message

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.MessageInfoLayoutBinding

class MessageInfoLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val _binding = MessageInfoLayoutBinding
        .inflate(LayoutInflater.from(context), this)

    var userFullName: String = ""
        set(value) {
            if (field != value) {
                field = value
                _binding.userFullName.text = value

                requestLayout()
            }
        }

    var messageContent: String = ""
        set(value) {
            if (field != value) {
                field = value
                _binding.messageContent.text = value

                requestLayout()
            }
        }

    var isHiddenUserFullName: Boolean = false
        set(value) {
            if (field != value) {
                field = value

                _binding.userFullName.isGone = value

                requestLayout()
            }
        }

    private val userFullNameView = _binding.userFullName
    private val messageContentView = _binding.messageContent

    init {
        context.withStyledAttributes(attrs, R.styleable.MessageInfoLayout) {
            userFullName = getString(R.styleable.MessageInfoLayout_userFullName) ?: ""
            messageContent = getString(R.styleable.MessageInfoLayout_messageContent) ?: ""
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(userFullNameView, widthMeasureSpec, 0, heightMeasureSpec, 0)
        measureChildWithMargins(messageContentView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val firstWidth =
            if (isHiddenUserFullName) 0 else userFullNameView.let { it.measuredWidth + it.marginLeft + it.marginRight }
        val firstHeight =
            if (isHiddenUserFullName) 0 else userFullNameView.let { it.measuredHeight + it.marginTop + it.marginBottom }

        val secondWidth =
            messageContentView.let { it.measuredWidth + it.marginLeft + it.marginRight }
        val secondHeight =
            messageContentView.let { it.measuredHeight + it.marginTop + it.marginBottom }

        setMeasuredDimension(
            maxOf(firstWidth, secondWidth) + paddingStart + paddingEnd,
            firstHeight + secondHeight + paddingTop + paddingEnd
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val firstLeft = paddingLeft + if (isHiddenUserFullName) 0 else userFullNameView.marginStart
        val firstTop = paddingTop + if (isHiddenUserFullName) 0 else userFullNameView.marginTop
        val firstRight = firstLeft + if (isHiddenUserFullName) 0 else userFullNameView.measuredWidth
        val firstBottom = firstTop + if (isHiddenUserFullName) 0 else userFullNameView.measuredHeight

        userFullNameView.layout(firstLeft, firstTop, firstRight, firstBottom)

        val secondLeft = paddingLeft + messageContentView.marginLeft
        val secondTop = firstBottom + userFullNameView.marginBottom + messageContentView.marginTop
        val secondRight = secondLeft + messageContentView.measuredWidth
        val secondBottom = secondTop + messageContentView.measuredHeight

        messageContentView.layout(secondLeft, secondTop, secondRight, secondBottom)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}
