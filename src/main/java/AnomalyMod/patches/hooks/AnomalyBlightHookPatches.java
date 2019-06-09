package AnomalyMod.patches.hooks;

import AnomalyMod.blights.AbstractAnomalyBlight;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;

public class AnomalyBlightHookPatches {

    @SpirePatch(
            clz = CombatRewardScreen.class,
            method = "setupItemReward"
    )
    public static class OnSetupItemReward {

        public static void Postfix(CombatRewardScreen __instance) {
            for (AbstractBlight b : AbstractDungeon.player.blights) {
                if (b instanceof AbstractAnomalyBlight) {
                    ((AbstractAnomalyBlight) b).onSetupItemRewards();
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {
                    SaveFile.class
            }
    )
    public static class OnEnterRoom {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            for (AbstractBlight b : AbstractDungeon.player.blights) {
                if (b instanceof AbstractAnomalyBlight) {
                    ((AbstractAnomalyBlight) b).onEnterRoom(AbstractDungeon.nextRoom.room);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "onEnterRoom");
                int[] found = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                found[0] += 2;
                return found;
            }
        }
    }
}
