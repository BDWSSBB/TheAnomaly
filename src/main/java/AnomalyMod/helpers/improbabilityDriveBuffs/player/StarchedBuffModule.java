package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StarchedPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class StarchedBuffModule extends AbstractImprobabilityDriveBuffModule {

    public StarchedBuffModule(AbstractPlayer target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new StarchedPower(this.target));
    }
}
