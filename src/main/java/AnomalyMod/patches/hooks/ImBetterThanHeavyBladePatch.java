package AnomalyMod.patches.hooks;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ImBetterThanHeavyBladePatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class AtDamageGiveApplyPowersHook {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("atDamageGive")) {
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
                } else {
                    return false;
                }
            }

            public static float doMagic(AbstractCard c, float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower p) {
                if (c instanceof AbstractAnomalyCard) {
                    return ((AbstractAnomalyCard) c).atDamageGiveWithPower(tmp, damageTypeForTurn, p);
                } else {
                    // These should never actually be called, but they're here just in case.
                    return p.atDamageGive(tmp, damageTypeForTurn);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class AtDamageReceiveHook {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("atDamageReceive")) {
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
                } else {
                    return false;
                }
            }

            public static float doMagic(AbstractCard c, float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower p) {
                if (c instanceof AbstractAnomalyCard) {
                    return ((AbstractAnomalyCard) c).atDamageReceiveWithPower(tmp, damageTypeForTurn, p);
                } else {
                    return p.atDamageReceive(tmp, damageTypeForTurn);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowers"
    )
    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class AtDamageFinalGiveApplyPowersHook {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("atDamageFinalGive")) {
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
                } else {
                    return false;
                }
            }

            public static float doMagic(AbstractCard c, float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower p) {
                if (c instanceof AbstractAnomalyCard) {
                    return ((AbstractAnomalyCard) c).atDamageFinalGiveWithPower(tmp, damageTypeForTurn, p);
                } else {
                    return p.atDamageFinalGive(tmp, damageTypeForTurn);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "calculateCardDamage"
    )
    public static class AtDamageFinalReceiveHook {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("atDamageFinalReceive")) {
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
                } else {
                    return false;
                }
            }

            public static float doMagic(AbstractCard c, float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower p) {
                if (c instanceof AbstractAnomalyCard) {
                    return ((AbstractAnomalyCard) c).atDamageFinalReceiveWithPower(tmp, damageTypeForTurn, p);
                } else {
                    return p.atDamageFinalReceive(tmp, damageTypeForTurn);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "applyPowersToBlock"
    )
    public static class ModifyBlockHook {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("modifyBlock")) {
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
                } else {
                    return false;
                }
            }

            public static float doMagic(AbstractCard c, float tmp, AbstractPower p) {
                if (c instanceof AbstractAnomalyCard) {
                    return ((AbstractAnomalyCard) c).modifyBlockWithPower(tmp, p);
                } else {
                    return p.modifyBlock(tmp);
                }
            }
        }
    }
}
