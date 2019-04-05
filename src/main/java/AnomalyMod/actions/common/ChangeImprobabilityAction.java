package AnomalyMod.actions.common;

import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ChangeImprobabilityAction extends AbstractGameAction {

    private int improbabilityNumber;

    public ChangeImprobabilityAction(int improbabilityNumber) {
        this.improbabilityNumber = improbabilityNumber;
    }

    public void update() {
        ImprobabilityDrive.changeImprobability(this.improbabilityNumber);
        this.isDone = true;
    }
}
