package AnomalyMod.actions.common.VanillaImprovements;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;

public class FasterExhaustSpecificCardAction extends ExhaustSpecificCardAction {

    public FasterExhaustSpecificCardAction(AbstractCard targetCard, CardGroup group) {
        super(targetCard, group);
        this.duration = Settings.ACTION_DUR_XFAST;
        ReflectionHacks.setPrivate(this, ExhaustSpecificCardAction.class, "startingDuration", Settings.ACTION_DUR_XFAST);
    }
}
