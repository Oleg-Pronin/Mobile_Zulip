package pronin.oleg.zulip.presentation.screens.channels.adapter_delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.databinding.ItemTopicBinding
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI
import pronin.oleg.zulip.presentation.ui.ChannelUI

class TopicDelegate(
    private val onClick: ((streamId: Int, topicName: String, lastMessageId: Int) -> Unit)? = null,
) : AdapterDelegate<ChannelUI> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )

    override fun onBindViewHolder(
        item: ChannelUI,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    ) {
        (holder as ViewHolder).bind(item as ChannelUI.TopicUI)
    }

    override fun isForViewType(item: BaseItemUI): Boolean = item is ChannelUI.TopicUI

    class ViewHolder(
        private val binding: ItemTopicBinding,
        private val onClick: ((streamId: Int, topicName: String, lastMessageId: Int) -> Unit)? = null,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: ChannelUI.TopicUI) {
            binding.apply {
                root.setOnClickListener {
                    onClick?.invoke(
                        model.parentId,
                        model.name,
                        model.lastMessageId
                    )
                }

                setupUI(model)
            }
        }

        private fun ItemTopicBinding.setupUI(model: ChannelUI.TopicUI) {
            model.color?.let {
                root.setBackgroundColor(it.toArgb())
            }

            nameTopic.text = model.name
            count.text = model.count.toString()
        }
    }
}
