package pronin.oleg.zulip.presentation.screens.channels.adapter_delegates

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.ItemStreamBinding
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI
import pronin.oleg.zulip.presentation.ui.ChannelUI

class StreamDelegate(
    private val onLoadTopics: ((streamId: Int, streamColor: Color?) -> Unit)? = null,
    private val onHideTopics: ((streamId: Int) -> Unit)? = null,
) : AdapterDelegate<ChannelUI> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onLoadTopics,
            onHideTopics
        )

    override fun onBindViewHolder(
        item: ChannelUI,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    ) {
        (holder as ViewHolder).bind(item as ChannelUI.StreamUI)
    }

    override fun isForViewType(item: BaseItemUI): Boolean = item is ChannelUI.StreamUI

    class ViewHolder(
        private val binding: ItemStreamBinding,
        private val onLoadTopics: ((streamId: Int, streamColor: Color?) -> Unit)? = null,
        private val onHideTopics: ((streamId: Int) -> Unit)? = null,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChannelUI.StreamUI) {
            binding.apply {
                if (item.noClickDownloadTopics) {
                    root.setOnClickListener(null)
                } else {
                    root.setOnClickListener {
                        if (!item.isOpened)
                            onLoadTopics?.invoke(item.id, item.color)
                        else
                            onHideTopics?.invoke(item.id)
                    }
                }

                setupUI(
                    model = item,
                    isLoadingTopic = item.isLoading,
                    isOpenStream = item.isOpened
                )
            }
        }

        private fun ItemStreamBinding.setupUI(
            model: ChannelUI.StreamUI,
            isLoadingTopic: Boolean = false,
            isOpenStream: Boolean = false,
        ) {
            hashtagStream.apply {
                setImageResource(
                    if (model.isPrivate)
                        R.drawable.ic_lock
                    else
                        R.drawable.ic_hashtag
                )

                imageTintList = ColorStateList.valueOf(
                    if (model.color != null)
                        model.color.toArgb()
                    else
                        root.context.getColor(R.color.sea_green)
                )
            }

            nameStream.text = model.name

            if (isLoadingTopic) {
                loadingProgress.isVisible = true
                arrowStream.isInvisible = true
            } else {
                loadingProgress.isVisible = false

                if (model.noClickDownloadTopics)
                    arrowStream.isVisible = false
                else
                    arrowStream.apply {
                        isInvisible = false

                        setImageResource(
                            if (isOpenStream)
                                R.drawable.ic_arrow_up
                            else
                                R.drawable.ic_arrow_down
                        )
                    }
            }
        }
    }
}
