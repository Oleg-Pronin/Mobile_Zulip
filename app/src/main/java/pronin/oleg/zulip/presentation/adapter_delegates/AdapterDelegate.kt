package pronin.oleg.zulip.presentation.adapter_delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.presentation.base.BaseItemUI

interface AdapterDelegate<in T : BaseItemUI> {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(
        item: T,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    )

    fun isForViewType(item: BaseItemUI): Boolean
}
