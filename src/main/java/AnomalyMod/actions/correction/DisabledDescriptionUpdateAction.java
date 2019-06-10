package AnomalyMod.actions.correction;

import AnomalyMod.powers.DisablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class DisabledDescriptionUpdateAction extends AbstractGameAction {

    private DisablePower powerInstance;

    public DisabledDescriptionUpdateAction(DisablePower powerInstance) {
        this.powerInstance = powerInstance;
    }

    @Override
    public void update() {
        powerInstance.updateWeakAndVuln();
        this.isDone = true;
    }
}
