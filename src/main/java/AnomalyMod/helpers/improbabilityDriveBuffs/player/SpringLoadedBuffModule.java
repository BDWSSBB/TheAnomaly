package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.SpringLoadedPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Collections;

public class SpringLoadedBuffModule extends AbstractImprobabilityDriveBuffModule {

    public SpringLoadedBuffModule(AbstractPlayer target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new SpringLoadedPower(this.target));
        Collections.sort(this.target.powers);
    }
}
