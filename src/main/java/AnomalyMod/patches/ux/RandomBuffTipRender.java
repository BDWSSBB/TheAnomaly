package AnomalyMod.patches.ux;

import AnomalyMod.helpers.interfaces.RandomBuff;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class RandomBuffTipRender {

    public static boolean processingRandomBuff = false;
    public static boolean coloringBox = false;

    @SpirePatch(
            clz = PowerTip.class,
            method = SpirePatch.CLASS
    )
    public static class PowerTipTypeField {

        public static SpireField<Boolean> isRandomBuff = new SpireField<>(() -> false);
    }

    public static class PowerTipConstructorPatches {

        public static void setIsRandomBuffField(PowerTip tip) {
            PowerTipTypeField.isRandomBuff.set(tip, processingRandomBuff);
        }

        @SpirePatch(
                clz = PowerTip.class,
                method = SpirePatch.CONSTRUCTOR,
                paramtypez = {
                        String.class,
                        String.class
                }
        )
        public static class ConstructorOne {

            public static void Postfix(PowerTip __instance, String header, String body) {
                setIsRandomBuffField(__instance);
            }
        }

        @SpirePatch(
                clz = PowerTip.class,
                method = SpirePatch.CONSTRUCTOR,
                paramtypez = {
                        String.class,
                        String.class,
                        Texture.class
                }
        )
        public static class ConstructorTwo {

            public static void Postfix(PowerTip __instance, String header, String body, Texture img) {
                setIsRandomBuffField(__instance);
            }
        }

        @SpirePatch(
                clz = PowerTip.class,
                method = SpirePatch.CONSTRUCTOR,
                paramtypez = {
                        String.class,
                        String.class,
                        TextureAtlas.AtlasRegion.class
                }
        )
        public static class ConstructorThree {

            public static void Postfix(PowerTip __instance, String header, String body, TextureAtlas.AtlasRegion region48) {
                setIsRandomBuffField(__instance);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderPowerTips"
    )
    public static class FindPlayerRandomBuffs {

        @SpireInsertPatch(
                locator = LocatorOne.class,
                localvars = {"p"}
        )
        public static void InsertOne(AbstractCreature __instance, SpriteBatch sb, AbstractPower p) {
            if (p instanceof RandomBuff) {
                processingRandomBuff = true;
            } else {
                processingRandomBuff = false;
            }
        }

        private static class LocatorOne extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        @SpireInsertPatch(
                locator = LocatorTwo.class
        )
        public static void InsertTwo(AbstractCreature __instance, SpriteBatch sb) {
            processingRandomBuff = false;
        }

        private static class LocatorTwo extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "renderTip"
    )
    public static class FindMonsterRandomBuffs {

        @SpireInsertPatch(
                locator = LocatorOne.class,
                localvars = {"p"}
        )
        public static void InsertOne(AbstractMonster __instance, SpriteBatch sb, AbstractPower p) {
            if (p instanceof RandomBuff) {
                processingRandomBuff = true;
            } else {
                processingRandomBuff = false;
            }
        }

        private static class LocatorOne extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 2], found[found.length - 1]};
            }
        }

        @SpireInsertPatch(
                locator = LocatorTwo.class
        )
        public static void InsertTwo(AbstractMonster __instance, SpriteBatch sb) {
            processingRandomBuff = false;
        }

        private static class LocatorTwo extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderPowerTips"
    )
    public static class FindCorrectTips {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tip"}
        )
        public static void Insert(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips, PowerTip tip) {
            if (PowerTipTypeField.isRandomBuff.get(tip)) {
                coloringBox = true;
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TipHelper.class, "renderTipBox");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderTipBox"
    )
    public static class ChangeColor {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(float x, float y, SpriteBatch sb, String title, String description) {
            if (coloringBox) {
                coloringBox = false;
                sb.setColor(Color.PURPLE.cpy());
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[3]};
            }
        }
    }
}
