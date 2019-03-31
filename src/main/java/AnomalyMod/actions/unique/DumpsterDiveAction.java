package AnomalyMod.actions.unique;

import AnomalyMod.actions.common.VanillaImprovements.BetterDiscardPileToHandAction;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DumpsterDiveAction extends AbstractGameAction {

    private AbstractPlayer player;

    public DumpsterDiveAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
        final int count = AbstractDungeon.player.hand.size();
        AbstractDungeon.actionManager.addToTop(new BetterDiscardPileToHandAction(count));
        AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(0.25F));
        AbstractDungeon.actionManager.addToTop(new DiscardAction(this.player, this.player, count, false));
        this.isDone = true;
    }
}
