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
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AggressorSyntaxPower extends AbstractAnomalyTwoAmountPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:AggressorSyntax";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private int cardSetupAmount;
    private boolean isActive = false;

    public AggressorSyntaxPower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.cardSetupAmount = this.amount2 = amount2;
        updateDescription();
        loadRegion("doubleDamage");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.cardSetupAmount + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (!this.isActive) {
                this.amount2--;
                if (this.amount2 == 0) {
                    flash();
                    this.isActive = true;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
                }
            }
        } else {
            if (this.isActive) {
                flash();
                this.isActive = false;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, -this.amount), -this.amount));
            }
            this.amount2 = this.cardSetupAmount;
        }
    }
}
