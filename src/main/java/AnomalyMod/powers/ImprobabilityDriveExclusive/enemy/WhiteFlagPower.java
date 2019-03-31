package AnomalyMod.powers.ImprobabilityDriveExclusive.enemy;

import AnomalyMod.actions.correction.WhiteFlagPowerCorrectionAction;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WhiteFlagPower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:WhiteFlag";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int HITS_PER_DENIAL = 6;
    public boolean shouldTakeNoDamage = false;

    public WhiteFlagPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = 0;
        this.amount2 = HITS_PER_DENIAL;
        this.priority = 99;
        updateDescription();
        loadRegion("flight");
    }

    @Override
    public void updateDescription() {
        if (this.amount > 1) {
            this.description = DESCRIPTIONS[0] + HITS_PER_DENIAL + DESCRIPTIONS[1] + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
        else if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + HITS_PER_DENIAL + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        }
        else {
            this.description = DESCRIPTIONS[0] + HITS_PER_DENIAL + DESCRIPTIONS[1];
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0f;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && this.amount > 0) {
            flash();
            this.amount--;
            updateDescription();
        }
        AbstractDungeon.actionManager.addToBottom(new WhiteFlagPowerCorrectionAction(this));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL && info.owner != null && info.owner != this.owner) {
            flashWithoutSound();
            this.amount2--;
            if (this.amount2 == 0) {
                flash();
                this.amount2 = HITS_PER_DENIAL;
                this.amount++;
                updateDescription();
            }
        }
        return damageAmount;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.shouldTakeNoDamage) {
            return 0.0F;
        }
        else {
            return damage;
        }
    }
}
