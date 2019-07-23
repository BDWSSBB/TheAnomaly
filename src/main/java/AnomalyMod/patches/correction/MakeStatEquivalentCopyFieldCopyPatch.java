package AnomalyMod.patches.correction;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractCard.class,
        method = "makeStatEquivalentCopy"
)
public class MakeStatEquivalentCopyFieldCopyPatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"card"}
    )
    public static void Insert(AbstractCard __instance, AbstractCard card) {
        card.exhaust = __instance.exhaust;
        card.type = __instance.type;
        if (__instance instanceof AbstractAnomalyCard && card instanceof AbstractAnomalyCard) {
            ((AbstractAnomalyCard) card).loadCardImage(((AbstractAnomalyCard) __instance).textureImg);
            ((AbstractAnomalyCard) card).textureImg = ((AbstractAnomalyCard) __instance).textureImg;
            ((AbstractAnomalyCard) card).baseImprobabilityNumber = ((AbstractAnomalyCard) __instance).baseImprobabilityNumber;
            ((AbstractAnomalyCard) card).baseSecondMagicNumber = ((AbstractAnomalyCard) __instance).baseSecondMagicNumber;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "name");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}

