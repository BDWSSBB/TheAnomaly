package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DoorUnlockScreen;
import javassist.CtBehavior;

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

    // For now, we'll just replace the heart transition with the dungeon.
    @SpirePatch(
            clz = DoorUnlockScreen.class,
            method = "exit"
    )
    public static class ImplementDungeon {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(DoorUnlockScreen __instance) {
            if (false) {
                CardCrawlGame.nextDungeon = AnomalyTheEnding.ID;
            }
        }

        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "isDungeonBeaten");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
