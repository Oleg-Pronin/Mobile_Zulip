package pronin.oleg.zulip.app.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pronin.oleg.zulip.data.interceptor.AuthorizationInterceptor
import pronin.oleg.zulip.data.network.api.ZulipChatApi
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(context: Context) = OkHttpClient.Builder()
        .apply {
            readTimeout(1, TimeUnit.MINUTES)

            addInterceptor(
                AuthorizationInterceptor()
            )

            addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.HEADERS)
                }
            )

            addInterceptor(
                ChuckerInterceptor.Builder(context)
                    .collector(ChuckerCollector(context))
                    .alwaysReadResponseBody(false)
                    .build()
            )
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        baseUrl: String,
        jsonSerializer: Json,
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                jsonSerializer.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideZulipChatApiService(retrofit: Retrofit): ZulipChatApi =
        retrofit.create(ZulipChatApi::class.java)
}
