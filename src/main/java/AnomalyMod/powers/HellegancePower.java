package AnomalyMod.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HellegancePower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:Hellegance";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public HellegancePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount * 2;
        updateDescription();
        loadRegion("attackBurn");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        this.amount2 = amount * 2;
    }

    @Override
    public void onActuallyGainImprobability(int improbabilityGained) {
        if (improbabilityGained > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.owner, this.amount2));
        }
    }
}
