package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.AggressorSyntaxPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Collections;

public class AggressorSyntaxBuffModule extends AbstractImprobabilityDriveBuffModule {

    public AggressorSyntaxBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new AggressorSyntaxPower(this.target, (int) this.totalAmount, 2));
            Collections.sort(this.target.powers);
        }
    }
}
