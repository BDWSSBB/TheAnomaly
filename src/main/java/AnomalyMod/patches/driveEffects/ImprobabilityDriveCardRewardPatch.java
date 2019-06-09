package AnomalyMod.patches.driveEffects;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.improbabilityDriveInfo.ImprobabilityDriveTryNewThingsInfo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class ImprobabilityDriveCardRewardPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class ConvertGoldToCardRemoveOneCard {

        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result) {
            if (ImprobabilityDriveTryNewThingsInfo.convertingGoldToCard) {
                for (int i = 0; i < 2; i++) {
                    if (!__result.isEmpty()) {
                        __result.remove(AnomalyMod.anomalyRNG.random(0, __result.size() - 1));
                    }
                }
                if (ImprobabilityDriveTryNewThingsInfo.cardRewardStrength >= 25) {
                    for (AbstractCard c : __result) {
                        c.upgrade();
                    }
                }
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getRewardCards"
    )
    public static class GuaranteeRareCard {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("rollRarity")) {
                        m.replace(
                                "{" +
                                        "if (" + Nested.class.getName() + ".shouldGetRareCard()) {" +
                                        "$_ = " + AbstractCard.CardRarity.class.getName() + ".RARE;" +
                                        "}" +
                                        "else {" +
                                        "$_ = $proceed($$);" +
                                        "}" +
                                        "}");
                    }
                }
            };
        }

        public static class Nested {

            public static boolean shouldGetRareCard() {
                if (ImprobabilityDriveTryNewThingsInfo.cardRewardStrength >= 50) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
