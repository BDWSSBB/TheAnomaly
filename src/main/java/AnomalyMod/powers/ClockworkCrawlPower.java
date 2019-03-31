package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ClockworkCrawlPower extends AbstractAnomalyTwoAmountPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:ClockworkCrawl";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private int cardInterval;

    public ClockworkCrawlPower(AbstractCreature owner, int amount, int amount2) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = this.cardInterval = amount2;
        updateDescription();
        loadRegion("artifact");
        this.priority = -6;
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            if (this.amount2 == 1) {
                this.description = DESCRIPTIONS[3] + this.amount2 + DESCRIPTIONS[4];
            }
            else {
                this.description = DESCRIPTIONS[3] + this.amount2 + DESCRIPTIONS[5];
            }
        }
        else {
            if (this.amount2 == 1) {
                this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.cardInterval + DESCRIPTIONS[2] + DESCRIPTIONS[3] + this.amount2 + DESCRIPTIONS[4];
            }
            else {
                this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.cardInterval + DESCRIPTIONS[2] + DESCRIPTIONS[3] + this.amount2 + DESCRIPTIONS[5];
            }
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flashWithoutSound();
        changeAmount2(-1);
        if (this.amount2 == 0) {
            changeAmount2(this.cardInterval);
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, 1));
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    private void changeAmount2(int amount) {
        this.amount2 += amount;
        this.updateDescription();
    }
}
