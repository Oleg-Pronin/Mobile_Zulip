package pronin.oleg.zulip.matchers

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import pronin.oleg.zulip.presentation.custom_view.message.MessageLayout

class MessageLayoutOwnType(
    private val isOwnMessage: Boolean,
) : BoundedMatcher<View, MessageLayout>(MessageLayout::class.java) {

    override fun describeTo(description: Description?) {
        description?.appendText("The MessageLayout parameter isOwnMessage is not equals to $isOwnMessage")
    }

    override fun matchesSafely(item: MessageLayout?): Boolean {

        return item?.isOwnMessage == isOwnMessage
    }
}
