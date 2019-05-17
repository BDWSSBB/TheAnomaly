package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.DefensorSyntaxPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class DefensorSyntaxBuffModule extends AbstractImprobabilityDriveBuffModule {

    public DefensorSyntaxBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new DefensorSyntaxPower(this.target, (int) this.totalAmount, 2));
        }
    }
}
