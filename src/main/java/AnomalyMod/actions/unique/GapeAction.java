package AnomalyMod.actions.unique;

import AnomalyMod.powers.StapledPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class GapeAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private int damageIncreaseAmount;

    public GapeAction(AbstractCreature target, int damageIncreaseAmount) {
        this.target = target;
        this.player = AbstractDungeon.player;
        this.damageIncreaseAmount = damageIncreaseAmount;
    }

    @Override
    public void update() {
        if (this.target.hasPower(VulnerablePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new StapledPower(this.target, this.damageIncreaseAmount), this.damageIncreaseAmount));
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.target, this.player, VulnerablePower.POWER_ID, 1));
        }
        this.isDone = true;
    }
}
