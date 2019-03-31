package AnomalyMod.actions.unique;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class InvalidateAction extends AbstractGameAction {

    private AbstractPlayer player;

    public InvalidateAction() {
        this.player = AbstractDungeon.player;
    }

    public void update() {
        int numberOfDebuffs = 0;
        for (AbstractPower p : this.player.powers) {
            if (p.type == AbstractPower.PowerType.DEBUFF) {
                numberOfDebuffs++;
            }
        }
        AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(numberOfDebuffs));
        AbstractDungeon.actionManager.addToTop(new RemoveDebuffsAction(this.player));
        this.isDone = true;
    }
}
