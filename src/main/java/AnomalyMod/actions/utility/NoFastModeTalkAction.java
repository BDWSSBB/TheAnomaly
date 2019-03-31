package AnomalyMod.actions.utility;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class NoFastModeTalkAction extends TalkAction {

    public NoFastModeTalkAction(AbstractCreature source, final String text, final float duration, final float bubbleDuration) {
        super(source, text, duration, bubbleDuration);
        this.duration = duration;
    }
}
