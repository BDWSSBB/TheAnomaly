package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.AuditorPower;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AuditorBuffModule extends AbstractImprobabilityDriveBuffModule {

    public AuditorBuffModule(AbstractMonster target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new AuditorPower(this.target));
    }
}
