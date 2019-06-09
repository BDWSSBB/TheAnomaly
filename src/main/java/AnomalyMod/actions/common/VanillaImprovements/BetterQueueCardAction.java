package AnomalyMod.actions.common.VanillaImprovements;

import AnomalyMod.AnomalyMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BetterQueueCardAction extends AbstractGameAction {

    private AbstractCard card;
    private boolean selectRandomTarget;
    private boolean immediateCard;

    public BetterQueueCardAction(AbstractCard card, AbstractCreature target, boolean immediateCard) {
        this.card = card;
        this.target = target;
        this.immediateCard = immediateCard;
        this.selectRandomTarget = false;
    }

    public BetterQueueCardAction(AbstractCard card, AbstractCreature target) {
        this(card, target, true);
    }

    public BetterQueueCardAction(AbstractCard card, boolean selectRandomTarget, boolean immediateCard) {
        this(card, null, immediateCard);
        this.selectRandomTarget = true;
    }

    public BetterQueueCardAction(AbstractCard card, boolean selectRandomTarget) {
        this(card, selectRandomTarget, true);
        this.selectRandomTarget = true;
    }

    @Override
    public void update() {
        if (this.card == null) {
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem());
        } else if (!queueContains(this.card)) {
            if (this.selectRandomTarget) {
                this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            }
            if (!(this.target instanceof AbstractMonster)) {
                AnomalyMod.logger.info("WARNING: BetterQueueCardAction contains not an AbstractMonster!");
                this.isDone = true;
                return;
            }
            if (this.immediateCard) {
                if (!AbstractDungeon.actionManager.cardQueue.isEmpty()) {
                    AbstractDungeon.actionManager.cardQueue.add(1, new CardQueueItem(this.card, (AbstractMonster) this.target));
                } else {
                    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.card, (AbstractMonster) this.target));
                }
            } else {
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this.card, (AbstractMonster) this.target));
            }
        }
        this.isDone = true;
    }

    private boolean queueContains(AbstractCard card) {
        for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card == card) {
                return true;
            }
        }
        return false;
    }
}
