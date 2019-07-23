package AnomalyMod.patches.correction;

import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.blights.ToBeDeleted;
import AnomalyMod.blights.driveEffects.*;
import AnomalyMod.trololololo.AragoltsBane;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
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
        } else if (id.equals(PortableTerminal.ID)) {
            return SpireReturn.Return(new PortableTerminal());
        } else if (id.equals(Malfunction.ID)) {
            return SpireReturn.Return(new Malfunction());
        } else if (id.equals(RandomBuffs.ID)) {
            return SpireReturn.Return(new RandomBuffs());
        } else if (id.equals(FattenUp.ID)) {
            return SpireReturn.Return(new FattenUp());
        } else if (id.equals(Specialty.ID)) {
            return SpireReturn.Return(new Specialty());
        } else if (id.equals(SurpriseElites.ID)) {
            return SpireReturn.Return(new SurpriseElites());
        } else if (id.equals(ToBeDeleted.ID)) {
            return SpireReturn.Return(new ToBeDeleted());
        } else if (id.equals(AragoltsBane.ID)) {
            return SpireReturn.Return(new AragoltsBane());
        }
        return SpireReturn.Continue();
    }
}
