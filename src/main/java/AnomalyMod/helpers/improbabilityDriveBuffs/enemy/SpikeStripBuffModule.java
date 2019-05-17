package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.SpikeStripPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpikeStripBuffModule extends AbstractImprobabilityDriveBuffModule {

    public SpikeStripBuffModule(AbstractMonster target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new SpikeStripPower(this.target, (int) this.totalAmount));
        }
    }
}
