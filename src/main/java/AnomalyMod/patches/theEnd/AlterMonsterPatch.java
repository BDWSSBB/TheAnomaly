package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Maw;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.powers.SplitPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class AlterMonsterPatch {

    public static class LargeSlimeSplitOption {

        public static boolean slimesShouldSplitSwitch = true;

        @SpirePatch(
                clz = AcidSlime_L.class,
                method = SpirePatch.CONSTRUCTOR,
                paramtypez = {
                        float.class,
                        float.class,
                        int.class,
                        int.class
                }
        )
        public static class AcidShouldSplit {

            public static void Postfix(AcidSlime_L __instance, float x, float y, int poisonAmount, int newHealth) {
                if (!slimesShouldSplitSwitch) {
                    __instance.powers.remove(__instance.getPower(SplitPower.POWER_ID));
                    ReflectionHacks.setPrivate(__instance, AcidSlime_L.class, "splitTriggered", true);
                }
            }
        }

        @SpirePatch(
                clz = SpikeSlime_L.class,
                method = SpirePatch.CONSTRUCTOR,
                paramtypez = {
                        float.class,
                        float.class,
                        int.class,
                        int.class
                }
        )
        public static class SpikeShouldSplit {

            public static void Postfix(SpikeSlime_L __instance, float x, float y, int poisonAmount, int newHealth) {
                if (!slimesShouldSplitSwitch) {
                    __instance.powers.remove(__instance.getPower(SplitPower.POWER_ID));
                    ReflectionHacks.setPrivate(__instance, SpikeSlime_L.class, "splitTriggered", true);
                }
            }
        }
    }

    @SpirePatch(
            clz = Maw.class,
            method = "getMove"
    )
    public static class GuaranteeMawSoftAttackTurnTwo {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn<Void> Insert(Maw __instance, int num) {
            if (AbstractDungeon.id != null && AbstractDungeon.id.equals(AnomalyTheEnding.ID) && !__instance.moveHistory.isEmpty() && __instance.moveHistory.get(__instance.moveHistory.size() - 1).equals((byte) 2)) {
                __instance.setMove((byte) 5, AbstractMonster.Intent.ATTACK, __instance.damage.get(1).base);
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Maw.class, "roared");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static class RemoveLagavulinMusic {

        @SpirePatch(
                clz = Lagavulin.class,
                method = "usePreBattleAction"
        )
        public static class StopLagavulinMusic {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("playTempBgmInstantly") || m.getMethodName().equals("unsilenceBGM")) {
                            m.replace(
                                    "{" +
                                            "if (!" + Nested.class.getName() + ".isInAnomalyAct()) {$_ = $proceed($$);}" +
                                            "}");
                        }
                    }
                };
            }
        }

        @SpirePatch(
                clz = Lagavulin.class,
                method = "die"
        )
        public static class KeepCurrentMusic {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("fadeOutTempBGM")) {
                            m.replace(
                                    "{" +
                                            "if (!" + Nested.class.getName() + ".isInAnomalyAct()) {$_ = $proceed($$);}" +
                                            "}");
                        }
                    }
                };
            }
        }

        public static class Nested {

            public static boolean isInAnomalyAct() {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}
