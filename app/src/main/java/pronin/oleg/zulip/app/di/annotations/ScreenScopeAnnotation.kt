package pronin.oleg.zulip.app.di.annotations

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ChannelScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ChatScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class CreateChatScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class PeopleScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class ProfileScope

@Scope
@Retention(AnnotationRetention.BINARY)
annotation class EmojiScope
