package AnomalyMod.patches;

import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.trololololo.AragoltsBane;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.neow.NeowEvent;

@SpirePatch(
        clz = NeowEvent.class,
        method = "update"
)
public class ImprobabilityDriveSpawnPatch {

    public static void Prefix(NeowEvent __instance) {
        if (AbstractDungeon.player instanceof AnomalyCharacter && !AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID)) {
            ImprobabilityDrive drive = new ImprobabilityDrive();
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, drive);
            drive.checkForSpawnInfoBlights();
        }
    }
}
