package pronin.oleg.zulip.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI

class SimpleListAdapter<T: BaseItemUI>(
    diffCallback: DiffUtil.ItemCallback<T>,
    vararg delegates: AdapterDelegate<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val delegatesManager: MutableList<AdapterDelegate<T>> = mutableListOf()
    private var differ: AsyncListDiffer<T> = AsyncListDiffer(this, diffCallback)

    init {
        delegates.forEach {
            addDelegate(it)
        }
    }

    private fun addDelegate(delegate: AdapterDelegate<T>) {
        delegatesManager.add(delegate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager[getItemViewType(position)].onBindViewHolder(
            this.differ.currentList[position],
            position,
            holder,
            listOf()
        )
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>,
    ) {
        delegatesManager[getItemViewType(position)].onBindViewHolder(
            this.differ.currentList[position],
            position,
            holder,
            payloads
        )
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesManager.indexOfFirst { it.isForViewType(this.differ.currentList[position]) }
    }

    override fun getItemCount(): Int {
        return this.differ.currentList.size
    }

    fun setItems(items: List<T>) {
        this.differ.submitList(items)
    }
}
