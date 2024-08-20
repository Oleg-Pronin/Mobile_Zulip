package pronin.oleg.zulip.app.di.components

import dagger.Subcomponent
import pronin.oleg.zulip.app.di.annotations.EmojiScope
import pronin.oleg.zulip.app.di.modules.EmojiModule
import pronin.oleg.zulip.presentation.dialogs.emoji.EmojiDialogFragment

@EmojiScope
@Subcomponent(modules = [EmojiModule::class])
interface EmojiComponent {
    fun inject(emojiDialogFragment: EmojiDialogFragment)
}
