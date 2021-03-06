package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ExpirationDatePower extends AbstractAnomalyTwoAmountPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:ExpirationDate";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public ExpirationDatePower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        this.priority = -1;
        updateDescription();
        loadRegion("time");
    }

    @Override
    public void updateDescription() {
        if (this.amount2 == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        if (power instanceof ExpirationDatePower && this.amount2 == ((ExpirationDatePower) power).amount2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        changeAmount2(-1);
        if (this.amount2 == 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    private void changeAmount2(int amount) {
        this.amount2 += amount;
        this.updateDescription();
    }
}
