package AnomalyMod.actions.unique;

import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class EscapeAttemptAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:EscapeAttemptAction").TEXT;
    private AbstractPlayer player;
    private int cardsToKeep;
    private int cardsToGetBlock;
    private int blockAmount;

    public EscapeAttemptAction(int cardsToKeep, int cardsToGetBlock, int blockAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.cardsToKeep = cardsToKeep;
        this.cardsToGetBlock = cardsToGetBlock;
        this.blockAmount = blockAmount;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.cardsToKeep, true, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard> cardsToDiscard = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (!AbstractDungeon.handCardSelectScreen.selectedCards.group.contains(c)) {
                    cardsToDiscard.add(c);
                }
            }
            for (AbstractCard c : cardsToDiscard) {
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.player.hand.addToTop(c);
            }
            if (cardsToDiscard.size() >= this.cardsToGetBlock) {
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.blockAmount));
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, this.blockAmount));
            }
            CardPlayHelper.standardHandActionResetProtocol();
        }
        tickDuration();
    }
}
