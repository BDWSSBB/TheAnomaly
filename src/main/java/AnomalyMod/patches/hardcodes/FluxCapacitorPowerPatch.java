package AnomalyMod.patches.hardcodes;

import AnomalyMod.cards.wistful.FluxCapacitor;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz = EnergyManager.class,
        method = "recharge"
)
public class FluxCapacitorPowerPatch {

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("setEnergy")) {
                    m.replace(
                            "{" +
                                    Nested.class.getName() + ".spudPowerLOL($$);" +
                                    "if (!" + Nested.class.getName() + ".hadPotatoPower()) {$_ = $proceed($$);}" +
                                    "}");
                }
            }
        };
    }

    public static class Nested {

        public static void spudPowerLOL(int energyPerTurn) { // POTATOES
            if (AbstractDungeon.player.hasPower(FluxCapacitor.ID)) {
                if (EnergyPanel.totalCount > AbstractDungeon.player.getPower(FluxCapacitor.ID).amount) {
                    EnergyPanel.addEnergy(energyPerTurn + AbstractDungeon.player.getPower(FluxCapacitor.ID).amount - EnergyPanel.totalCount);
                } else {
                    EnergyPanel.addEnergy(energyPerTurn);
                }
            }
        }

        public static boolean hadPotatoPower() { // POE TAY TOHS
            if (AbstractDungeon.player.hasPower(FluxCapacitor.ID)) { // There's a slightly better way to check it, but it's good enough.
                return true;
            } else {
                return false;
            }
        }
    }
}
