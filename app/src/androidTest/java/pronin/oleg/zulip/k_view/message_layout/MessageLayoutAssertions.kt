package pronin.oleg.zulip.k_view.message_layout

import androidx.test.espresso.assertion.ViewAssertions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import pronin.oleg.zulip.matchers.MessageLayoutOwnType

interface MessageLayoutAssertions : BaseAssertions {
    fun hasOwnMessage(value: Boolean) {
        view.check(ViewAssertions.matches(MessageLayoutOwnType(value)))
    }
}
