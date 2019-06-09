package AnomalyMod.powers.ImprobabilityDriveExclusive.enemy;

import AnomalyMod.helpers.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpikierStripPower extends AbstractAnomalyPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:SpikierStrip";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public SpikierStripPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        updateDescription();
        loadRegion("juggernaut");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onActuallyGainImprobability(int improbabilityGained) {
        if (improbabilityGained > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SpikeStripPower(this.owner, improbabilityGained), improbabilityGained));
        }
    }
}
