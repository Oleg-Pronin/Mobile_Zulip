package pronin.oleg.zulip.domain.use_cases.messages

interface SendMessageUseCase {
    suspend operator fun invoke(streamId: Int, topicName: String, content: String): Int
}
