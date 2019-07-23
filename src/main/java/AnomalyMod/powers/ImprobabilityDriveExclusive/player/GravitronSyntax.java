package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.helpers.interfaces.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;

public class GravitronSyntax extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:GravitronSyntax";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private int cardIntervalAmount;
    private boolean isOn;

    public GravitronSyntax(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.cardIntervalAmount = this.amount2 = amount2;
        this.isOn = false;
        updateDescription();
        loadRegion("buffer");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardIntervalAmount + PlayerTurnEffect.getOrdinalNaming(this.cardIntervalAmount) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        if (this.isOn) {
            this.description += DESCRIPTIONS[5];
        } else {
            this.description += DESCRIPTIONS[6];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount2--;
        if (this.amount2 == 0) {
            if (this.isOn) {
                this.isOn = false;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
            } else {
                this.isOn = true;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
            }
            this.amount2 = this.cardIntervalAmount;
            updateDescription();
        }
    }
}
