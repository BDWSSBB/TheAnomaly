package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;

public class DelayedPowerPower extends AbstractAnomalyPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:DelayedPower";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private AbstractPower powerToApply;

    public DelayedPowerPower(AbstractCreature owner, AbstractPower powerToApply) {
        this.ID = POWER_ID;
        this.name = NAME + " " + powerToApply.name;
        if (powerToApply.type == PowerType.BUFF) {
            this.type = AbstractPower.PowerType.BUFF;
        } else {
            this.type = AbstractPower.PowerType.DEBUFF;
        }
        this.owner = owner;
        this.powerToApply = powerToApply;
        this.amount = powerToApply.amount;
        updateDescription();
        loadRegion("backAttack");
    }

    @Override
    public void updateDescription() {
        if (this.owner.isPlayer) {
            this.description = DESCRIPTIONS[0] + this.amount + " " + FontHelper.colorString(powerToApply.name, "y") + LocalizedStrings.PERIOD;
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + " " + FontHelper.colorString(powerToApply.name, "y") + LocalizedStrings.PERIOD;
        }
    }

    public void onTrigger() {
        this.owner.addPower(this.powerToApply);
        Collections.sort(this.owner.powers);
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
