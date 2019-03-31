package AnomalyMod.patches;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

// Rn this is only for atDamageReceive, but the same idea carries over for the other places you would patch.
@SpirePatch(
        clz = AbstractCard.class,
        method = "calculateCardDamage"
)
public class ImBetterThanHeavyBladePatch {

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("atDamageReceive")) {
                    m.replace(
                            "{" +
                                    "if (" + Nested.class.getName() + ".isAnomalyCard(this)) {" +
                                    "$_ = " + Nested.class.getName() + ".doMagic(this, $$, $0);" +
                                    "}" +
                                    "else" +
                                    "{" +
                                    "$_ = $proceed($$);" +
                                    "}" +
                                    "}");
                }
            }
        };
    }

    public static class Nested {

        public static boolean isAnomalyCard(AbstractCard c) {
            if (c instanceof AbstractAnomalyCard) {
                return true;
            }
            else {
                return false;
            }
        }

        public static float doMagic(AbstractCard c, float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower p) {
            if (c instanceof AbstractAnomalyCard) {
                return ((AbstractAnomalyCard) c).atDamageReceiveWithPower(tmp, damageTypeForTurn, p);
            }
            else {
                return p.atDamageReceive(tmp, damageTypeForTurn);
            }
        }
    }
}
