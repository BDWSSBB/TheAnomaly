package AnomalyMod.actions.utility;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class NoFastModeWaitAction extends WaitAction {

    public NoFastModeWaitAction(final float setDur) {
        super(setDur);
        this.duration = setDur;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hasRelic("hubris:CrackedHourglass")) {
            AbstractRelic hourglass = AbstractDungeon.player.getRelic("hubris:CrackedHourglass");
            if (hourglass.counter == 0) {
                this.isDone = true;
            }
        }
        super.update();
    }
}
