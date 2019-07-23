package AnomalyMod.helpers.improbabilityDriveBuffs.enemy;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.unique.ScaleInvincibilityAction;
import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.patches.correction.HideIncreaseMaxHPEffectPatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;

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
            int hpIncrease = MathUtils.floor((float) this.target.maxHealth * this.totalAmount);
            if (this.totalAmount > 0 && this.target.currentHealth > 1 && hpIncrease > 0) {
                HideIncreaseMaxHPEffectPatch.hideEffect = true;
                this.target.increaseMaxHp(hpIncrease, false);
                HideIncreaseMaxHPEffectPatch.hideEffect = false;
            }
            if (this.target instanceof CorruptHeart) {
                AbstractDungeon.actionManager.addToBottom(new ScaleInvincibilityAction(this.target));
            }
        } else {
            AnomalyMod.logger.info("Why does a non-monster have the HP module?");
        }
    }
}
