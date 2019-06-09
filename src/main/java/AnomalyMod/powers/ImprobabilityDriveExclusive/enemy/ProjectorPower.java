package AnomalyMod.powers.ImprobabilityDriveExclusive.enemy;

import AnomalyMod.AnomalyMod;
import AnomalyMod.helpers.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ProjectorPower extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:Projector";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public ProjectorPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount;
        this.priority = 21;
        updateDescription();
        loadRegion("rebound");
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (this.amount2 > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            flashWithoutSound();
            this.amount2--;
            if (this.amount2 == 0) {
                flash();
                updateThings();
            }
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if (this.amount2 == 0) {
            this.amount2 = amount;
            updateThings();
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.amount2 > 0) {
            return damage * 1.5F;
        } else {
            return damage;
        }
    }

    private void updateThings() {
        updateDescription();
        if (this.owner instanceof AbstractMonster) {
            ((AbstractMonster) this.owner).applyPowers();
        } else {
            AnomalyMod.logger.info("Hey what the hell are you doing putting this on other people? Get outta here.");
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, null, this));
        }
    }
}
