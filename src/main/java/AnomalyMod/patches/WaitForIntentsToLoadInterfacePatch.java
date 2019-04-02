package AnomalyMod.patches;

import AnomalyMod.actions.utility.NoFastModeWaitAction;
import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.helpers.WaitForIntentsToLoad;
import AnomalyMod.relics.AbstractAnomalyRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "applyStartOfTurnPowers"
)
public class WaitForIntentsToLoadInterfacePatch{

    private static final float INTENT_WAIT_TIME = 0.4F;

    public static void Postfix(AbstractCreature __instance) {
        if (__instance instanceof AbstractPlayer) {
            for (AbstractRelic r : ((AbstractPlayer) __instance).relics) {
                if (r instanceof AbstractAnomalyRelic) {
                    AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(INTENT_WAIT_TIME));
                    return;
                }
            }
            for (AbstractBlight b : ((AbstractPlayer) __instance).blights) {
                if (b instanceof AbstractAnomalyBlight) {
                    AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(INTENT_WAIT_TIME));
                    return;
                }
            }
        }
        for (AbstractPower p : __instance.powers) {
            if (p instanceof WaitForIntentsToLoad) {
                AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(INTENT_WAIT_TIME));
                return;
            }
        }
    }
}
