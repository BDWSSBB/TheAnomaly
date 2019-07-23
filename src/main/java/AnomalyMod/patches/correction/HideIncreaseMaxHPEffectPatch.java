package AnomalyMod.patches.correction;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "increaseMaxHp"
)
public class HideIncreaseMaxHPEffectPatch {

    // I do not patch to adjust to the parameter showEffect because some mods (and the base game) rely on the method being broken.
    public static boolean hideEffect = false;

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("add")) {
                    m.replace(
                            "{" +
                                    "if (!" + HideIncreaseMaxHPEffectPatch.class.getName() + ".hideEffect) {$_ = $proceed($$);}" +
                                    "}");
                }
            }
        };
    }
}
