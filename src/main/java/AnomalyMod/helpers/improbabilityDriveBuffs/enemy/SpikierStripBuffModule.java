package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.SpikierStripPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SpikierStripBuffModule extends AbstractImprobabilityDriveBuffModule {

    public SpikierStripBuffModule(AbstractMonster target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new SpikierStripPower(this.target));
    }
}
