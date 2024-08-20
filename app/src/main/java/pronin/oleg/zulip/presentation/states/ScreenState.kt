package pronin.oleg.zulip.presentation.states

sealed interface ScreenState<out R> {
    data object Init : ScreenState<Nothing>
    data object Loading : ScreenState<Nothing>
    data class Content<out T>(val content: T) : ScreenState<T>
    data class Error(val throwable: Throwable) : ScreenState<Nothing>
}
