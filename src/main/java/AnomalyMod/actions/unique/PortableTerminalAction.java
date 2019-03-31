package AnomalyMod.actions.unique;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.relics.ControlHijack;
import AnomalyMod.relics.AuditingHijack;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PortableTerminalAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("ForethoughtAction").TEXT;
    private AbstractPlayer player;
    private int improbabilityReductionAmount;

    public PortableTerminalAction(int improbabilityReductionAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.improbabilityReductionAmount = improbabilityReductionAmount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 99, true, true);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved && !AbstractDungeon.handCardSelectScreen.selectedCards.group.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c instanceof AbstractAnomalyCard) {
                    if (AbstractDungeon.player.hasRelic(AuditingHijack.ID) && AbstractDungeon.handCardSelectScreen.selectedCards.group.size() == 1) {
                        ((AbstractAnomalyCard) c).changeImprobabilityNumber(-AuditingHijack.AUDITING_HIJACK_IMPROBABILITY_LOSS, true);
                    }
                    else if (AbstractDungeon.player.hasRelic(ControlHijack.ID) && AbstractDungeon.handCardSelectScreen.selectedCards.group.size() == 1) {
                        ((AbstractAnomalyCard) c).changeImprobabilityNumber(-ControlHijack.CONTROL_HIJACK_IMPROBABILITY_LOSS, true);
                        AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.player, 1));
                    }
                    else {
                        ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbabilityReductionAmount, true);
                    }
                }
                if (AbstractDungeon.player.hasRelic(AuditingHijack.ID) && AbstractDungeon.handCardSelectScreen.selectedCards.group.size() == 1) {
                    this.player.hand.addToTop(c);
                }
                else {
                    this.player.hand.moveToBottomOfDeck(c);
                }
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.player.hand.refreshHandLayout();
        }
        this.tickDuration();
    }
}
