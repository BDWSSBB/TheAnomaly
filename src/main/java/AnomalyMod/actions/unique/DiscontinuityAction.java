package AnomalyMod.actions.unique;

import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import AnomalyMod.powers.DiscontinuityPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscontinuityAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int numberOfCards;

    public DiscontinuityAction(int numberOfCards) {
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    @Override
    public void update() {
        if (CardPlayHelper.cardsActuallyPlayedThisCombat.size() > 1) {
            AbstractCard tmp = CardPlayHelper.cardsActuallyPlayedThisCombat.get(CardPlayHelper.cardsActuallyPlayedThisCombat.size() - 2).makeStatEquivalentCopy();
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new DiscontinuityPower(this.player, this.numberOfCards, tmp)));
        }
        this.isDone = true;
    }
}
