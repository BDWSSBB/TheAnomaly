package AnomalyMod.powers.FixedVanillaPowers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

public class BetterNextTurnDrawPower extends DrawCardNextTurnPower {

    public BetterNextTurnDrawPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        AbstractDungeon.player.gameHandSize += amount;
    }

    @Override
    public void atStartOfTurn() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void atStartOfTurnPostDraw() {
    }

    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize -= this.amount;
    }
}
