package AnomalyMod.patches.misc;

import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import javassist.CtBehavior;

import java.util.ArrayList;

public class StartNewRunPatch {

    @SpirePatch(
            clz = Exordium.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractPlayer.class,
                    ArrayList.class
            }
    )
    public static class NewRun {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(Exordium __instance, AbstractPlayer p, ArrayList<String> emptyList) {
            AnomalyModDungeonData.resetTheEndComponents();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Exordium.class, "initializeSpecialOneTimeEventList");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "generateSeeds"
    )
    public static class GenerateSeeds {

        public static void Postfix() {
            AnomalyModDungeonData.generateSeeds();
        }
    }
}
