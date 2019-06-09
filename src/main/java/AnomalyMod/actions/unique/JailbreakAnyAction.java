package AnomalyMod.actions.unique;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.ux.JailbreakRenderPreviewPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class JailbreakAnyAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:JailbreakAnyAction").TEXT;
    private AbstractPlayer player;
    private int improbReductionAmount;

    public JailbreakAnyAction(int improbReductionAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.improbReductionAmount = improbReductionAmount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.player.drawPile.group) {
                if (canSelect(c)) {
                    temp.addToTop(c);
                }
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            for (AbstractCard c : this.player.hand.group) {
                if (canSelect(c)) {
                    c.angle = 0.0F;
                    temp.addToTop(c);
                    c.beginGlowing();
                }
            }
            for (AbstractCard c : this.player.discardPile.group) {
                if (canSelect(c)) {
                    temp.addToTop(c);
                }
            }
            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            } else if (temp.size() == 1) {
                for (AbstractCard c : temp.group) {
                    if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0) {
                        ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbReductionAmount, true);
                    } else if (c.canUpgrade()) {
                        c.upgrade();
                    }
                }
            } else {
                JailbreakRenderPreviewPatch.GridCardSelectScreenPatch.doingForJailbreakGrid = true;
                JailbreakRenderPreviewPatch.GridCardSelectScreenPatch.jailbreakReduction = improbReductionAmount;
                AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], true);
                JailbreakRenderPreviewPatch.GridCardSelectScreenPatch.doingForJailbreakGrid = false;
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0) {
                    ((AbstractAnomalyCard) c).changeImprobabilityNumber(-this.improbReductionAmount, true);
                } else if (c.canUpgrade()) {
                    c.upgrade();
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.player.hand.refreshHandLayout();
            this.player.hand.glowCheck();
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
