package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ConicSandsPower extends AbstractAnomalyTwoAmountPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:ConicSands";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public ConicSandsPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = this.amount2 = amount;
        updateDescription();
        loadRegion("drawDown");
        this.priority = -1;
    }

    @Override
    public void updateDescription() {
        if (this.amount2 == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[4];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2] + this.amount2 + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flash();
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new StrengthPower(m, -1), -1));
        }
        changeAmount2(-1);
        if (this.amount2 == 0) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new StrengthPower(m, this.amount), this.amount));
            }
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    private void changeAmount2(int amount) {
        this.amount2 += amount;
        this.updateDescription();
    }
}
