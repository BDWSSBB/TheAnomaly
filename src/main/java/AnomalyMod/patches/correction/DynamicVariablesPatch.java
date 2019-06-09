package AnomalyMod.patches.correction;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.powers.AbstractAnomalyPower;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DynamicVariablesPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    public static class applyPowersPatch {

        public static void Prefix(AbstractCard __instance) {
            applyPowersToDynamicVariables(__instance);
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class calculateCardDamagePatch {

        public static void Prefix(AbstractCard __instance) {
            applyPowersToDynamicVariables(__instance);
        }
    }

    private static void applyPowersToDynamicVariables(AbstractCard card) {
        if (card instanceof AbstractAnomalyCard) {
            ((AbstractAnomalyCard) card).secondMagicNumber = ((AbstractAnomalyCard) card).baseSecondMagicNumber;
            float cardImprobability = ((AbstractAnomalyCard) card).baseImprobabilityNumber;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof AbstractAnomalyPower) {
                    cardImprobability = ((AbstractAnomalyPower) p).atImprobabilityGive(cardImprobability);
                }
                if (p instanceof AbstractAnomalyTwoAmountPower) {
                    cardImprobability = ((AbstractAnomalyTwoAmountPower) p).atImprobabilityGive(cardImprobability);
                }
            }
            if ((int) cardImprobability != ((AbstractAnomalyCard) card).baseImprobabilityNumber) {
                ((AbstractAnomalyCard) card).isImprobabilityNumberModified = true;
            } else {
                ((AbstractAnomalyCard) card).isImprobabilityNumberModified = false;
            }
            ((AbstractAnomalyCard) card).improbabilityNumber = (int) cardImprobability;
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "resetAttributes"
    )
    public static class resetAttributesPatch {

        public static void Prefix(AbstractCard __instance) {
            if (__instance instanceof AbstractAnomalyCard) {
                ((AbstractAnomalyCard) __instance).improbabilityNumber = ((AbstractAnomalyCard) __instance).baseImprobabilityNumber;
                ((AbstractAnomalyCard) __instance).isImprobabilityNumberModified = false;
            }
        }
    }
}
