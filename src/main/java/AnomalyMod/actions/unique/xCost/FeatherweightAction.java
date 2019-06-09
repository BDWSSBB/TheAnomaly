package AnomalyMod.actions.unique.xCost;

import AnomalyMod.powers.SmoothSailingPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class FeatherweightAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    private AbstractPlayer player;
    private int energyPerDiscard;
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public FeatherweightAction(int energyPerDiscard, boolean freeToPlayOnce, int energyOnUse) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.energyPerDiscard = energyPerDiscard;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            int effect = EnergyPanel.totalCount;
            if (this.energyOnUse != -1) {
                effect = this.energyOnUse;
            }
            if (this.player.hasRelic(ChemicalX.ID)) {
                effect += 2;
                this.player.getRelic(ChemicalX.ID).flash();
            }
            if (!this.freeToPlayOnce) {
                this.player.energy.use(EnergyPanel.totalCount);
            }
            if (effect > 0 && !this.player.hand.isEmpty()) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], effect, true, true);
                tickDuration();
                return;
            } else {
                this.isDone = true;
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            int numberOfCardsDiscarded = 0;
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                numberOfCardsDiscarded++;
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            if (numberOfCardsDiscarded > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new SmoothSailingPower(this.player, numberOfCardsDiscarded * this.energyPerDiscard, 1), numberOfCardsDiscarded * this.energyPerDiscard));
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
