package pronin.oleg.zulip.app

object Const {
    /* API */
    const val SLUG = "tinkoff-android-spring-2024"
    const val BASE_URL = "https://$SLUG.zulipchat.com/api/v1/"
    const val BASE_URL_LOCAL = "http://localhost:8080"
    const val USER_NAME = "pronin.o.b@yandex.ru"
    const val API_KEY = "XBE7YSTlsemao9RnpTPz4vMbtGKB1w4c"

    /* DB */
    const val MAX_COUNT_MESSAGES_IN_DB = 50
    const val BASE_COUNT_MESSAGES_IN_DB = 20
    const val COUNT_LOAD_MORE_MESSAGES_IN_DB = 21
}