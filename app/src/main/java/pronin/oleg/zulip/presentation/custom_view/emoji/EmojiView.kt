package pronin.oleg.zulip.presentation.custom_view.emoji

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.withStyledAttributes
import pronin.oleg.zulip.R

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defTheme: Int = 0,
) : View(context, attrs, defStyle, defTheme) {

    var emojiCode: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var emojiCount: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    var emojiName: String? = ""
        set(value) {
            if (field != value) {
                field = value
            }
        }

    @ColorInt
    var emojiTextColor: Int = Color.WHITE
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    @Dimension
    var emojiTextSize: Float = 24f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    private val textToDraw
        get() = if (emojiCode.isNotEmpty())
            "$emojiCode $emojiCount"
        else
            emojiCount.toString()

    init {
        context.withStyledAttributes(attrs, R.styleable.EmojiView, defStyle, defTheme) {
            emojiCode = getString(R.styleable.EmojiView_emoji) ?: emojiCode
            emojiCount = getInt(R.styleable.EmojiView_emojiCount, 0)
            emojiTextSize = getDimension(R.styleable.EmojiView_emojiTextSize, emojiTextSize)
            emojiTextColor = getColor(R.styleable.EmojiView_emojiTextColor, emojiTextColor)
        }
    }

    private val textPaint = TextPaint().apply {
        color = emojiTextColor
        textSize = emojiTextSize
    }

    private val textRect = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        textPaint.getTextBounds(textToDraw, 0, textToDraw.length, textRect)

        val actualWidth = resolveSize(
            paddingStart + paddingEnd + textRect.width(),
            widthMeasureSpec
        )

        val actualHeight = resolveSize(
            paddingTop + paddingBottom + textRect.height(),
            heightMeasureSpec
        )

        setMeasuredDimension(actualWidth, actualHeight)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawText(
            textToDraw,
            paddingLeft.toFloat(),
            height / 2 - textRect.exactCenterY(),
            textPaint
        )
    }
}
