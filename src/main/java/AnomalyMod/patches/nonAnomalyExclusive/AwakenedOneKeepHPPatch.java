package AnomalyMod.patches.nonAnomalyExclusive;

import AnomalyMod.character.AnomalyCharacter;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;

@SpirePatch(
        clz = AwakenedOne.class,
        method = "changeState"
)
public class AwakenedOneKeepHPPatch {

    private static int retVal = 0;

    public static void Prefix(AwakenedOne __instance, String key) {
        if (key.equals("REBIRTH")) {
            retVal = __instance.maxHealth;
        }
    }

    public static void Postfix(AwakenedOne __instance, String key) {
        // Containing to Anomaly for now.
        if (AbstractDungeon.player instanceof AnomalyCharacter) {
            if (key.equals("REBIRTH") && __instance.maxHealth != retVal) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(__instance, __instance, retVal - __instance.maxHealth));
                __instance.maxHealth = retVal;
            }
        }
    }
}
