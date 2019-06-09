package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SmoothSailingPower extends AbstractAnomalyTwoAmountPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:SmoothSailing";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public SmoothSailingPower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        this.priority = -5;
        updateDescription();
        loadRegion("energized_blue");
    }

    @Override
    public void updateDescription() {
        if (this.amount2 == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + this.amount2 + DESCRIPTIONS[3];
        }
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        if (power instanceof SmoothSailingPower && this.amount2 == ((SmoothSailingPower) power).amount2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void atStartOfTurn() {
        changeAmount2(-1);
        if (this.amount2 == 0) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    private void changeAmount2(int amount) {
        this.amount2 += amount;
        this.updateDescription();
    }
}
