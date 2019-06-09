package AnomalyMod.patches.ux;

import AnomalyMod.cards.AbstractAnomalyCard;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.screens.select.HandCardSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

// Holy fuck there are a lot of places to patch. Never again. Okay maybe.
public class JailbreakRenderPreviewPatch {

    public static class HandCardSelectScreenPatch {

        public static boolean doingForJailbreakHand = false;
        public static int jailbreakReduction = 0;

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = SpirePatch.CLASS
        )
        public static class ForJailbreakField {

            public static SpireField<Boolean> forJailbreak = new SpireField<>(() -> false);
        }

        private static class MainLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "forUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class SecondaryLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(HandCardSelectScreen.class, "forUpgrade");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "refreshSelectedCards"
        )
        public static class RefreshSelectedCards {

            public static void Postfix(HandCardSelectScreen __instance) {
                if (ForJailbreakField.forJailbreak.get(__instance) && __instance.selectedCards.size() == 1) {
                    __instance.selectedCards.group.get(0).target_x = Settings.WIDTH * 0.37F;
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "selectHoveredCard"
        )
        public static class SelectHoveredCard {

            @SpireInsertPatch(
                    locator = MainLocator.class
            )
            public static void Insert(HandCardSelectScreen __instance) {
                if (ForJailbreakField.forJailbreak.get(__instance) && __instance.selectedCards.size() == 1) {
                    __instance.upgradePreviewCard = __instance.selectedCards.group.get(0).makeStatEquivalentCopy();
                    if (__instance.upgradePreviewCard instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) __instance.upgradePreviewCard).baseImprobabilityNumber > 0) {
                        ((AbstractAnomalyCard) __instance.upgradePreviewCard).upgradeImprobabilityNumber(-jailbreakReduction);
                    } else {
                        __instance.upgradePreviewCard.upgrade();
                    }
                    __instance.upgradePreviewCard.displayUpgrades();
                    __instance.upgradePreviewCard.drawScale = 0.75f;
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "updateSelectedCards"
        )
        public static class UpdateSelectedCardsPartOne {

            @SpireInsertPatch(
                    locator = MainLocator.class,
                    localvars = {"e"}
            )
            public static void Insert(HandCardSelectScreen __instance, AbstractCard e) {
                if (ForJailbreakField.forJailbreak.get(__instance)) {
                    e.targetDrawScale = 0.75F;
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "updateSelectedCards"
        )
        public static class UpdateSelectedCardsPartTwo {

            @SpireInsertPatch(
                    locator = SecondaryLocator.class,
                    localvars = {"e"}
            )
            public static void Insert(HandCardSelectScreen __instance, AbstractCard e) {
                if (ForJailbreakField.forJailbreak.get(__instance)) {
                    e.targetDrawScale = 0.85F;
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "updateMessage"
        )
        public static class UpdateMessage {

            @SpireInsertPatch(
                    locator = MainLocator.class
            )
            public static SpireReturn<Void> Insert(HandCardSelectScreen __instance) {
                if (ForJailbreakField.forJailbreak.get(__instance) && __instance.selectedCards.size() == 1) {
                    if (__instance.upgradePreviewCard == null) {
                        __instance.upgradePreviewCard = __instance.selectedCards.group.get(0).makeStatEquivalentCopy();
                    }
                    if (__instance.upgradePreviewCard instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) __instance.upgradePreviewCard).baseImprobabilityNumber > 0) {
                        ((AbstractAnomalyCard) __instance.upgradePreviewCard).upgradeImprobabilityNumber(-jailbreakReduction);
                    } else {
                        __instance.upgradePreviewCard.upgrade();
                    }
                    __instance.upgradePreviewCard.displayUpgrades();
                    __instance.upgradePreviewCard.drawScale = 0.75f;
                    __instance.upgradePreviewCard.targetDrawScale = 0.75f;
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "open",
                paramtypez = {
                        String.class,
                        int.class,
                        boolean.class,
                        boolean.class,
                        boolean.class,
                        boolean.class,
                        boolean.class
                }
        )
        public static class Open {

            public static void Postfix(HandCardSelectScreen __instance, String msg, int amount, boolean la, boolean le, boolean lu, boolean LE, boolean lo) {
                if (doingForJailbreakHand) {
                    ForJailbreakField.forJailbreak.set(__instance, true);
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "render"
        )
        public static class Render {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if (f.getFieldName().equals("forUpgrade")) {
                            f.replace(
                                    "{" +
                                            "$_ = $proceed($$) || " + Nested.class.getName() + ".shouldProceed(this);" +
                                            "}");
                        }
                    }
                };
            }

            public static class Nested {

                public static boolean shouldProceed(HandCardSelectScreen screen) {
                    if (ForJailbreakField.forJailbreak.get(screen)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        @SpirePatch(
                clz = HandCardSelectScreen.class,
                method = "prep"
        )
        public static class Prep {

            public static void Postfix(HandCardSelectScreen __instance) {
                ForJailbreakField.forJailbreak.set(__instance, false);
            }
        }
    }

    public static class GridCardSelectScreenPatch {

        public static boolean doingForJailbreakGrid = false;
        public static int jailbreakReduction = 0;

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = SpirePatch.CLASS
        )
        public static class ForJailbreakField {

            public static SpireField<Boolean> forJailbreak = new SpireField<>(() -> false);
        }

        private static class MainLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "forUpgrade");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class SecondaryLocator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GridCardSelectScreen.class, "forUpgrade");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "update"
        )
        public static class UpdatePartOne {

            @SpireInsertPatch(
                    locator = MainLocator.class
            )
            public static SpireReturn<Void> Insert(GridCardSelectScreen __instance) {
                if (ForJailbreakField.forJailbreak.get(__instance)) {
                    AbstractCard hoveredCard = (AbstractCard) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "hoveredCard");
                    hoveredCard.untip();
                    __instance.confirmScreenUp = true;
                    __instance.upgradePreviewCard = hoveredCard.makeStatEquivalentCopy();
                    if (__instance.upgradePreviewCard instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) __instance.upgradePreviewCard).baseImprobabilityNumber > 0) {
                        ((AbstractAnomalyCard) __instance.upgradePreviewCard).upgradeImprobabilityNumber(-jailbreakReduction);
                    } else {
                        __instance.upgradePreviewCard.upgrade();
                    }
                    __instance.upgradePreviewCard.displayUpgrades();
                    __instance.upgradePreviewCard.drawScale = 0.875f;
                    hoveredCard.stopGlowing();
                    __instance.selectedCards.clear();
                    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                    __instance.confirmButton.show();
                    __instance.confirmButton.isDisabled = false;
                    ReflectionHacks.setPrivate(__instance, GridCardSelectScreen.class, "lastTip", (String) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "tipMsg"));
                    ReflectionHacks.setPrivate(__instance, GridCardSelectScreen.class, "tipMsg", GridCardSelectScreen.TEXT[2]);
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "update"
        )
        public static class UpdatePartTwo {

            @SpireInsertPatch(
                    locator = SecondaryLocator.class
            )
            public static void Insert(GridCardSelectScreen __instance) {
                if (ForJailbreakField.forJailbreak.get(__instance)) {
                    __instance.upgradePreviewCard.update();
                }
            }
        }

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "open",
                paramtypez = {
                        CardGroup.class,
                        int.class,
                        String.class,
                        boolean.class,
                        boolean.class,
                        boolean.class,
                        boolean.class
                }
        )
        public static class Open {

            @SpireInsertPatch(
                    locator = Dammit.class
            )
            public static void Insert(GridCardSelectScreen __instance, CardGroup R, int A, String I, boolean N, boolean B, boolean O, boolean W) {
                // O is canCancel btw. I was bored.
                ForJailbreakField.forJailbreak.set(__instance, doingForJailbreakGrid);
                if (doingForJailbreakGrid && O) {
                    AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
                }
            }
        }

        private static class Dammit extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "previousScreen");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "callOnOpen"
        )
        public static class CallOnOpen {

            public static void Postfix(GridCardSelectScreen __instance) {
                ForJailbreakField.forJailbreak.set(__instance, false);
            }
        }

        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "cancelUpgrade"
        )
        @SpirePatch(
                clz = GridCardSelectScreen.class,
                method = "render"
        )
        public static class CancelUpgrade {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if (f.getFieldName().equals("forUpgrade")) {
                            f.replace(
                                    "{" +
                                            "$_ = $proceed($$) || " + Nested.class.getName() + ".shouldProceed(this);" +
                                            "}");
                        }
                    }
                };
            }

            public static class Nested {

                public static boolean shouldProceed(GridCardSelectScreen screen) {
                    if (ForJailbreakField.forJailbreak.get(screen)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }
}
