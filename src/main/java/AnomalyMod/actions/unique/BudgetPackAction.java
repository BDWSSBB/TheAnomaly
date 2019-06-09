package AnomalyMod.actions.unique;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class BudgetPackAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int maxTotalCost;
    private int failsafeCounter;

    public BudgetPackAction(int maxTotalCost, int failsafeCounter) {
        this.player = AbstractDungeon.player;
        this.maxTotalCost = maxTotalCost;
        this.failsafeCounter = failsafeCounter;
    }

    public BudgetPackAction(int maxTotalCost) {
        this(maxTotalCost, 0);
    }

    public void update() {
        int totalCost = 0;
        for (AbstractCard c : this.player.hand.group) {
            if (c.freeToPlayOnce) {
                totalCost += 0;
            } else if (c.costForTurn == -1) {
                totalCost += EnergyPanel.getCurrentEnergy();
            } else if (c.costForTurn > 0) {
                totalCost += c.costForTurn;
            }
        }
        if (totalCost < this.maxTotalCost && this.player.hand.size() != BaseMod.MAX_HAND_SIZE && !this.player.hasPower(NoDrawPower.POWER_ID) && this.failsafeCounter < 100) {
            this.failsafeCounter++;
            AbstractDungeon.actionManager.addToTop(new BudgetPackAction(this.maxTotalCost, this.failsafeCounter));
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.player, 1));
        }
        this.isDone = true;
    }
}
