package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.helpers.interfaces.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ViscosityPower extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:Viscosity";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public ViscosityPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = this.amount2 = amount;
        updateDescription();
        loadRegion("brutality");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount2 += stackAmount;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && this.amount2 > 0) {
            flash();
            if (damageAmount >= this.amount2) {
                int newDamage = damageAmount - this.amount2;
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new ViscosityDamagePower(this.owner, this.amount2), this.amount2));
                this.amount2 = 0;
                return newDamage;
            } else {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new ViscosityDamagePower(this.owner, damageAmount), damageAmount));
                this.amount2 -= damageAmount;
                return 0;
            }
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if (this.amount2 != this.amount) {
            flash();
            this.amount2 = this.amount;
        }
    }
}
