package pronin.oleg.zulip.presentation.states

interface ListState<T> {
    val items: List<T>
    val screenState: ScreenState<List<T>>
}
