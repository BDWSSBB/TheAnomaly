package AnomalyMod.actions.unique;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class JailbreakAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:JailbreakAction").TEXT;
    private AbstractPlayer player;
    private int improbReductionAmount;

    public JailbreakAction(int improbReductionAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.improbReductionAmount = improbReductionAmount;
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
                if (card.canUpgrade()) {
                    card.upgrade();
                }
                if (card instanceof AbstractAnomalyCard) {
                    ((AbstractAnomalyCard) card).changeImprobabilityNumber(-this.improbReductionAmount, true);
                }
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c.canUpgrade()) {
                    c.upgrade();
                }
                if (c instanceof AbstractAnomalyCard) {
                    ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbReductionAmount, true);
                }
                this.player.hand.addToTop(c);
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.player.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
}
