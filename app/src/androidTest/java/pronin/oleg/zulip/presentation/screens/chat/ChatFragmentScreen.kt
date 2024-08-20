package pronin.oleg.zulip.presentation.screens.chat

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import org.hamcrest.Matcher
import pronin.oleg.zulip.R
import pronin.oleg.zulip.k_view.message_layout.KMessageLayout

object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val toolbar = KToolbar { withId(R.id.toolbarChat) }

    val messageRV = KRecyclerView(
        builder = { withId(R.id.messageRV) },
        itemTypeBuilder = {
            itemType(::KDateItem)
            itemType(::KMessageItem)
            itemType(::KOwnMessageItem)
        }
    )

    class KDateItem(parent: Matcher<View>) : KRecyclerItem<KDateItem>(parent) {
        val dateText = KTextView { withId(R.id.date) }
    }

    class KMessageItem(parent: Matcher<View>) : KRecyclerItem<KMessageItem>(parent) {
        val messageLayout = KMessageLayout(parent) { withId(R.id.messageLayout) }
    }

    class KOwnMessageItem(parent: Matcher<View>) : KRecyclerItem<KOwnMessageItem>(parent) {
        val messageLayout = KMessageLayout(parent) { withId(R.id.messageLayout) }
    }
}
