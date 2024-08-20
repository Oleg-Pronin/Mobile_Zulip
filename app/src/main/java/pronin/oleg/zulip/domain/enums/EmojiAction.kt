package pronin.oleg.zulip.domain.enums

enum class EmojiAction {
    ADD, REMOVE, UNKNOWN;

    companion object {
        fun byCode(code: String?) = when(code) {
            "add" -> ADD
            "remove" -> REMOVE
            else -> UNKNOWN
        }
    }
}
