package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ExcavateAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:ExcavateAction").TEXT;
    private AbstractPlayer player;
    private int numberOfCards;
    private boolean hasDiscarded = false;

    public ExcavateAction(int numberOfCards) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            else {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : this.player.drawPile.group) {
                    temp.addToTop(c);
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                if (this.numberOfCards == 1) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                }
                else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + numberOfCards + TEXT[2]);
                }
                tickDuration();
                return;
            }
        }
        if (!this.hasDiscarded) {
            ArrayList<AbstractCard> temp = new ArrayList<>();
            for (AbstractCard c : this.player.drawPile.group) {
                if (!AbstractDungeon.gridSelectScreen.selectedCards.contains(c)) {
                    temp.add(c);
                }
                else {
                    c.stopGlowing();
                }
            }
            for (AbstractCard c : temp) {
                this.player.drawPile.moveToDiscardPile(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.hasDiscarded = true;
        }
        tickDuration();
    }
}
