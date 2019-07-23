package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.ShellLoadedPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Collections;

public class ShellLoadedBuffModule extends AbstractImprobabilityDriveBuffModule {

    public ShellLoadedBuffModule(AbstractPlayer target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new ShellLoadedPower(this.target));
        Collections.sort(this.target.powers);
    }
}
