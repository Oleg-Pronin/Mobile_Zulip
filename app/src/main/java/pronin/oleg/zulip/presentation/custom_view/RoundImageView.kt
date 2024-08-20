package pronin.oleg.zulip.presentation.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.withStyledAttributes
import pronin.oleg.zulip.R

@SuppressLint("AppCompatCustomView")
class RoundImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ImageView(context, attrs, defStyleAttr, defStyleRes) {

    var radius: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.RoundImageView) {
            radius = getDimension(R.styleable.RoundImageView_radius, radius)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        if (radius > 0f) {
            val path = Path()
            val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

            path.addRoundRect(rect, radius, radius, Path.Direction.CW)
            canvas.clipPath(path)
        }

        super.onDraw(canvas)
    }
}