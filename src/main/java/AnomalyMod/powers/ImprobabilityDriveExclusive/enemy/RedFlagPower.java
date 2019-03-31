package AnomalyMod.powers.ImprobabilityDriveExclusive.enemy;

import AnomalyMod.AnomalyMod;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class RedFlagPower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:RedFlag";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int ATTACKS_PER_ANGERY = 5;
    private boolean attackedThisCard = false;

    public RedFlagPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = 0;
        this.amount2 = ATTACKS_PER_ANGERY;
        this.priority = 21;
        updateDescription();
        loadRegion("nightmare");
    }

    @Override
    public void updateDescription() {
        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + ATTACKS_PER_ANGERY + DESCRIPTIONS[1] + DESCRIPTIONS[2] + this.amount * 50 + DESCRIPTIONS[3];
        }
        else {
            this.description = DESCRIPTIONS[0] + ATTACKS_PER_ANGERY + DESCRIPTIONS[1];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.attackedThisCard = false;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!this.attackedThisCard && damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            flashWithoutSound();
            this.attackedThisCard = true;
            this.amount2--;
            if (this.amount2 == 0) {
                flash();
                this.amount2 = ATTACKS_PER_ANGERY;
                this.amount++;
                updateThings();
            }
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        this.amount = 0;
        updateThings();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.amount > 0) {
            return damage * (1.0F + (float) this.amount * 0.5F);
        }
        else {
            return damage;
        }
    }

    private void updateThings() {
        updateDescription();
        if (this.owner instanceof AbstractMonster) {
            ((AbstractMonster) this.owner).applyPowers();
        }
        else {
            AnomalyMod.logger.info("Hey what the hell are you doing putting this on other people? Get outta here.");
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, null, this));
        }
    }
}
