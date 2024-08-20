package pronin.oleg.zulip.presentation.diff_utils

import androidx.recyclerview.widget.DiffUtil
import pronin.oleg.zulip.presentation.base.BaseItemUI

class DelegateAdapterItemCallback<T : BaseItemUI> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areSame(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }
}
