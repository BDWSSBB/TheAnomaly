package AnomalyMod.patches.correction;

import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import javassist.CtBehavior;

public class BetterCardPlayHistoryPatch {

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class AddCardToHistory {

        @SpireInsertPatch(
                locator = TurnLocator.class
        )
        public static void TurnInsert(GameActionManager __instance) {
            if (!__instance.cardQueue.get(0).card.dontTriggerOnUseCard) {
                CardPlayHelper.cardsActuallyPlayedThisTurn.add(__instance.cardQueue.get(0).card);
            }
        }

        private static class TurnLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisTurn");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpireInsertPatch(
                locator = CombatLocator.class
        )
        public static void CombatInsert(GameActionManager __instance) {
            if (!__instance.cardQueue.get(0).card.dontTriggerOnUseCard) {
                CardPlayHelper.cardsActuallyPlayedThisCombat.add(__instance.cardQueue.get(0).card);
            }
        }

        private static class CombatLocator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisCombat");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class RemoveCardsFromTurnHistory {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            CardPlayHelper.cardsActuallyPlayedThisTurn.clear();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "damageReceivedThisTurn");
                int[] found = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "clear"
    )
    public static class RemoveCardsFromCombatHistory {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            CardPlayHelper.cardsActuallyPlayedThisCombat.clear();
            CardPlayHelper.cardsActuallyPlayedThisTurn.clear();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "cardsPlayedThisCombat");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
