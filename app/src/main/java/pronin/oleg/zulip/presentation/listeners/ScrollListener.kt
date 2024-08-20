package pronin.oleg.zulip.presentation.listeners

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollListener(
    private val layoutManager: LinearLayoutManager,
    private val loadMoreCallback: (Int) -> Unit,
) : RecyclerView.OnScrollListener() {
    private var previousFirstVisibleItem = 0
    private var previousLastVisibleItem = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy < 0) {
            previousFirstVisibleItem = layoutManager.findFirstVisibleItemPosition().also {
                if (previousFirstVisibleItem != it) {
                    loadMoreCallback(it)
                }
            }
        } else {
            previousLastVisibleItem = layoutManager.findLastVisibleItemPosition().also {
                if (previousLastVisibleItem != it) {
                    loadMoreCallback(it)
                }
            }
        }
    }
}
