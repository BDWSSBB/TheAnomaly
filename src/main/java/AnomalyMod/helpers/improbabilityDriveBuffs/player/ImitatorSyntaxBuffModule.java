package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.ImitatorSyntax;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public class ImitatorSyntaxBuffModule extends AbstractImprobabilityDriveBuffModule {

    public ImitatorSyntaxBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            this.target.addPower(new ImitatorSyntax(this.target, (int) this.totalAmount, 5));
        }
    }
}
