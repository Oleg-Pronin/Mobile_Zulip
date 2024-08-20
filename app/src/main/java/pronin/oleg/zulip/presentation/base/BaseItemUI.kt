package pronin.oleg.zulip.presentation.base

interface BaseItemUI {
    val id: Int

    fun areSame(newItem: BaseItemUI): Boolean =
        this.javaClass == newItem.javaClass && id == newItem.id

    fun areContentsTheSame(newItem: BaseItemUI): Boolean =
        this == newItem
}
