package AnomalyMod.actions.unique;

import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class SocketSliceAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private int weakAmount;

    public SocketSliceAction(AbstractCreature target, int weakAmount) {
        this.target = target;
        this.player = AbstractDungeon.player;
        this.weakAmount = weakAmount;
    }

    @Override
    public void update() {
        int findIt = 0;
        for (int i = CardPlayHelper.cardsActuallyPlayedThisCombat.size() - 2; i >= 0; i--) {
            if (CardPlayHelper.cardsActuallyPlayedThisCombat.get(i).type == AbstractCard.CardType.ATTACK) {
                findIt++;
                if (findIt == 2) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new WeakPower(this.target, this.weakAmount, false), this.weakAmount));
                    break;
                }
            } else {
                break;
            }
        }
        this.isDone = true;
    }
}
