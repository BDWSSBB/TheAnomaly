package AnomalyMod.patches.improbabilityDriveEffects;

import AnomalyMod.blights.ImprobabilityDrive;
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
        ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        if (drive != null && AbstractDungeon.nextRoom != null && AbstractDungeon.nextRoom.room != null) {
            drive.rollSurpriseElite();
        }
    }
}
