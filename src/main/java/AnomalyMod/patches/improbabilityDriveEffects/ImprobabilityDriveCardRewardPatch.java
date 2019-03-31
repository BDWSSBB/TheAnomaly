package AnomalyMod.patches.improbabilityDriveEffects;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.ImprobabilityDrive;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.PrismaticShard;
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
            if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID) && ((ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID)).convertingGoldToCard) {
//                if (!__result.isEmpty()) {
//                    __result.remove(0);
//                }
//                // Too lazy to do something better. Sorry.
//                // TODO: Move the less cards stuff to some patch.
//                if (!__result.isEmpty()) {
//                    __result.remove(0);
//                }
                for (int i = 0; i < 2; i++) {
                    if (!__result.isEmpty()) {
                        __result.remove(AnomalyMod.anomalyRNG.random(0, __result.size() - 1));
                    }
                }
                if (((ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID)).cardRewardStrength >= 25) {
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
    public static class GetAnyColor {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("hasRelic")) {
                        m.replace(
                                "{" +
                                        "if (" + Nested.class.getName() + ".isCorrectMethod($$)" +
                                        " && " + Nested.class.getName() + ".shouldGetAnyColor()) {" +
                                        "$_ = true;" +
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

            public static boolean isCorrectMethod(String relicID) {
                if (relicID.equals(PrismaticShard.ID)) {
                    return true;
                }
                else {
                    return false;
                }
            }

            public static boolean shouldGetAnyColor() {
                if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID) && ((ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID)).convertingGoldToCard) {
                    //return true;
                    return false;
                }
                else {
                    return false;
                }
            }
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
                if (
                        AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID) &&
                                ((ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID)).convertingGoldToCard &&
                                ((ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID)).cardRewardStrength >= 50) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
    }
}
