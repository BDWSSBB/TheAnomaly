package AnomalyMod.actions.common;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class InstantDiscardSpecificCardAction extends DiscardSpecificCardAction {

    public InstantDiscardSpecificCardAction(AbstractCard card) {
        super(card);
    }

    public void update() {
        super.update();
        this.isDone = true;
    }
}
