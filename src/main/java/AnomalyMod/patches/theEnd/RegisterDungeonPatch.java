package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import AnomalyMod.patches.enums.PlayerClassEnum;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class RegisterDungeonPatch {

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "getDungeon",
            paramtypez = {
                    String.class,
                    AbstractPlayer.class
            }
    )
    public static class AddDungeon {

        public static AbstractDungeon Postfix(AbstractDungeon __result, CardCrawlGame __instance, String key, AbstractPlayer p) {
            if (key.equals(AnomalyTheEnding.ID)) {
                __result = new AnomalyTheEnding(p, AbstractDungeon.specialOneTimeEventList);
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = CardCrawlGame.class,
            method = "getDungeon",
            paramtypez = {
                    String.class,
                    AbstractPlayer.class,
                    SaveFile.class
            }
    )
    public static class AddDungeonSaveAndLoad {

        public static AbstractDungeon Postfix(AbstractDungeon __result, CardCrawlGame __instance, String key, AbstractPlayer p, SaveFile saveFile) {
            if (key.equals(AnomalyTheEnding.ID)) {
                __result = new AnomalyTheEnding(p, saveFile);
            }
            return __result;
        }
    }

    public static class RegisterAnomalyAct4 {

        @SpirePatch(
                clz = Settings.class,
                method = "setFinalActAvailability"
        )
        public static class SetAct4 {

            public static void Postfix() {
                if (CardCrawlGame.chosenCharacter == PlayerClassEnum.ANOMALY_CLASS && ConfigHelper.goToAnomalyAct4) {
                    Settings.isFinalActAvailable = false;
                    AnomalyModDungeonData.goToAnomalyAct4 = true;
                } else {
                    AnomalyModDungeonData.goToAnomalyAct4 = false;
                }
            }
        }

        @SpirePatch(
                clz = AbstractRoom.class,
                method = "update"
        )
        public static class AllowRewardScreen {

            public static Boolean temp;

            @SpireInsertPatch(
                    locator = LocatorOne.class
            )
            public static void InsertOne(AbstractRoom __instance) {
                if (AnomalyModDungeonData.goToAnomalyAct4) {
                    temp = Settings.isEndless;
                    Settings.isEndless = true;
                }
            }

            private static class LocatorOne extends SpireInsertLocator {

                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                    int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                    return new int[]{found[found.length - 1]};
                }
            }

            @SpireInsertPatch(
                    locator = LocatorTwo.class
            )
            public static void InsertTwo(AbstractRoom __instance) {
                if (temp != null) {
                    Settings.isEndless = temp;
                    temp = null;
                }
            }

            private static class LocatorTwo extends SpireInsertLocator {

                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "loading_post_combat");
                    int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                    return new int[]{found[found.length - 3]};
                }
            }

            public static void Postfix(AbstractRoom __instance) {
                if (temp != null) {
                    Settings.isEndless = temp;
                    temp = null;
                }
            }
        }

        @SpirePatch(
                clz = ProceedButton.class,
                method = "update"
        )
        public static class TransitionToBossChest {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("goToVictoryRoomOrTheDoor")) {
                            m.replace(
                                    "{" +
                                            "if (!" + AnomalyModDungeonData.class.getName() + ".goToAnomalyAct4) {$_ = $proceed($$);}" +
                                            "}");
                        }
                    }
                };
            }
        }

        @SpirePatch(
                clz = TreasureRoomBoss.class,
                method = "getNextDungeonName"
        )
        public static class SetNextDungeon {

            public static String Postfix(String __result, TreasureRoomBoss __instance) {
                if (AnomalyModDungeonData.goToAnomalyAct4 && AbstractDungeon.id.equals(TheBeyond.ID)) {
                    __result = AnomalyTheEnding.ID;
                }
                return __result;
            }
        }
    }
}
