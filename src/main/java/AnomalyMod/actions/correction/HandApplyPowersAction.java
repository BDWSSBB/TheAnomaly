package AnomalyMod.actions.correction;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandApplyPowersAction extends AbstractGameAction {

    private AbstractPlayer player;

    public HandApplyPowersAction() {
        this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
        this.player.hand.applyPowers();
        this.isDone = true;
    }
}
