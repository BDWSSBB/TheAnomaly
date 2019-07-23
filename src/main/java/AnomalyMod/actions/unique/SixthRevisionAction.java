package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SixthRevisionAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:SixthRevisionAction").TEXT;
    private AbstractPlayer player;
    private int numberOfCards;

    public SixthRevisionAction(int numberOfCards) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        if (Settings.FAST_MODE) {
            this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        }
        this.player = AbstractDungeon.player;
        this.numberOfCards = numberOfCards;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            } else {
                CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : this.player.discardPile.group) {
                    if (c.canUpgrade()) {
                        temp.addToTop(c);
                    }
                }
                temp.sortAlphabetically(true);
                temp.sortByRarityPlusStatusCardType(false);
                if (temp.isEmpty()) {
                    this.isDone = true;
                    return;
                }
                if (this.numberOfCards == 1) {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[0]);
                } else {
                    AbstractDungeon.gridSelectScreen.open(temp, this.numberOfCards, true, TEXT[1] + this.numberOfCards + TEXT[2]);
                }
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                this.player.discardPile.moveToDeck(c, true);
                if (c.canUpgrade()) {
                    c.upgrade();
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
