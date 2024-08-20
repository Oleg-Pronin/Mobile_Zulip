package pronin.oleg.zulip.app.di.modules

import dagger.Binds
import dagger.Module
import pronin.oleg.zulip.data.use_cases.messages.AddEmojiInMessageUseCaseImpl
import pronin.oleg.zulip.data.use_cases.messages.RemoveEmojiInMessageUseCaseImpl
import pronin.oleg.zulip.data.use_cases.messages.GetMessagesUseCaseImpl
import pronin.oleg.zulip.data.use_cases.messages.SendMessageUseCaseImpl
import pronin.oleg.zulip.domain.use_cases.messages.AddEmojiInMessageUseCase
import pronin.oleg.zulip.domain.use_cases.messages.RemoveEmojiFromMessageUseCase
import pronin.oleg.zulip.domain.use_cases.messages.GetMessagesUseCase
import pronin.oleg.zulip.domain.use_cases.messages.SendMessageUseCase

@Module
interface ChatModule {
    @Binds
    fun bindGetMessagesUseCase(getMessagesUseCaseImpl: GetMessagesUseCaseImpl): GetMessagesUseCase

    @Binds
    fun bindSendMessageUseCase(sendMessageUseCaseImpl: SendMessageUseCaseImpl): SendMessageUseCase

    @Binds
    fun bindAddEmojiInMessageUseCase(addEmojiInMessageUseCaseImpl: AddEmojiInMessageUseCaseImpl): AddEmojiInMessageUseCase

    @Binds
    fun bindDeleteEmojiFromMessageUseCase(removeEmojiInMessageUseCaseImpl: RemoveEmojiInMessageUseCaseImpl): RemoveEmojiFromMessageUseCase
}
