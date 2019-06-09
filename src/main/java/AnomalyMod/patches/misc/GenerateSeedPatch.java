package AnomalyMod.patches.misc;

import AnomalyMod.AnomalyMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "generateSeeds"
)
public class GenerateSeedPatch {

    public static void Postfix() {
        AnomalyMod.generateSeeds();
    }
}
