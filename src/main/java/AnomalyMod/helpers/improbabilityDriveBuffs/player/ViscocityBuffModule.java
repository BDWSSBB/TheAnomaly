package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.ViscosityPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ViscocityBuffModule extends AbstractImprobabilityDriveBuffModule {

    public ViscocityBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight, int initialAmount, float freeridingFactor) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false, initialAmount, freeridingFactor);
        this.isRemainderModule = true;
    }

    public ViscocityBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        this(target, amountPerSelect, costPerSelect, selectionWeight, 0, 0.0F);
    }

    public void doAction() {
        if (this.totalAmount> 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, null, new ViscosityPower(this.target, (int) this.totalAmount), (int) this.totalAmount));
        }
    }
}
