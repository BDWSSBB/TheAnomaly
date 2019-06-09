package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.AnomalyMod;
import AnomalyMod.helpers.RandomBuff;
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

public class ImitatorSyntax extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:ImitatorSyntax";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private int cardIntervalAmount;
    private ImitatorSyntaxMode currentMode;

    public ImitatorSyntax(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.cardIntervalAmount = this.amount2 = amount2;
        this.currentMode = null;
        updateDescription();
        loadRegion("buffer");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardIntervalAmount + PlayerTurnEffect.getOrdinalNaming(this.cardIntervalAmount) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        if (this.currentMode == null) {
            this.description += DESCRIPTIONS[7];
        } else if (this.currentMode == ImitatorSyntaxMode.STRENGTH) {
            this.description += DESCRIPTIONS[5];
        } else if (this.currentMode == ImitatorSyntaxMode.DEXTERITY) {
            this.description += DESCRIPTIONS[6];
        } else {
            AnomalyMod.logger.info("WTF? What happened to Imitator Syntax?");
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.amount2--;
        if (this.amount2 == 0) {
            if (card.type == AbstractCard.CardType.SKILL) {
                if (this.currentMode == null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
                } else if (this.currentMode == ImitatorSyntaxMode.STRENGTH) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
                }
                this.currentMode = ImitatorSyntaxMode.DEXTERITY;
            } else {
                if (this.currentMode == null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
                } else if (this.currentMode == ImitatorSyntaxMode.DEXTERITY) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
                }
                this.currentMode = ImitatorSyntaxMode.STRENGTH;
            }
            this.amount2 = this.cardIntervalAmount;
            updateDescription();
        }
    }

    private enum ImitatorSyntaxMode {
        STRENGTH, DEXTERITY
    }
}
