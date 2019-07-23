package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.GravitronSyntax;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import java.util.Collections;

public class GravitronSyntaxBuffModule extends AbstractImprobabilityDriveBuffModule {

    public GravitronSyntaxBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new GravitronSyntax(this.target, (int) this.totalAmount, 4));
            Collections.sort(this.target.powers);
        }
    }
}
