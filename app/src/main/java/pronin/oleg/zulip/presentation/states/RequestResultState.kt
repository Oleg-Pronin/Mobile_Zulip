package pronin.oleg.zulip.presentation.states

sealed interface RequestResultState<out R> {
    data class InProgress<out T>(val data: T) : RequestResultState<T>
    data class Success<out T>(val data: T) : RequestResultState<T>
    data class Error(val throwable: Throwable) : RequestResultState<Nothing>
}
