package AnomalyMod.actions.unique;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import AnomalyMod.patches.ux.JailbreakRenderPreviewPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class JailbreakAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:JailbreakAction").TEXT;
    private AbstractPlayer player;
    private int improbReductionAmount;
    private ArrayList<AbstractCard> cantSelect = new ArrayList<>();

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
            } else {
                for (AbstractCard c : this.player.hand.group) {
                    if (!canSelect(c)) {
                        this.cantSelect.add(c);
                    }
                }
                if (this.cantSelect.size() == this.player.hand.size()) {
                    this.isDone = true;
                    return;
                } else if (this.cantSelect.size() == this.player.hand.size() - 1) {
                    for (AbstractCard c : this.player.hand.group) {
                        if (canSelect(c)) {
                            if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0) {
                                ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbReductionAmount, true);
                            } else if (c.canUpgrade()) {
                                c.upgrade();
                            }
                        }
                    }
                    this.isDone = true;
                    return;
                } else {
                    for (AbstractCard c : this.cantSelect) {
                        this.player.hand.removeCard(c);
                    }
                    JailbreakRenderPreviewPatch.HandCardSelectScreenPatch.doingForJailbreakHand = true;
                    JailbreakRenderPreviewPatch.HandCardSelectScreenPatch.jailbreakReduction = this.improbReductionAmount;
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false);
                    JailbreakRenderPreviewPatch.HandCardSelectScreenPatch.doingForJailbreakHand = false;
                    tickDuration();
                    return;
                }
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0) {
                    ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbReductionAmount, true);
                } else if (c.canUpgrade()) {
                    c.upgrade();
                }
                this.player.hand.addToTop(c);
            }
            for (AbstractCard c : this.cantSelect) {
                this.player.hand.addToTop(c);
            }
            CardPlayHelper.standardHandActionResetProtocol();
        }
        this.tickDuration();
    }

    private boolean canSelect(AbstractCard card) {
        if (card.canUpgrade() || (card instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) card).baseImprobabilityNumber > 0)) {
            return true;
        } else {
            return false;
        }
    }
}
