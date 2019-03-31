package AnomalyMod.actions.common;

import AnomalyMod.patches.PurgeOnUseLaterPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayCardFromNowhereAction extends AbstractGameAction {

    private AbstractCard card;

    public PlayCardFromNowhereAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        this.card.current_y = -200.0f * Settings.scale;
        this.card.target_x = Settings.WIDTH / 2.0f * Settings.scale;
        this.card.target_y = Settings.HEIGHT / 2.0f;
        this.card.freeToPlayOnce = true;
        PurgeOnUseLaterPatch.PurgeOnUseLaterField.anomalyModPurgeOnUseLater.set(this.card, true);
        this.card.applyPowers();
        AbstractDungeon.actionManager.addToTop(new QueueCardAction(this.card, AbstractDungeon.getRandomMonster()));
        this.isDone = true;
    }
}
