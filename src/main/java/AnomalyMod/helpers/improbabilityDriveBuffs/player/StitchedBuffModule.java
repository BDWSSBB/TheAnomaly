package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StitchedPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class StitchedBuffModule extends AbstractImprobabilityDriveBuffModule {

    public StitchedBuffModule(AbstractPlayer target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new StitchedPower(this.target));
    }
}
