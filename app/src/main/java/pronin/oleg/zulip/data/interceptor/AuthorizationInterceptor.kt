package pronin.oleg.zulip.data.interceptor

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import pronin.oleg.zulip.app.Const

class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().header(
                "Authorization",
                Credentials.basic(Const.USER_NAME, Const.API_KEY)
            ).build()
        )
    }
}
