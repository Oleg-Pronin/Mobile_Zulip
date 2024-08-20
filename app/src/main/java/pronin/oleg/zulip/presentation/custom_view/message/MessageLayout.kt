package pronin.oleg.zulip.presentation.custom_view.message

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.bumptech.glide.Glide
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.MessageLayoutBinding

class MessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = MessageLayoutBinding.inflate(LayoutInflater.from(context), this)

    private val avatarView = binding.userAvatar
    private val messageInfoView = binding.messageInfo
    private val flexboxEmojiView = binding.flexboxEmoji

    var isOwnMessage: Boolean = false
        set(value) {
            if (field != value) {
                field = value

                avatarView.isGone = value
                flexboxEmojiView.isHorizontalGravityEnd = value
                messageInfoView.isHiddenUserFullName = value

                requestLayout()
            }
        }

    var messageUserAvatar: String? = null
        set(value) {
            if (field != value) {
                field = value

                Glide.with(context)
                    .load(field)
                    .error(R.drawable.ic_avatar)
                    .into(avatarView)

                invalidate()
            }
        }

    var messageInfoUserFullName: String = ""
        set(value) {
            if (field != value) {
                field = value
                messageInfoView.userFullName = value

                requestLayout()
            }
        }

    var messageInfoContent: String = ""
        set(value) {
            if (field != value) {
                field = value
                messageInfoView.messageContent = value

                requestLayout()
            }
        }

    @DrawableRes
    var messageInfoBackgroundRes: Int = 0
        set(value) {
            if (field != value) {
                field = value
                messageInfoView.setBackgroundResource(field)

                requestLayout()
            }
        }

    var gravity: Int = Gravity.START

    init {
        context.withStyledAttributes(attrs, R.styleable.MessageLayout) {
            messageUserAvatar = getString(R.styleable.MessageLayout_messageUserAvatar)
            isOwnMessage = getBoolean(R.styleable.MessageLayout_isOwnMessage, isOwnMessage)
            messageInfoUserFullName =
                getString(R.styleable.MessageLayout_messageInfoUserFullName) ?: ""
            messageInfoContent = getString(R.styleable.MessageLayout_messageInfoContent) ?: ""
            messageInfoBackgroundRes =
                getResourceId(R.styleable.MessageLayout_messageInfoBackground, 0)
            gravity = getInt(R.styleable.MessageLayout_android_gravity, Gravity.START)
        }
    }

    fun setOnClickAddEmoji(onClickListener: OnClickListener?) {
        flexboxEmojiView.setOnClickAddEmoji(onClickListener)
    }

    fun addEmoji(
        emojiCode: String,
        emojiName: String? = null,
        emojiCount: Int = 1,
        isSelectedEmoji: Boolean = false,
        onClickListener: OnClickListener? = null,
    ) {
        flexboxEmojiView.addEmoji(emojiCode, emojiName, emojiCount, isSelectedEmoji, onClickListener)
    }

    fun clearEmoji() {
        flexboxEmojiView.removeAllEmoji()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(avatarView, widthMeasureSpec, 0, heightMeasureSpec, 0)

        val avatarWidth = if (!isOwnMessage)
            avatarView.let { it.measuredWidth + it.marginStart + it.marginEnd }
        else
            0

        measureChildWithMargins(
            messageInfoView,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            0
        )

        measureChildWithMargins(
            flexboxEmojiView,
            widthMeasureSpec,
            avatarWidth,
            heightMeasureSpec,
            0
        )

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)

        val widthChildren = avatarWidth +
                maxOf(
                    messageInfoView.let { it.measuredWidth + it.marginStart + it.marginStart },
                    flexboxEmojiView.let { it.measuredWidth + it.marginStart + it.marginEnd }
                )

        val width = when (widthMode) {
            MeasureSpec.AT_MOST -> minOf(widthChildren, parentWidth)

            MeasureSpec.EXACTLY -> parentWidth

            MeasureSpec.UNSPECIFIED -> widthChildren

            else -> error("Unreachable")
        }

        val height = messageInfoView.let { it.measuredHeight + it.marginTop + it.marginBottom } +
                flexboxEmojiView.let { it.measuredHeight + it.marginTop + it.marginBottom }

        setMeasuredDimension(
            width + paddingStart + paddingEnd,
            height + paddingTop + paddingEnd
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isOwnMessage)
            endLayout()
        else
            defaultLayout()
    }

    private fun defaultLayout() {
        val avatarViewLeft = paddingLeft + avatarView.marginStart
        val avatarViewTop = paddingTop + avatarView.marginTop
        val avatarViewRight =
            avatarViewLeft + if (avatarView.isGone) 0 else avatarView.measuredWidth
        val avatarViewBottom =
            avatarViewTop + if (avatarView.isGone) 0 else avatarView.measuredHeight

        avatarView.layout(avatarViewLeft, avatarViewTop, avatarViewRight, avatarViewBottom)

        val messageInfoViewLeft = avatarViewRight + messageInfoView.marginStart
        val messageInfoViewTop = paddingTop + messageInfoView.marginTop
        val messageInfoViewRight = messageInfoViewLeft + messageInfoView.measuredWidth
        val messageInfoViewBottom = messageInfoViewTop + messageInfoView.measuredHeight

        messageInfoView.layout(
            messageInfoViewLeft,
            messageInfoViewTop,
            messageInfoViewRight,
            messageInfoViewBottom
        )

        val flexboxEmojiViewLeft = avatarViewRight + flexboxEmojiView.marginStart
        val flexboxEmojiViewTop = messageInfoViewBottom + flexboxEmojiView.marginTop
        val flexboxEmojiViewRight = flexboxEmojiViewLeft + flexboxEmojiView.measuredWidth
        val flexboxEmojiViewBottom = flexboxEmojiViewTop + flexboxEmojiView.measuredHeight

        flexboxEmojiView.layout(
            flexboxEmojiViewLeft,
            flexboxEmojiViewTop,
            flexboxEmojiViewRight,
            flexboxEmojiViewBottom
        )
    }

    private fun endLayout() {
        val messageInfoViewRight = measuredWidth - messageInfoView.marginEnd
        val messageInfoViewTop = paddingTop + messageInfoView.marginTop
        val messageInfoViewLeft = messageInfoViewRight - messageInfoView.measuredWidth
        val messageInfoViewBottom = messageInfoViewTop + messageInfoView.measuredHeight

        messageInfoView.layout(
            messageInfoViewLeft,
            messageInfoViewTop,
            messageInfoViewRight,
            messageInfoViewBottom
        )

        val avatarViewRight = messageInfoViewLeft - messageInfoView.marginStart
        val avatarViewTop = paddingTop + avatarView.marginTop
        val avatarViewLeft = avatarViewRight - if (avatarView.isGone) 0 else avatarView.measuredWidth
        val avatarViewBottom = avatarViewTop + if (avatarView.isGone) 0 else avatarView.measuredHeight

        avatarView.layout(avatarViewLeft, avatarViewTop, avatarViewRight, avatarViewBottom)

        val flexboxEmojiViewRight = messageInfoViewRight - flexboxEmojiView.marginEnd
        val flexboxEmojiViewTop = messageInfoViewBottom + flexboxEmojiView.marginTop
        val flexboxEmojiViewLeft = flexboxEmojiViewRight - flexboxEmojiView.measuredWidth
        val flexboxEmojiViewBottom = flexboxEmojiViewTop + flexboxEmojiView.measuredHeight

        flexboxEmojiView.layout(
            flexboxEmojiViewLeft,
            flexboxEmojiViewTop,
            flexboxEmojiViewRight,
            flexboxEmojiViewBottom
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}