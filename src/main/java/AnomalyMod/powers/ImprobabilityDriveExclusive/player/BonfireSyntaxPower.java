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

public class BonfireSyntaxPower extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:BonfireSyntax";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private int cardSetupAmount;
    private boolean isActive = false;
    private AbstractCard.CardType lastCardType = null;

    public BonfireSyntaxPower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.cardSetupAmount = this.amount2 = amount2;
        updateDescription();
        loadRegion("infinitegreen");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardSetupAmount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4];
        if (this.lastCardType == null) {
            this.description += DESCRIPTIONS[11];
        } else {
            switch (this.lastCardType) {
                case ATTACK: {
                    this.description += DESCRIPTIONS[5];
                    break;
                }
                case SKILL: {
                    this.description += DESCRIPTIONS[6];
                    break;
                }
                case POWER: {
                    this.description += DESCRIPTIONS[7];
                    break;
                }
                case STATUS: {
                    this.description += DESCRIPTIONS[8];
                    break;
                }
                case CURSE: {
                    this.description += DESCRIPTIONS[9];
                    break;
                }
                default: {
                    this.description += DESCRIPTIONS[10];
                }
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != this.lastCardType) {
            if (!this.isActive) {
                flashWithoutSound();
                this.amount2--;
                if (this.amount2 == 0) {
                    switchActivity();
                }
            }
        } else {
            if (this.isActive) {
                switchActivity();
            }
            if (this.amount2 != this.cardSetupAmount) {
                flashWithoutSound();
                this.amount2 = this.cardSetupAmount;
            }
        }
        this.lastCardType = card.type;
        updateDescription();
    }

    private void switchActivity() {
        flash();
        if (this.isActive) {
            this.isActive = false;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, -this.amount), -this.amount));
        } else {
            this.isActive = true;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new DexterityPower(this.owner, this.amount), this.amount));
        }
    }
}
