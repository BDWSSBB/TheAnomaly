package AnomalyMod.actions.common;

import AnomalyMod.actions.common.VanillaImprovements.BetterQueueCardAction;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlayCardAndKeepExistingAction extends AbstractGameAction {

    private AbstractMonster target;
    private AbstractPlayer player;
    private AbstractCard card;
    private boolean putInFrontOfQueue;

    public PlayCardAndKeepExistingAction(AbstractCard card, boolean putInFrontOfQueue) {
        this.player = AbstractDungeon.player;
        this.card = card;
        this.putInFrontOfQueue = putInFrontOfQueue;
    }

    public PlayCardAndKeepExistingAction(AbstractCard card) {
        this(card, true);
    }

    @Override
    public void update() {
        AbstractDungeon.getCurrRoom().souls.remove(this.card);
        this.player.limbo.group.add(this.card);
        this.card.current_y = -200.0f * Settings.scale;
        this.card.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
        this.card.target_y = Settings.HEIGHT / 2.0f;
        this.card.targetAngle = 0.0f;
        this.card.lighten(false);
        this.card.drawScale = 0.12f;
        this.card.targetDrawScale = 0.75f;
        this.card.freeToPlayOnce = true;

        if (!this.card.canUse(AbstractDungeon.player, this.target)) {
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(this.card));
            AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(this.card, AbstractDungeon.player.limbo));
            AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(0.4f));
        } else {
            this.card.applyPowers();
            AbstractDungeon.actionManager.addToTop(new BetterQueueCardAction(this.card, true, this.putInFrontOfQueue));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(this.card));
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
        this.isDone = true;
    }
}
