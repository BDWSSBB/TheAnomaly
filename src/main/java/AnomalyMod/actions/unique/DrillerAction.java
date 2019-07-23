package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class DrillerAction extends AbstractGameAction {

    private AbstractCreature target;
    private int minWeakOrVuln;
    private DamageInfo info;
    private int numberOfHits;

    public DrillerAction(AbstractCreature target, int minWeakOrVuln, DamageInfo info, int numberOfHits) {
        this.target = target;
        this.minWeakOrVuln = minWeakOrVuln;
        this.info = info;
        this.numberOfHits = numberOfHits;
    }

    @Override
    public void update() {
        if ((this.target.hasPower(WeakPower.POWER_ID) && this.target.getPower(WeakPower.POWER_ID).amount >= this.minWeakOrVuln) ||
                (this.target.hasPower(VulnerablePower.POWER_ID) && this.target.getPower(VulnerablePower.POWER_ID).amount >= this.minWeakOrVuln)) {
            for (int i = 0; i < this.numberOfHits; i++) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, AttackEffect.SLASH_HEAVY));
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.25F));
        }
        this.isDone = true;
    }
}
