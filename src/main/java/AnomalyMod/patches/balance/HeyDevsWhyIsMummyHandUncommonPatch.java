package AnomalyMod.patches.balance;

import AnomalyMod.character.AnomalyCharacter;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MummifiedHand;

@SpirePatch(
        clz = MummifiedHand.class,
        method = SpirePatch.CONSTRUCTOR
)
public class HeyDevsWhyIsMummyHandUncommonPatch {

    public static void Postfix(MummifiedHand __instance) {
        if (AbstractDungeon.player instanceof AnomalyCharacter) {
            __instance.tier = AbstractRelic.RelicTier.RARE;
        }
    }
}
