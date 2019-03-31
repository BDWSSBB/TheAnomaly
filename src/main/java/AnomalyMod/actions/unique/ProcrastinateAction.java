package AnomalyMod.actions.unique;

import AnomalyMod.powers.ProcrastinatePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public class ProcrastinateAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:PurgeFromHandAction").TEXT;
    private AbstractPlayer player;

    public ProcrastinateAction() {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            else if (this.player.hand.size() == 1) {
                AbstractCard card = this.player.hand.getTopCard();
                AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
                this.player.hand.removeCard(card);
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new ProcrastinatePower(this.player, card)));
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new ProcrastinatePower(this.player, c)));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.player.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
}
