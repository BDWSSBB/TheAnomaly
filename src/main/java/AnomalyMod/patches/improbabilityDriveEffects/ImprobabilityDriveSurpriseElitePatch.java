package AnomalyMod.patches.improbabilityDriveEffects;

import AnomalyMod.blights.improbabilityDriveInfo.ImprobabilityDriveSurpriseElitesInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "nextRoomTransition",
        paramtypez = {
                SaveFile.class
        }
)
public class ImprobabilityDriveSurpriseElitePatch {

    public static void Prefix(AbstractDungeon __instance, SaveFile savefile) {
        ImprobabilityDriveSurpriseElitesInfo.rollSurpriseElite();
    }
}
