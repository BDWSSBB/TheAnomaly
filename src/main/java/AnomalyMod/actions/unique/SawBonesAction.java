package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class SawBonesAction extends AbstractGameAction {

    private AbstractMonster target;
    private AbstractPlayer player;
    private int strengthDownAmount;

    public SawBonesAction(AbstractMonster monster, int strengthDownAmount) {
        this.target = monster;
        this.player = AbstractDungeon.player;
        this.strengthDownAmount = strengthDownAmount;
    }

    public void update() {
        if (!this.target.isDeadOrEscaped() && this.target.hasPower(WeakPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new StrengthPower(this.target, -this.strengthDownAmount), -this.strengthDownAmount));
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.target, this.player, WeakPower.POWER_ID, 1));
        }
        this.isDone = true;
    }
}
