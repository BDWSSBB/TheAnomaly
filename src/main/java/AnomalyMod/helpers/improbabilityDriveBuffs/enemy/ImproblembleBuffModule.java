package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.ImproblemblePower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ImproblembleBuffModule extends AbstractImprobabilityDriveBuffModule {

    public ImproblembleBuffModule(AbstractMonster target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new ImproblemblePower(this.target, (int) this.totalAmount));
        }
    }
}
