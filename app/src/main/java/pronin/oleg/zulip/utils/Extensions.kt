package pronin.oleg.zulip.utils

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import okhttp3.internal.toHexString
import java.io.IOException
import java.io.Serializable
import java.time.format.TextStyle
import java.util.Locale
import java.util.concurrent.CancellationException


fun LocalDate.getFormattedDateOfMessage() = "${this.dayOfMonth} ${
    this.month.getDisplayName(
        TextStyle.SHORT,
        Locale.getDefault()
    )?.lowercase()
}"

fun <T> lazyUnsafe(block: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, block)

inline fun <R> runCatchingNonCancellation(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        throw e
    }
}

suspend fun <T1, T2, R> asyncAwait(
    s1: suspend CoroutineScope.() -> T1,
    s2: suspend CoroutineScope.() -> T2,
    transform: suspend (T1, T2) -> R,
): R {
    return coroutineScope {
        val result1 = async(block = s1)
        val result2 = async(block = s2)

        transform(
            result1.await(),
            result2.await()
        )
    }
}

fun Long.toLocalDateTime(): LocalDateTime {
    val instant: Instant = Instant.fromEpochMilliseconds(this * DateUtils.SECOND_IN_MILLIS)
    return instant.toLocalDateTime(TimeZone.UTC)
}

fun LocalDateTime.toLong(): Long {
    return this.toInstant(TimeZone.UTC).toEpochMilliseconds() / DateUtils.SECOND_IN_MILLIS
}

fun getEmojiByUnicode(reactionCode: String): String {
    return if (reactionCode.isNotEmpty())
        String(Character.toChars(reactionCode.toInt(16)))
    else
        ""
}

fun getUnicodeByEmoji(emojiCode: String): String {
    return if (emojiCode.isNotEmpty()) {
        val chars = emojiCode.toCharArray()

        Character.toCodePoint(chars.first(), chars.last()).toHexString()
    } else
        ""
}

fun getJsonDataFromAsset(context: Context, fileName: String): String {
    var jsonString = ""

    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
    }

    return jsonString
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)

    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) = setOnClickListener(
    SafeClickListener(SafeClickListener.DEFAULT_INTERVAL) {
        onSafeClick(it)
    }
)
