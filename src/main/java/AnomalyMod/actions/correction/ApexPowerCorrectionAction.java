package AnomalyMod.actions.correction;

import AnomalyMod.powers.ApexPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ApexPowerCorrectionAction extends AbstractGameAction { // This is to get around the issue of remove Apex when you have 1 card left so it doesn't debuff.

    private ApexPower powerInstance;

    public ApexPowerCorrectionAction(ApexPower powerInstance) {
        this.powerInstance = powerInstance;
    }

    public void update() {
        this.powerInstance.shouldDebuff = true;
        this.isDone = true;
    }
}