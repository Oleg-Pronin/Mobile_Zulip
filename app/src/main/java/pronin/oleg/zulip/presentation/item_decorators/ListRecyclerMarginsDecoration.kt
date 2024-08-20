package pronin.oleg.zulip.presentation.item_decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListRecyclerMarginsDecoration(
    private val firstItemTopMargin: Int,
    private val firstItemBottomMargin: Int,
    private val firstItemStartMargin: Int,
    private val firstItemEndMargin: Int,
    private val lastItemTopMargin: Int,
    private val lastItemBottomMargin: Int,
    private val lastItemStartMargin: Int,
    private val lastItemEndMargin: Int,
    private val topMargin: Int,
    private val bottomMargin: Int,
    private val startMargin: Int,
    private val endMargin: Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemsCount = parent.adapter?.itemCount ?: 0
        val position = (view.layoutParams as? RecyclerView.LayoutParams)?.viewLayoutPosition ?: 0

        outRect.set(
            getLeftMargin(itemsCount, position),
            getTopMargin(itemsCount, position),
            getRightMargin(itemsCount, position),
            getBottomMargin(itemsCount, position)
        )
    }

    private fun getLeftMargin(itemCount: Int, position: Int): Int {
        return when (position) {
            0 -> firstItemStartMargin
            itemCount - 1 -> lastItemStartMargin
            else -> startMargin
        }
    }

    private fun getRightMargin(itemCount: Int, position: Int): Int {
        return when (position) {
            0 -> firstItemEndMargin
            itemCount - 1 -> lastItemEndMargin
            else -> endMargin
        }
    }

    private fun getTopMargin(itemCount: Int, position: Int): Int {
        return when (position) {
            0 -> firstItemTopMargin
            itemCount - 1 -> lastItemTopMargin
            else -> topMargin
        }
    }

    private fun getBottomMargin(itemCount: Int, position: Int): Int {
        return when (position) {
            0 -> firstItemBottomMargin
            itemCount - 1 -> lastItemBottomMargin
            else -> bottomMargin
        }
    }

    companion object {
        fun verticalListMargins(
            vertical: Int = 0,
            horizontal: Int = 0,
            firstTop: Int = vertical,
            lastBottom: Int = vertical
        ) = ListRecyclerMarginsDecoration(
            firstItemTopMargin = firstTop,
            firstItemBottomMargin = vertical / 2,
            firstItemStartMargin = horizontal,
            firstItemEndMargin = horizontal,
            lastItemTopMargin = vertical / 2,
            lastItemBottomMargin = lastBottom,
            lastItemStartMargin = horizontal,
            lastItemEndMargin = horizontal,
            topMargin = vertical / 2,
            bottomMargin = vertical / 2,
            startMargin = horizontal,
            endMargin = horizontal
        )

        fun horizontalListMargins(
            horizontal: Int = 0,
            vertical: Int = 0,
            firstStart: Int = 0,
            lastEnd: Int = 0
        ) = ListRecyclerMarginsDecoration(
            firstItemTopMargin = vertical,
            firstItemBottomMargin = vertical,
            firstItemStartMargin = firstStart,
            firstItemEndMargin = 0,
            lastItemTopMargin = vertical,
            lastItemBottomMargin = vertical,
            lastItemStartMargin = horizontal,
            lastItemEndMargin = lastEnd,
            topMargin = vertical,
            bottomMargin = vertical,
            startMargin = horizontal,
            endMargin = 0
        )
    }
}
