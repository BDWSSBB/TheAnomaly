package AnomalyMod.actions.correction;

import AnomalyMod.powers.BlackoutPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class BlackoutPowerCorrectionAction extends AbstractGameAction { // Similar to the apex issue. There's probably a better way around this.

    private BlackoutPower powerInstance;

    public BlackoutPowerCorrectionAction(BlackoutPower powerInstance) {
        this.powerInstance = powerInstance;
    }

    public void update() {
        this.powerInstance.shouldEndTurn = true;
        this.isDone = true;
    }
}
