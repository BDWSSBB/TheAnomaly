package AnomalyMod.actions.common;

import AnomalyMod.actions.common.VanillaImprovements.BetterQueueCardAction;
import AnomalyMod.patches.correction.PurgeOnUseLaterPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayCardFromNowhereAction extends AbstractGameAction {

    private AbstractCard card;
    private boolean putInFrontOfQueue;

    public PlayCardFromNowhereAction(AbstractCard card, boolean putInFrontOfQueue) {
        this.card = card;
        this.putInFrontOfQueue = putInFrontOfQueue;
    }

    public PlayCardFromNowhereAction(AbstractCard card) {
        this(card, true);
    }

    @Override
    public void update() {
        this.card.current_y = -200.0f * Settings.scale;
        this.card.target_x = Settings.WIDTH / 2.0f * Settings.scale;
        this.card.target_y = Settings.HEIGHT / 2.0f;
        this.card.freeToPlayOnce = true;
        PurgeOnUseLaterPatch.PurgeOnUseLaterField.anomalyModPurgeOnUseLater.set(this.card, true);
        this.card.applyPowers();
        AbstractDungeon.actionManager.addToTop(new BetterQueueCardAction(this.card, true, this.putInFrontOfQueue));
        this.isDone = true;
    }
}
