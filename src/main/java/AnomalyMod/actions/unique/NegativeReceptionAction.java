package AnomalyMod.actions.unique;

import AnomalyMod.actions.common.InstantDiscardSpecificCardAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class NegativeReceptionAction extends AbstractGameAction {

    private AbstractPlayer player;

    public NegativeReceptionAction() {
        this.player = AbstractDungeon.player;
    }

    public void update() {
        int cardsDiscarded = 0;
        for (AbstractCard c : this.player.hand.group) {
            if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE || (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0)) {
                cardsDiscarded++;
            }
        }
        if (cardsDiscarded > 0) {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.player, cardsDiscarded));
        }
        for (AbstractCard c : this.player.hand.group) {
            if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE || (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0)) {
                AbstractDungeon.actionManager.addToTop(new InstantDiscardSpecificCardAction(c));
            }
        }
        this.isDone = true;
    }
}
