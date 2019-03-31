package AnomalyMod.patches;

import AnomalyMod.AnomalyMod;
import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class FreeFormPatch {

    public static boolean doingFreeFormSelection = false; // Grumble.

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "discoveryOpen",
            paramtypez = {}
    )
    public static class ReduceImprobabilityInFreeFormSelection {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CardRewardScreen __instance) {
            if (doingFreeFormSelection) {
                for (AbstractCard c : __instance.rewardGroup) {
                    if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).improbabilityNumber > 0) {
                        ((AbstractAnomalyCard) c).changeImprobabilityNumber(-1, true);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "isScreenUp");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = CardRewardScreen.class,
            method = "discoveryOpen",
            paramtypez = {}
    )
    public static class NoUnplayablesFromFreeForm {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("add")) {
                        m.replace(
                                "{" +
                                        "if (" + FreeFormPatch.NoUnplayablesFromFreeForm.Nested.class.getName() + ".notUnplayable($$)) {$_ = $proceed($$);}" +
                                        "}");
                    }
                }
            };
        }

        public static class Nested {

            public static boolean notUnplayable(Object tmp) {
                if (tmp instanceof AbstractCard) {
                    AbstractCard definitelyACard = (AbstractCard) tmp;
                    if (doingFreeFormSelection && definitelyACard.cost == -2) {
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    AnomalyMod.logger.info("I SWEAR I PUT A CARD IN, WHAT THE HELL.");
                    return true;
                }
            }
        }
    }
}
