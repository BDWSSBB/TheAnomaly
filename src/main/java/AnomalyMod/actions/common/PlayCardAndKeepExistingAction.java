package AnomalyMod.actions.common;

import AnomalyMod.actions.utility.NoFastModeWaitAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
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

    public PlayCardAndKeepExistingAction(AbstractCard card) {
        this.target = AbstractDungeon.getRandomMonster();
        this.player = AbstractDungeon.player;
        this.card = card;
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
        if (!card.canUse(AbstractDungeon.player, this.target)) {
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            AbstractDungeon.actionManager.addToTop(new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
            AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(0.4f));
        }
        else {
            this.card.freeToPlayOnce = true;
            this.card.applyPowers();
            AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
        this.isDone = true;
    }
}
