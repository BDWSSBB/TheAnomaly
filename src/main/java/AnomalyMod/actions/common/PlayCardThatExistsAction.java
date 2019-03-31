package AnomalyMod.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlayCardThatExistsAction extends AbstractGameAction {

    private AbstractCard card;

    public PlayCardThatExistsAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        AbstractDungeon.getCurrRoom().souls.remove(this.card);
        this.card.freeToPlayOnce = true;
        AbstractDungeon.player.limbo.group.add(this.card);
        AbstractMonster target = AbstractDungeon.getRandomMonster();
        this.card.applyPowers();
        AbstractDungeon.actionManager.addToTop(new QueueCardAction(this.card, target));
        AbstractDungeon.actionManager.addToTop(new UnlimboAction(this.card));
        if (Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
        }
        else {
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }
        this.isDone = true;
    }
}
