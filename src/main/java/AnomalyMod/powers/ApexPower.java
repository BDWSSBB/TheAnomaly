package AnomalyMod.powers;

import AnomalyMod.actions.correction.AddActionLaterAction;
import AnomalyMod.actions.correction.ApexPowerCorrectionAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ApexPower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:Apex";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int BASE_PRIORITY = 0;
    public boolean shouldDebuff = false;

    public ApexPower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID + amount2;
        this.name = NAME;
        this.type = AbstractPower.PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        this.priority = BASE_PRIORITY + amount2;
        updateDescription();
        loadRegion("rupture");
    }

    @Override
    public void updateDescription() {
        if (this.amount2 == 1) {
            this.description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
        else {
            this.description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
    }

    @Override
    public void onRemove() {
        if (this.shouldDebuff) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flashWithoutSound();
        changeAmount2(-1);
        if (this.amount2 == 0) { // Orange Pellets lost, so I had to delay.
            AbstractDungeon.actionManager.addToBottom(new AddActionLaterAction(new ApexPowerCorrectionAction(this), 1));
            AbstractDungeon.actionManager.addToBottom(new AddActionLaterAction(new RemoveSpecificPowerAction(this.owner, this.owner, this), 1));
        }
    }

    private void changeAmount2(int amount) {
        this.amount2 += amount;
        this.ID = POWER_ID + this.amount2;
        this.priority = BASE_PRIORITY + this.amount2;
        this.updateDescription();
    }
}
