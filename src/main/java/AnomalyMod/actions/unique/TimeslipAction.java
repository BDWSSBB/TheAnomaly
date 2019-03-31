package AnomalyMod.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TimeslipAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int numberOfCards;

    public TimeslipAction(int numberOfCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    @Override
    public void update() {
        for (int i = 0; i < this.numberOfCards; i++) {
            if (!this.player.drawPile.isEmpty() && this.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                this.player.drawPile.moveToHand(this.player.drawPile.getBottomCard(), this.player.drawPile);
            }
        }
        this.isDone = true;
    }
}
