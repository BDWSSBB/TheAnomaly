package AnomalyMod.patches.driveEffects;

import AnomalyMod.blights.driveEffects.FattenUp;
import AnomalyMod.blights.driveEffects.Specialty;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class ImprobabilityDriveSaveAndLoadPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractRoom __instance) {
        FattenUp.convertGoldToRewards();
        Specialty.specializeCardRewards();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "loading_post_combat");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[found.length - 2]};
        }
    }
}
