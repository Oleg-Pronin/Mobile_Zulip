package pronin.oleg.zulip.presentation.screens.chat

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.tomakehurst.wiremock.client.WireMock
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pronin.oleg.zulip.R
import pronin.oleg.zulip.domain.models.streams.StreamDomain
import pronin.oleg.zulip.mocks.ChatMock
import pronin.oleg.zulip.mocks.ChatMock.Companion.chat
import pronin.oleg.zulip.rule.AppTestRule

@RunWith(AndroidJUnit4::class)
class ChatFragmentTest : TestCase() {

    @get:Rule
    val rule = AppTestRule()

    @Before
    fun setup() {
        FragmentScenario.launchInContainer(
            ChatFragment::class.java,
            ChatFragment.getBundle(
                StreamDomain(
                    id = 434442,
                    name = "test test test",
                    color = "#F5CE6E",
                    isSubscribed = false,
                    pinToTop = false,
                    weeklyTraffic = 0
                ),
                "События канала",
                438330086
            ),
            themeResId = R.style.Theme_Homework
        )

        rule.wiremockRule.chat {
            getOwnUser()
            getMessages()
            registerEvent()
            getEventsFromEventQueue()
        }
    }

    @Test
    fun testingMethodCalls() = run {
        ChatFragmentScreen {
            step("We check that the 'getOwnUser()' method has been called") {
                WireMock.verify(WireMock.getRequestedFor(ChatMock.urlPatternUserMe))
            }

            step("We check that the 'getMessages()' method has been called") {
                WireMock.verify(WireMock.getRequestedFor(ChatMock.urlPatternMessages))
            }

            step("We check that the 'registerEvent()' method has been called") {
                WireMock.verify(WireMock.postRequestedFor(ChatMock.urlPatternRegister))
            }

            step("We check that the 'getEventsFromEventQueue()' method has been called") {
                WireMock.verify(WireMock.getRequestedFor(ChatMock.urlPatternEvent))
            }
        }
    }

    @Test
    fun testingDisplayInRecyclerView() = run {
        ChatFragmentScreen {
            step("Checking the display of the Toolbar") {
                toolbar {
                    isVisible()
                }
            }

            step("Checking the display in RecyclerView") {
                flakySafely {
                    messageRV {
                        step("Checking that the date is displayed correctly") {
                            childWith<ChatFragmentScreen.KDateItem> {
                                withId(R.id.itemDateMessage)
                            } perform  {
                                dateText.isVisible()
                                dateText.hasAnyText()
                            }
                        }

                        step("Checking that the message is displayed correctly") {
                            childWith<ChatFragmentScreen.KMessageItem> {
                                withId(R.id.itemMessage)
                                isFirst()
                            }.apply {
                                messageLayout.hasOwnMessage(false)
                            }
                        }

                        step("Checking the correct display of a personal message") {
                            childWith<ChatFragmentScreen.KOwnMessageItem> {
                                withId(R.id.itemOwnMessage)
                                isFirst()
                            }.apply {
                                messageLayout.hasOwnMessage(true)
                            }
                        }
                    }
                }
            }
        }
    }
}
