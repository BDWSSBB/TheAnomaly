package AnomalyMod.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ReverseDrawPileAction extends AbstractGameAction {

    public AbstractPlayer player;

    public ReverseDrawPileAction() {
        this.player = AbstractDungeon.player;
    }

    public void update() {
        ArrayList<AbstractCard> cardsToMove = new ArrayList<>();
        for (AbstractCard c : this.player.drawPile.group) {
            cardsToMove.add(c);
        }
        for (AbstractCard c : cardsToMove) {
            this.player.drawPile.removeCard(c);
        }
        for (AbstractCard c : cardsToMove) {
            this.player.drawPile.addToBottom(c);
        }
        this.isDone = true;
    }
}
