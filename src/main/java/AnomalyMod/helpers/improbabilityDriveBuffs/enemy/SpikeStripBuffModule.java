package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.SpikeStripPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpikeStripBuffModule extends AbstractImprobabilityDriveBuffModule {

    public SpikeStripBuffModule(AbstractMonster target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, null, new SpikeStripPower(this.target, (int) this.totalAmount), (int) this.totalAmount));
        }
    }
}
