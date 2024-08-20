package pronin.oleg.zulip.presentation.screens.chat.adapter_delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.databinding.ItemDateMessageBinding
import pronin.oleg.zulip.presentation.ui.ChatUI
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI
import pronin.oleg.zulip.utils.getFormattedDateOfMessage

class DateAdapterDelegate : AdapterDelegate<ChatUI> {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemDateMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        item: ChatUI,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    ) {
        (holder as ViewHolder).bind(item as ChatUI.DateUI)
    }

    override fun isForViewType(item: BaseItemUI): Boolean = item is ChatUI.DateUI

    class ViewHolder(private val binding: ItemDateMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dateMessage: ChatUI.DateUI) {
            binding.date.text = dateMessage.date.getFormattedDateOfMessage()
        }
    }
}