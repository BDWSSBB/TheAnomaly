package AnomalyMod.actions.unique;

import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class NegativeReceptionAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    private AbstractPlayer player;
    private int cardsToDiscard;
    private boolean upgraded;

    public NegativeReceptionAction(int cardsToDiscard, boolean upgraded) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.cardsToDiscard = cardsToDiscard;
        this.upgraded = upgraded;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.cardsToDiscard, true, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (this.upgraded) {
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    this.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                }
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.player, AbstractDungeon.handCardSelectScreen.selectedCards.group.size()));
            } else {
                ArrayList<AbstractCard.CardType> typesOfCardsDiscarded = new ArrayList<>();
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    if (!typesOfCardsDiscarded.contains(c.type)) {
                        typesOfCardsDiscarded.add(c.type);
                    }
                    this.player.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                    GameActionManager.incrementDiscard(false);
                }
                AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.player, typesOfCardsDiscarded.size()));
            }
            CardPlayHelper.standardHandActionResetProtocol();
        }
        tickDuration();
    }
}
