package AnomalyMod.patches;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.relics.AbstractAnomalyRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfTurnPostDrawRelics"
)
public class ApplyStartOfTurnPostPostDrawRelicsPatch{ // Only one way to trump this. lol

    public static void Postfix(AbstractPlayer __instance) {
        for (AbstractRelic r : __instance.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                ((AbstractAnomalyRelic) r).atStartOfTurnPostPostDraw();
            }
        }
        for (AbstractBlight b : __instance.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                ((AbstractAnomalyBlight) b).atStartOfTurnPostPostDraw();
            }
        }
    }
}
