package pronin.oleg.zulip.mocks

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.matching.UrlPathPattern
import pronin.oleg.zulip.utils.AssetsUtils

class ChatMock(private val wireMockServer: WireMockServer) {
    private fun getMatcher(urlPattern: UrlPathPattern) =  WireMock.get(urlPattern)
    private fun postMatcher(urlPattern: UrlPathPattern) =  WireMock.post(urlPattern)

    fun getOwnUser() {
        wireMockServer.stubFor(
            getMatcher(urlPatternUserMe).willReturn(
                ok(AssetsUtils.fromAssets("ownUser.json"))
            )
        )
    }

    fun getMessages() {
        wireMockServer.stubFor(
            getMatcher(urlPatternMessages).willReturn(
                ok(AssetsUtils.fromAssets("messages.json"))
            )
        )
    }

    fun registerEvent() {
        wireMockServer.stubFor(
            postMatcher(urlPatternRegister).willReturn(
                ok(AssetsUtils.fromAssets("register.json"))
            )
        )
    }

    fun getEventsFromEventQueue() {
        wireMockServer.stubFor(
            getMatcher(urlPatternEvent).willReturn(
                ok(AssetsUtils.fromAssets("event.json"))
            )
        )
    }

    companion object {
        val urlPatternUserMe: UrlPathPattern = WireMock.urlPathMatching("/users/me")
        val urlPatternMessages: UrlPathPattern = WireMock.urlPathMatching("/messages")
        val urlPatternRegister: UrlPathPattern = WireMock.urlPathMatching("/register")
        val urlPatternEvent: UrlPathPattern = WireMock.urlPathMatching("/events")

        fun WireMockServer.chat(block: ChatMock.() -> Unit) {
            ChatMock(this).apply(block)
        }
    }
}
