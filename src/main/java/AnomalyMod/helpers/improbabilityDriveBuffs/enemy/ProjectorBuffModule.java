package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.ProjectorPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;

public class ProjectorBuffModule extends AbstractImprobabilityDriveBuffModule {

    public ProjectorBuffModule(AbstractMonster target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new ProjectorPower(this.target, (int) this.totalAmount));
            Collections.sort(this.target.powers);
        }
    }
}
