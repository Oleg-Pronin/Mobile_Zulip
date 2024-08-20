package pronin.oleg.zulip.domain.use_cases

object ConstEventType {
    val UNREAD_MESSAGES = listOf("message", "update_message_flags")
    val CHAT = listOf("message", "reaction")
    val PRESENCE = listOf("presence")
}
