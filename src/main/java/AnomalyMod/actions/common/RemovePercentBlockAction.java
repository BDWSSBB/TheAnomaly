package AnomalyMod.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class RemovePercentBlockAction extends AbstractGameAction {

    private float percent;

    public RemovePercentBlockAction(AbstractCreature target, AbstractCreature source, float percent) {
        this.actionType = ActionType.BLOCK;
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.target = target;
        this.source = source;
        this.percent = percent;
    }

    @Override
    public void update() {
        if (!this.target.isDying && !this.target.isDead && this.duration == this.startDuration && this.target.currentBlock > 0) {
            this.target.loseBlock((int) (this.target.currentBlock * this.percent));
        }
        this.isDone = true;
    }
}
