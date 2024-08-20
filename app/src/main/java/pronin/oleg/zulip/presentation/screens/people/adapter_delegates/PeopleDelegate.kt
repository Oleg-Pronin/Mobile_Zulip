package pronin.oleg.zulip.presentation.screens.people.adapter_delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pronin.oleg.zulip.R
import pronin.oleg.zulip.databinding.ItemPeopleBinding
import pronin.oleg.zulip.domain.enums.StatusPresence
import pronin.oleg.zulip.presentation.adapter_delegates.AdapterDelegate
import pronin.oleg.zulip.presentation.base.BaseItemUI
import pronin.oleg.zulip.presentation.ui.UserUI

class PeopleDelegate(
    private val onClick: ((userId: Int) -> Unit)? = null,
) : AdapterDelegate<UserUI> {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            ItemPeopleBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )

    override fun onBindViewHolder(
        item: UserUI,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: List<Any>,
    ) {
        (holder as ViewHolder).bind(item)
    }

    override fun isForViewType(item: BaseItemUI): Boolean = item is UserUI

    class ViewHolder(
        private val binding: ItemPeopleBinding,
        private val onClick: ((userId: Int) -> Unit)? = null,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: UserUI) {
            binding.apply {
                root.setOnClickListener { onClick?.invoke(model.id) }

                setupIU(model)
            }
        }

        private fun ItemPeopleBinding.setupIU(model: UserUI) {
            Glide.with(this@ViewHolder.itemView)
                .asBitmap()
                .load(model.avatar)
                .error(R.drawable.ic_avatar)
                .into(avatarUser)

            fullNameUser.text = model.fullName
            emailUser.text = model.email

            statusUser.setBackgroundResource(
                when (model.status) {
                    StatusPresence.ACTIVE -> R.drawable.online_status_bg
                    StatusPresence.IDLE -> R.drawable.idle_status_bg
                    StatusPresence.OFFLINE -> R.drawable.offline_status_bg
                }
            )
        }
    }
}