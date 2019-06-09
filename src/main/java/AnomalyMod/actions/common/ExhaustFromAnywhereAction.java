package AnomalyMod.actions.common;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.common.VanillaImprovements.FasterExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustFromAnywhereAction extends AbstractGameAction {

    private AbstractCard card;

    public ExhaustFromAnywhereAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.contains(this.card)) {
            AbstractDungeon.actionManager.addToTop(new FasterExhaustSpecificCardAction(this.card, AbstractDungeon.player.drawPile));
        } else if (AbstractDungeon.player.hand.contains(this.card)) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(this.card, AbstractDungeon.player.hand));
        } else if (AbstractDungeon.player.discardPile.contains(this.card)) {
            AbstractDungeon.actionManager.addToTop(new FasterExhaustSpecificCardAction(this.card, AbstractDungeon.player.discardPile));
        } else {
            AnomalyMod.logger.info("WARNING: Couldn't find card to exhaust.");
        }
        this.isDone = true;
    }
}
