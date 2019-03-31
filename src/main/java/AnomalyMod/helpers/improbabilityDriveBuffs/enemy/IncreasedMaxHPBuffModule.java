package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.common.VanillaImprovements.FixedIncreaseMaxHpAction;
import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IncreasedMaxHPBuffModule extends AbstractImprobabilityDriveBuffModule {


    public IncreasedMaxHPBuffModule(AbstractMonster target, float amountPerSelect, float costPerSelect, float selectionWeight, int initialAmount, float freeridingFactor) {
        super(target, amountPerSelect, costPerSelect, selectionWeight, false, initialAmount, freeridingFactor);
        this.isRemainderModule = true;
    }

    public IncreasedMaxHPBuffModule(AbstractMonster target, float amountPerSelect, float costPerSelect, float selectionWeight) {
        this(target, amountPerSelect, costPerSelect, selectionWeight, 0, 0.0F);
    }

    public void doAction() {
        if (this.target instanceof AbstractMonster) {
            if (this.totalAmount > 0 && this.target.currentHealth > 1) {
                AbstractDungeon.actionManager.addToBottom(new FixedIncreaseMaxHpAction((AbstractMonster) this.target, this.totalAmount, true));
            }
        }
        else {
            AnomalyMod.logger.info("Why does a non-monster have the HP module?");
        }
    }
}
