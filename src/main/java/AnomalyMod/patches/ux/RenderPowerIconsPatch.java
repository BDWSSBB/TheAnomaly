package AnomalyMod.patches.ux;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.helpers.interfaces.RandomBuff;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "renderPowerIcons"
)
public class RenderPowerIconsPatch {

    private static final int NORMAL_POWERS_TILL_TWO_ROWS = 6;
    private static int improbabilityBuffsToDo = 0;
    private static int improbabilityBuffsHandled = 0;
    private static int normalBuffsToDo = 0;
    private static int normalBuffsHandled = 0;

    public static void Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
        improbabilityBuffsToDo = 0;
        improbabilityBuffsHandled = 0;
        normalBuffsToDo = 0;
        normalBuffsHandled = 0;
        for (AbstractPower p : __instance.powers) {
            if (p instanceof RandomBuff) {
                improbabilityBuffsToDo++;
            } else {
                normalBuffsToDo++;
            }
        }
    }

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
        improbabilityBuffsHandled = 0;
        normalBuffsHandled = 0;
    }

    private static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "scale");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[found.length - 3]};
        }
    }

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("renderIcons") || m.getMethodName().equals("renderAmount")) {
                    m.replace(
                            "{" +
                                    "if (" + Nested.class.getName() + ".renderExperimentalDisplay(this)) {" +
                                    "$_ = $proceed($1, $2 + " + Nested.class.getName() + ".calculateAdditionalXOffset(this, $0)," +
                                    "$3 + " + Nested.class.getName() + ".calculateAdditionalYOffset(this, $0), $4);" +
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

        public static boolean renderExperimentalDisplay(AbstractCreature creature) {
            if (creature instanceof AnomalyCharacter) {
                return true;
            } else {
                return false;
            }
        }

        public static float calculateAdditionalXOffset(AbstractCreature creature, AbstractPower p) {
            if (p instanceof RandomBuff) {
                improbabilityBuffsHandled++;
                return -48f * Settings.scale * (improbabilityBuffsHandled + normalBuffsHandled - 1);

            } else {
                normalBuffsHandled++;
                if (normalBuffsToDo > NORMAL_POWERS_TILL_TWO_ROWS) {
                    if (normalBuffsHandled <= (normalBuffsToDo + 1) / 2) {
                        return -48f * Settings.scale * improbabilityBuffsHandled;
                    } else {
                        return -48f * Settings.scale * (improbabilityBuffsHandled + ((normalBuffsToDo + 1) / 2));
                    }
                } else {
                    return -48f * Settings.scale * improbabilityBuffsHandled;
                }
            }
        }

        public static float calculateAdditionalYOffset(AbstractCreature creature, AbstractPower p) {
            if (p instanceof RandomBuff) {
                return 48f * Settings.scale * (improbabilityBuffsToDo - improbabilityBuffsHandled + 2);
            } else {
                if (normalBuffsToDo > NORMAL_POWERS_TILL_TWO_ROWS) {
                    if (normalBuffsHandled <= (normalBuffsToDo + 1) / 2) {
                        return 0.0F;
                    } else {
                        return -48f * Settings.scale;
                    }
                } else {
                    return 0.0F;
                }
            }
        }
    }
}
