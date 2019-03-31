package AnomalyMod.actions.unique;

import AnomalyMod.actions.common.PlayCardThatExistsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class StitchedAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:PlayFromHandAction").TEXT;
    private AbstractPlayer player;
    private ArrayList<AbstractCard> notAttacks = new ArrayList<>();

    public StitchedAction() {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            for (AbstractCard c : this.player.hand.group) {
                if (c.type != AbstractCard.CardType.ATTACK) {
                    this.notAttacks.add(c);
                }
            }
            if (this.notAttacks.size() == this.player.hand.size()) {
                this.isDone = true;
                return;
            }
            else {
                this.player.hand.group.removeAll(this.notAttacks);
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, true, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (!AbstractDungeon.handCardSelectScreen.selectedCards.isEmpty()) {
                AbstractDungeon.actionManager.addToTop(new PlayCardThatExistsAction(AbstractDungeon.handCardSelectScreen.selectedCards.getTopCard()));
            }
            for (final AbstractCard c : this.notAttacks) {
                this.player.hand.addToTop(c);
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
