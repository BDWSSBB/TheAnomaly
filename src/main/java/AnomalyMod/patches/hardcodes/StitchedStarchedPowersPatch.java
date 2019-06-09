package AnomalyMod.patches.hardcodes;

import AnomalyMod.AnomalyMod;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StarchedPower;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StitchedPower;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.RenderFixSwitches;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CtBehavior;

public class StitchedStarchedPowersPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    // :angery:
    @SpirePatch(
            clz = RenderFixSwitches.RenderEnergySwitch.class,
            method = "Prefix"
    )
    public static class renderPurpleCostBaseModVersion {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"costColor"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor) {
            if (__instance.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasPower(StitchedPower.POWER_ID) && AbstractDungeon.player.getPower(StitchedPower.POWER_ID).amount == 1) {
                costColor[0] = Color.PURPLE.cpy();
            }
            if (__instance.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasPower(StarchedPower.POWER_ID) && AbstractDungeon.player.getPower(StarchedPower.POWER_ID).amount == 1) {
                costColor[0] = Color.PURPLE.cpy();
            }
        }

        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderRotatedText");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "hasEnoughEnergy"
    )
    public static class makeCardPlayable {

        public static boolean Postfix(boolean __result, AbstractCard __instance) {
            if (__instance.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasPower(StitchedPower.POWER_ID) && AbstractDungeon.player.getPower(StitchedPower.POWER_ID).amount == 1) {
                __result = true;
            }
            if (__instance.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasPower(StarchedPower.POWER_ID) && AbstractDungeon.player.getPower(StarchedPower.POWER_ID).amount == 1) {
                __result = true;
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = UseCardAction.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {
                    AbstractCard.class,
                    AbstractCreature.class
            }
    )
    public static class SetFreeToPlayOnceAtCorrectTime {

        public static void Prefix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
            if (card.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasPower(StitchedPower.POWER_ID) && AbstractDungeon.player.getPower(StitchedPower.POWER_ID).amount == 1) {
                card.freeToPlayOnce = true;
            }
            if (card.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasPower(StarchedPower.POWER_ID) && AbstractDungeon.player.getPower(StarchedPower.POWER_ID).amount == 1) {
                card.freeToPlayOnce = true;
            }
        }
    }
}
