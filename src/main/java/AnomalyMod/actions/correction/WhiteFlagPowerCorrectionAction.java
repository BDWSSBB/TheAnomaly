package AnomalyMod.actions.correction;

import AnomalyMod.AnomalyMod;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.WhiteFlagPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WhiteFlagPowerCorrectionAction extends AbstractGameAction {

    private WhiteFlagPower powerInstance;

    public WhiteFlagPowerCorrectionAction(WhiteFlagPower powerInstance) {
        this.powerInstance = powerInstance;
    }

    public void update() {
        if (this.powerInstance.amount > 0) {
            this.powerInstance.shouldTakeNoDamage = true;
        }
        else {
            this.powerInstance.shouldTakeNoDamage = false;
        }
        if (this.powerInstance.owner instanceof AbstractMonster) {
            ((AbstractMonster) this.powerInstance.owner).applyPowers();
        }
        else {
            AnomalyMod.logger.info("Hey what the hell are you doing putting this on other people? Get outta here.");
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.powerInstance.owner, null, this.powerInstance));
        }
        this.isDone = true;
    }
}
