package AnomalyMod.patches.correction;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

@SpirePatch(
        clz = AbstractCard.class,
        method = "makeStatEquivalentCopy"
)
public class MakeStatEquivalentCopyFieldCopyPatch {

    public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
        __result.exhaust = __instance.exhaust;
        __result.type = __instance.type;
        if (__instance instanceof AbstractAnomalyCard && __result instanceof AbstractAnomalyCard) {
            ((AbstractAnomalyCard) __result).loadCardImage(((AbstractAnomalyCard) __instance).textureImg);
            ((AbstractAnomalyCard) __result).textureImg = ((AbstractAnomalyCard) __instance).textureImg;
            ((AbstractAnomalyCard) __result).baseImprobabilityNumber = ((AbstractAnomalyCard) __instance).baseImprobabilityNumber;
            ((AbstractAnomalyCard) __result).baseSecondMagicNumber = ((AbstractAnomalyCard) __instance).baseSecondMagicNumber;
        }
        return __result;
    }
}

