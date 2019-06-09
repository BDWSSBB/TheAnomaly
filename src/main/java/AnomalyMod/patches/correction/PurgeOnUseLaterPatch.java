package AnomalyMod.patches.correction;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

// Primarily used for making sure certain cards are played twice.
public class PurgeOnUseLaterPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class PurgeOnUseLaterField {

        public static SpireField<Boolean> anomalyModPurgeOnUseLater = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    AbstractCreature.class
            }
    )
    public static class SetPurgeOnUseAtCorrectTime {

        public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (PurgeOnUseLaterField.anomalyModPurgeOnUseLater.get(card)) {
                card.purgeOnUse = true;
            }
        }
    }
}
