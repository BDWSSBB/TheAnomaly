package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.PolyrhythmPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Collections;

public class PolyrhythmBuffModule extends AbstractImprobabilityDriveBuffModule {

    public PolyrhythmBuffModule(AbstractPlayer target, float costPerSelect) {
        super(target, 0.0F, costPerSelect, 0.0F, true);
    }

    public void doAction() {
        this.target.addPower(new PolyrhythmPower(this.target));
        Collections.sort(this.target.powers);
    }
}
