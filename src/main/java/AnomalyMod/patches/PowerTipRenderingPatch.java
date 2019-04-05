package AnomalyMod.patches;

import AnomalyMod.character.AnomalyCharacter;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

// This is experimental, so I'm containing it to Anomaly. Refer to this code at your own risk.
public class PowerTipRenderingPatch {

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderPowerTips"
    )
    public static class PartOne {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"shift", "offset"}
        )
        public static void Insert(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips, @ByRef boolean[] shift, @ByRef float[] offset) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                shift[0] = false;
                offset[0] = 0.0F;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "scale");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                found[found.length - 2] -= 1;
                return new int[]{found[found.length - 2]};
            }
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderPowerTips"
    )
    public static class PartTwo {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"shift", "offset"}
        )
        public static void Insert(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips, @ByRef boolean[] shift, @ByRef float[] offset) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                if (offset[0] > 320.0F * Settings.scale) {
                    shift[0] = false;
                }
                else {
                    shift[0] = true;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "scale");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 3]};
            }
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "queuePowerTips"
    )
    public static class PartThree {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(float x, float y, final ArrayList<PowerTip> powerTips) {
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                float originalValue = (float) ReflectionHacks.getPrivateStatic(TipHelper.class, "drawY");
                ReflectionHacks.setPrivateStatic(TipHelper.class, "drawY", originalValue + powerTips.size() * 20.0f * Settings.scale);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(TipHelper.class, "drawY");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }
    }

    public static class PartFour {

        @SpirePatch(
                clz = AbstractCreature.class,
                method = "renderPowerTips"
        )
        public static class AbstractCreatureFix {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("queuePowerTips")) {
                            m.replace(
                                    "{" +
                                            "if (" + Nested.class.getName() + ".doingExperiment()) {" +
                                            "$_ = $proceed($1, this.hb.cY + " + Nested.class.getName() + ".calculateAdditionalOffset($3), $3);" +
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
        }

        @SpirePatch(
                clz = AbstractMonster.class,
                method = "renderTip"
        )
        public static class AbstractMonsterFix {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("queuePowerTips")) {
                            m.replace(
                                    "{" +
                                            "if (" + Nested.class.getName() + ".doingExperiment()) {" +
                                            "$_ = $proceed($1, this.hb.cY + " + Nested.class.getName() + ".calculateAdditionalOffset($3), $3);" +
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
        }

        public static class Nested {

            public static boolean doingExperiment() {
                if (AbstractDungeon.player instanceof AnomalyCharacter) {
                    return true;
                }
                else {
                    return false;
                }
            }

            public static float calculateAdditionalOffset(ArrayList<PowerTip> powerTips) {
                float currentOffset = 0.0F;
                float offsetToUse = 0.0F;
                for (PowerTip p : powerTips) {
                    currentOffset += -FontHelper.getSmartHeight(FontHelper.tipBodyFont, p.body, 280.0f * Settings.scale, 26.0f * Settings.scale) - 7.0f * Settings.scale + 32.0f * Settings.scale;
                    if (currentOffset > offsetToUse) {
                        offsetToUse = currentOffset;
                    }
                    if (currentOffset > 320.0F * Settings.scale) {
                        currentOffset = 0.0F;
                    }
                }
                return offsetToUse;
            }
        }
    }
}
