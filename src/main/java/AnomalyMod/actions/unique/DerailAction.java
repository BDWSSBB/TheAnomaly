package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class DerailAction extends AbstractGameAction {

    private AbstractMonster target;
    private AbstractPlayer player;
    private int powerAmount;

    public DerailAction(AbstractMonster monster, int powerAmount) {
        this.target = monster;
        this.player = AbstractDungeon.player;
        this.powerAmount = powerAmount;
    }

    public void update() {
        if (!this.target.isDeadOrEscaped()) {
            if (this.target.intent == AbstractMonster.Intent.ATTACK || this.target.intent == AbstractMonster.Intent.ATTACK_BUFF || this.target.intent == AbstractMonster.Intent.ATTACK_DEBUFF || this.target.intent == AbstractMonster.Intent.ATTACK_DEFEND) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new WeakPower(this.target, this.powerAmount, false), this.powerAmount));
            }
            else {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new VulnerablePower(this.target, this.powerAmount, false), this.powerAmount));
            }
        }
        this.isDone = true;
    }
}
