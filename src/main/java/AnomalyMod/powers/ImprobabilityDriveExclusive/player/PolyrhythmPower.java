package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.actions.correction.PolyrhythmPowerCorrectionAction;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PolyrhythmPower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:Polyrhythm";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    public boolean shouldDoDoubleDamage = false;
    public boolean shouldGainDoubleBlock = false;

    public PolyrhythmPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = 7;
        this.amount2 = 8;
        this.priority = 109;
        updateDescription();
        loadRegion("beat");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount--;
        this.amount2--;
        if (this.amount == 1) {
            flashWithoutSound();
        }
        else if (this.amount == 0) {
            flash();
            this.amount = 7;
        }
        if (this.amount2 == 1) {
            flashWithoutSound();
        }
        else if (this.amount2 == 0) {
            flash();
            this.amount2 = 8;
        }
        AbstractDungeon.actionManager.addToBottom(new PolyrhythmPowerCorrectionAction(this));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.shouldDoDoubleDamage) {
            return damage * 2.0F;
        }
        else {
            return damage;
        }
    }

    @Override
    public final float modifyBlock(float block) {
        if (this.shouldGainDoubleBlock) {
            return block * 2.0F;
        }
        else {
            return block;
        }
    }
}
