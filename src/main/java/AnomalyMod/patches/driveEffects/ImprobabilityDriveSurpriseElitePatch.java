package AnomalyMod.patches.driveEffects;

import AnomalyMod.blights.improbabilityDriveInfo.ImprobabilityDriveSurpriseElitesInfo;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "nextRoomTransition",
        paramtypez = {
                SaveFile.class
        }
)
public class ImprobabilityDriveSurpriseElitePatch {

    // Avoiding a save and load issue.
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
        ImprobabilityDriveSurpriseElitesInfo.rollSurpriseElite();
    }

    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "monsterHpRng");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
