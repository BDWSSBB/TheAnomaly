package AnomalyMod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NormalizedPower extends AbstractAnomalyPower {

    public static final String POWER_ID = "anomalyMod:Normalized";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public NormalizedPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        loadRegion("forcefield");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public int afterFinalImprobabilityCalculations(int finalImprobabilityNumber) {
        if (finalImprobabilityNumber > 0) {
            int reduceAmount = Math.min(this.amount, finalImprobabilityNumber);
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this, reduceAmount));
            return finalImprobabilityNumber - reduceAmount;
        }
        else {
            return finalImprobabilityNumber;
        }
    }
}
