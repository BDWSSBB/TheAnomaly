package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.DungeonMap;
import javassist.CtBehavior;

public class GetDungeonToWorkPatch {

    public static class DungeonMapPatches {

        @SpirePatch(
                clz = DungeonMap.class,
                method = "update"
        )
        public static class WhyAmIGoingToTheBoss {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(DungeonMap __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && __instance.bossHb.hovered) {
                    __instance.bossHb.hovered = false;
                }
            }

            private static class Locator extends SpireInsertLocator {
                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "hovered");
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                }
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "calculateMapSize"
        )
        public static class CalculateMapSize {

            public static float Postfix(float __result, DungeonMap __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    __result = Settings.MAP_DST_Y * (AbstractDungeon.FINAL_ACT_MAP_HEIGHT + 1) - 1380f * Settings.scale;
                }
                return __result;
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "render"
        )
        public static class Render {

            // It's just easier to trick the system.
            private static String tempID;

            public static void Prefix(DungeonMap __instance, SpriteBatch sb) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                   tempID = AbstractDungeon.id;
                   AbstractDungeon.id = TheEnding.ID;
                }
            }

            public static void Postfix(DungeonMap __instance, SpriteBatch sb) {
                if (tempID != null) {
                    AbstractDungeon.id = tempID;
                    tempID = null;
                }
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "renderBossIcon"
        )
        public static class RenderBossIcon {

            public static SpireReturn<Void> Prefix(DungeonMap __instance, SpriteBatch sb) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }
    }
}
