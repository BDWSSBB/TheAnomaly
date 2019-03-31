package AnomalyMod.actions.utility;

import com.megacrit.cardcrawl.actions.utility.WaitAction;

public class NoFastModeWaitAction extends WaitAction {

    public NoFastModeWaitAction(final float setDur) {
        super(setDur);
        this.duration = setDur;
    }
}
