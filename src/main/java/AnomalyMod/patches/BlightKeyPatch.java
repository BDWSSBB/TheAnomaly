package AnomalyMod.patches;

import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.blights.improbabilityDriveInfo.*;
import AnomalyMod.trololololo.AragoltsBane;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;

@SpirePatch(
        clz = BlightHelper.class,
        method = "getBlight"
)
public class BlightKeyPatch {

    public static SpireReturn<AbstractBlight> Prefix(String id) {
        if (id.equals(ImprobabilityDrive.ID)) {
            return SpireReturn.Return(new ImprobabilityDrive());
        }
        else if (id.equals(ImprobabilityDrivePortableTerminalInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDrivePortableTerminalInfo());
        }
        else if (id.equals(ImprobabilityDriveMalfunctionInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveMalfunctionInfo());
        }
        else if (id.equals(ImprobabilityDriveRandomBuffsInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveRandomBuffsInfo());
        }
        else if (id.equals(ImprobabilityDriveTryNewThingsInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveTryNewThingsInfo());
        }
        else if (id.equals(ImprobabilityDriveSpecialtyInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveSpecialtyInfo());
        }
        else if (id.equals(ImprobabilityDriveSurpriseElitesInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveSurpriseElitesInfo());
        }
        else if (id.equals(ImprobabilityDriveFutureInvadersInfo.ID)) {
            return SpireReturn.Return(new ImprobabilityDriveFutureInvadersInfo());
        }
        else if (id.equals(AragoltsBane.ID)) {
            return SpireReturn.Return(new AragoltsBane());
        }
        return SpireReturn.Continue();
    }
}
