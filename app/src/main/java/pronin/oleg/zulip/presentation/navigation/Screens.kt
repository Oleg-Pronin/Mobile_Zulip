package pronin.oleg.zulip.presentation.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.presentation.screens.channels.ChannelsFragment
import pronin.oleg.zulip.presentation.screens.chat.ChatFragment
import pronin.oleg.zulip.presentation.screens.people.PeopleFragment
import pronin.oleg.zulip.presentation.screens.profile.ProfileFragment

object Screens {
    fun Channels() = FragmentScreen {
        ChannelsFragment.getInstance()
    }

    fun Chat(stream: StreamDomain, topicName: String, lastMessageId: Int) = FragmentScreen {
        ChatFragment.getInstance(stream, topicName, lastMessageId)
    }

    fun People() = FragmentScreen {
        PeopleFragment.getInstance()
    }

    fun Profile(userId: Int? = null) = FragmentScreen {
        ProfileFragment.getInstance(userId)
    }
}
