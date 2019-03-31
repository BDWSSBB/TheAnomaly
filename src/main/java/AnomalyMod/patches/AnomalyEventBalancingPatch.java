package AnomalyMod.patches;

import AnomalyMod.character.AnomalyCharacter;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.city.CursedTome;
import com.megacrit.cardcrawl.events.exordium.GoldenWing;
import com.megacrit.cardcrawl.events.exordium.GoopPuddle;
import com.megacrit.cardcrawl.events.exordium.ScrapOoze;
import javassist.CtBehavior;

public class AnomalyEventBalancingPatch {

    @SpirePatch(
            clz = ScrapOoze.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class ScrapOozePatch {

        private static final int ANOMALY_SCRAP_OOZE_DAMAGE = 2;
        private static final int ASC_ANOMALY_SCRAP_OOZE_DAMAGE = 3;

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(ScrapOoze __instance) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                if (AbstractDungeon.ascensionLevel >= 15) {
                    ReflectionHacks.setPrivate(__instance, ScrapOoze.class, "dmg", ASC_ANOMALY_SCRAP_OOZE_DAMAGE);
                }
                else {
                    ReflectionHacks.setPrivate(__instance, ScrapOoze.class, "dmg", ANOMALY_SCRAP_OOZE_DAMAGE);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "setDialogOption");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GoldenWing.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class WingStatuePatch {

        private static final int ANOMALY_WING_STATUE_DAMAGE = 5;

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GoldenWing __instance) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                ReflectionHacks.setPrivate(__instance, GoldenWing.class, "damage", ANOMALY_WING_STATUE_DAMAGE);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "setDialogOption");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GoopPuddle.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class WorldOfGoopPatch {

        private static final int ANOMALY_WORLD_OF_GOOP_DAMAGE = 8;

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GoopPuddle __instance) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                ReflectionHacks.setPrivate(__instance, GoopPuddle.class, "damage", ANOMALY_WORLD_OF_GOOP_DAMAGE);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "setDialogOption");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
    @SpirePatch(
            clz = CursedTome.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class CursedTomePatch {

        private static final int ANOMALY_CURSED_TOME_FINAL_DAMAGE = 6;
        private static final int ASC_ANOMALY_CURSED_TOME_FINAL_DAMAGE = 10;

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(CursedTome __instance) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                if (AbstractDungeon.ascensionLevel >= 15) {
                    ReflectionHacks.setPrivate(__instance, CursedTome.class, "finalDmg", ASC_ANOMALY_CURSED_TOME_FINAL_DAMAGE);
                }
                else {
                    ReflectionHacks.setPrivate(__instance, CursedTome.class, "finalDmg", ANOMALY_CURSED_TOME_FINAL_DAMAGE);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(GenericEventDialog.class, "setDialogOption");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
