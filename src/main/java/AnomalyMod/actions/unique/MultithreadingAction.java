package AnomalyMod.actions.unique;

import AnomalyMod.powers.MultithreadingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MultithreadingAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("ExhaustAction").TEXT;
    private AbstractPlayer player;
    private int powerAmount;

    public MultithreadingAction(int powerAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.powerAmount = powerAmount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.group.isEmpty()) {
                this.isDone = true;
                return;
            }
            else if (this.player.hand.size() == 1) {
                AbstractCard card = this.player.hand.getTopCard();
                AbstractDungeon.player.hand.moveToExhaustPile(card);
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new MultithreadingPower(this.player, this.powerAmount, card)));
                this.isDone = true;
                return;
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                AbstractDungeon.player.hand.moveToExhaustPile(c);
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new MultithreadingPower(this.player, this.powerAmount, c)));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.player.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
}
