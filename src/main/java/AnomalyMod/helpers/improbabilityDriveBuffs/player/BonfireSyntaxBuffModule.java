package AnomalyMod.helpers.improbabilityDriveBuffs.player;

import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.BonfireSyntaxPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BonfireSyntaxBuffModule extends AbstractImprobabilityDriveBuffModule {

    public BonfireSyntaxBuffModule(AbstractPlayer target, int amountPerSelect, float costPerSelect, float selectionWeight) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false);
    }

    public void doAction() {
        if (this.totalAmount > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.target, null, new BonfireSyntaxPower(this.target,(int) this.totalAmount, 2), (int) this.totalAmount));
        }
    }
}
