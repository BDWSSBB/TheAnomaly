package AnomalyMod.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TelomereFlakeAction extends AbstractGameAction {

    public AbstractPlayer player;

    public TelomereFlakeAction() {
        this.player = AbstractDungeon.player;
    }

    public void update() {
        if (!this.player.drawPile.isEmpty()) {
            if (this.player.drawPile.size() == 1) {
                AbstractCard onlyCard = this.player.drawPile.getTopCard();
                if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                    this.player.createHandIsFullDialog();
                    this.player.drawPile.moveToDiscardPile(onlyCard);
                } else {
                    this.player.drawPile.moveToHand(onlyCard, this.player.drawPile);
                    if (onlyCard.canUpgrade()) {
                        onlyCard.upgrade();
                        onlyCard.superFlash();
                    }
                }
            } else {
                AbstractCard topCard = this.player.drawPile.getTopCard();
                AbstractCard bottomCard = this.player.drawPile.getBottomCard();
                if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                    this.player.createHandIsFullDialog();
                    this.player.drawPile.moveToDiscardPile(topCard);
                } else {
                    this.player.drawPile.moveToHand(topCard, this.player.drawPile);
                }
                if (this.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                    this.player.createHandIsFullDialog();
                    this.player.drawPile.moveToDiscardPile(bottomCard);
                } else {
                    this.player.drawPile.moveToHand(bottomCard, this.player.drawPile);
                }
                if (topCard.canUpgrade()) {
                    topCard.upgrade();
                    topCard.superFlash();
                }
                if (bottomCard.canUpgrade()) {
                    bottomCard.upgrade();
                    bottomCard.superFlash();
                }
            }
        }
        this.isDone = true;
    }
}
