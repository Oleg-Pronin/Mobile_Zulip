package pronin.oleg.zulip.presentation.custom_view.emoji

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.core.view.isGone
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.FlexboxEmojiLayoutBinding

class FlexboxEmojiLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = FlexboxEmojiLayoutBinding.inflate(
        LayoutInflater.from(context), this
    )

    var horizontalMargins = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var verticalMargins = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var isHorizontalGravityEnd: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.FlexboxEmojiLayout) {
            horizontalMargins = getDimensionPixelSize(
                R.styleable.FlexboxEmojiLayout_horizontalMargins,
                horizontalMargins
            )
            verticalMargins = getDimensionPixelSize(
                R.styleable.FlexboxEmojiLayout_verticalMargins,
                verticalMargins
            )
        }

        setGoneAddEmojiBtn()
    }

    fun addEmoji(
        emojiCode: String,
        emojiName: String? = null,
        emojiCount: Int = 1,
        isSelectedEmoji: Boolean = false,
        onClickListener: OnClickListener? = null,
    ) {
        addView(
            EmojiView(context, null, 0, R.style.EmojiStyle).apply {
                layoutParams = MarginLayoutParams(
                    MarginLayoutParams.WRAP_CONTENT,
                    MarginLayoutParams.WRAP_CONTENT
                )

                this.emojiCode = emojiCode
                this.emojiCount = emojiCount
                this.emojiName = emojiName

                isSelected = isSelectedEmoji

                setOnClickListener(onClickListener)
            },
            0
        )

        setGoneAddEmojiBtn()
    }

    fun removeAllEmoji() {
        val countEmoji = children.filter { it is EmojiView }.count()

        if (countEmoji > 0)
            removeViews(0, countEmoji)

        setGoneAddEmojiBtn()
    }

    private fun setGoneAddEmojiBtn() {
        binding.addEmojiBtn.apply {
            isGone = if (isHorizontalGravityEnd)
                true
            else
                children.filter { it is EmojiView }.count() <= 0
        }
    }

    fun setOnClickAddEmoji(onClickListener: OnClickListener?) {
        binding.addEmojiBtn.setOnClickListener(onClickListener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var severalLines = false

        var widthDrawing = 0
        var widthChildren = 0
        var heightChildren = 0

        children.filter { !it.isGone }.iterator().forEach { children ->
            measureChildWithMargins(children, widthMeasureSpec, 0, heightMeasureSpec, 0)

            val width =
                children.let { it.measuredWidth + it.marginStart + it.marginEnd } + horizontalMargins
            val height = children.let { it.measuredHeight + it.marginTop + it.marginBottom }

            if (heightChildren == 0)
                heightChildren += height

            if (widthDrawing + width > parentWidth) {
                severalLines = true

                widthChildren = maxOf(widthChildren, widthDrawing)
                heightChildren += height + verticalMargins

                widthDrawing = width
            } else
                widthDrawing += width
        }

        if (widthChildren == 0 && widthDrawing > 0)
            widthChildren = widthDrawing

        val measuredWidth = when (widthMode) {
            MeasureSpec.AT_MOST -> if (!severalLines)
                minOf(widthChildren, parentWidth)
            else
                parentWidth

            MeasureSpec.EXACTLY -> parentWidth

            MeasureSpec.UNSPECIFIED -> widthChildren

            else -> error("Unreachable")
        } + paddingLeft + paddingRight

        val measuredHeight = heightChildren + paddingTop + paddingBottom

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (isHorizontalGravityEnd)
            endLayout(l, r)
        else
            defaultLayout(l, r)
    }

    private fun endLayout(l: Int, r: Int) {
        val parentWidth = r - l
        var widthDrawing = parentWidth

        var childTop: Int = paddingTop
        var childRight: Int = measuredWidth - paddingEnd

        children.filter { !it.isGone }.iterator().forEach { children ->
            val childrenWidth = children.measuredWidth + horizontalMargins

            if (widthDrawing - childrenWidth < 0) {
                childTop += children.measuredHeight + children.marginTop + verticalMargins
                childRight = measuredWidth - paddingEnd
                widthDrawing = parentWidth
            } else
                widthDrawing -= childrenWidth

            val right = childRight - children.marginEnd
            val top = childTop + children.marginTop
            val left = right - children.measuredWidth
            val bottom = top + children.measuredHeight

            children.layout(left, top, right, bottom)

            childRight -= childrenWidth
        }
    }

    private fun defaultLayout(l: Int, r: Int) {
        val parentWidth = r - l
        var widthDrawing = 0

        var childTop: Int = paddingTop
        var childLeft: Int = paddingLeft

        children.filter { !it.isGone }.iterator().forEach { children ->
            val childrenWidth = children.measuredWidth + horizontalMargins

            if (widthDrawing + childrenWidth > parentWidth) {
                childTop += children.measuredHeight + children.marginTop + verticalMargins
                childLeft = paddingLeft
                widthDrawing = childrenWidth
            } else
                widthDrawing += childrenWidth

            val left = childLeft + children.marginLeft
            val top = childTop + children.marginTop
            val right = left + children.measuredWidth
            val bottom = top + children.measuredHeight

            children.layout(left, top, right, bottom)

            childLeft += childrenWidth
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }
}