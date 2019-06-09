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

public class TrenchWarfareAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    private AbstractPlayer player;
    private int blockPerCardType;

    public TrenchWarfareAction(int blockPerCardType) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.blockPerCardType = blockPerCardType;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                this.isDone = true;
                return;
            } else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 3, true, true);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            ArrayList<AbstractCard.CardType> typesOfCardsDiscarded = new ArrayList<>();
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (!typesOfCardsDiscarded.contains(c.type)) {
                    typesOfCardsDiscarded.add(c.type);
                }
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            for (int i = 0; i < typesOfCardsDiscarded.size(); i++) {
                AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.player, this.player, this.blockPerCardType));
            }
            CardPlayHelper.standardHandActionResetProtocol();
        }
        tickDuration();
    }
}
