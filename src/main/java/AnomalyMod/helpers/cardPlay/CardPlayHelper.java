package AnomalyMod.helpers.cardPlay;

import AnomalyMod.actions.correction.HandApplyPowersAction;
import AnomalyMod.actions.correction.HandGlowCheckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class CardPlayHelper {

    // Due to something related to Status and Curses, I need this to accurately track what cards were actually played (vs. stuff like Burns).
    // No, checking the normal card play history doesn't really work out for my purposes.
    public static ArrayList<AbstractCard> cardsActuallyPlayedThisTurn = new ArrayList<>();
    public static ArrayList<AbstractCard> cardsActuallyPlayedThisCombat = new ArrayList<>();

    public static void standardHandActionResetProtocol() {
        AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.actionManager.addToBottom(new HandGlowCheckAction());
        AbstractDungeon.actionManager.addToBottom(new HandApplyPowersAction());
    }
}
