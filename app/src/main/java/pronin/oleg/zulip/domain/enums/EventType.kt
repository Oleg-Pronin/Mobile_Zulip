package pronin.oleg.zulip.domain.enums

enum class EventType {
    MESSAGE, REACTION, UNKNOWN;

    companion object {
        fun byCode(code: String?) = when(code) {
            "message" -> MESSAGE
            "reaction" -> REACTION
            else -> UNKNOWN
        }
    }
}
