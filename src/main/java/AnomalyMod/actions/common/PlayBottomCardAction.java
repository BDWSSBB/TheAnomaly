package AnomalyMod.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayBottomCardAction extends AbstractGameAction {

    public PlayBottomCardAction() {
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
            this.isDone = true;
            return;
        }

        if (AbstractDungeon.player.drawPile.isEmpty()) {
            AbstractDungeon.actionManager.addToTop(new PlayBottomCardAction());
            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
            this.isDone = true;
            return;
        }

        if (!AbstractDungeon.player.drawPile.isEmpty()) {
            AbstractCard card = AbstractDungeon.player.drawPile.getBottomCard();
            AbstractDungeon.player.drawPile.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            AbstractDungeon.actionManager.addToTop(new PlayCardAndKeepExistingAction(card, false));
        }
        this.isDone = true;
    }
}
