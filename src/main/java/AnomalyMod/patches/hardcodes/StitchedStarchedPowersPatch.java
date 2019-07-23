package AnomalyMod.patches.hardcodes;

import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StarchedPower;
import AnomalyMod.powers.ImprobabilityDriveExclusive.player.StitchedPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

public class StitchedStarchedPowersPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = "renderEnergy"
    )
    public static class renderPurpleCost {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"costColor"}
        )
        public static void Insert(AbstractCard __instance, SpriteBatch sb, @ByRef Color[] costColor) {
            if (AbstractDungeon.id != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (__instance.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasPower(StitchedPower.POWER_ID) && AbstractDungeon.player.getPower(StitchedPower.POWER_ID).amount == 1) {
                    costColor[0] = Color.PURPLE.cpy();
                }
                if (__instance.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasPower(StarchedPower.POWER_ID) && AbstractDungeon.player.getPower(StarchedPower.POWER_ID).amount == 1) {
                    costColor[0] = Color.PURPLE.cpy();
                }
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
            clz = AbstractPlayer.class,
            method = "useCard"
    )
    public static class SetFreeToPlayOnceAtCorrectTime {

        public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasPower(StitchedPower.POWER_ID) && AbstractDungeon.player.getPower(StitchedPower.POWER_ID).amount == 1) {
                c.freeToPlayOnce = true;
            }
            if (c.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasPower(StarchedPower.POWER_ID) && AbstractDungeon.player.getPower(StarchedPower.POWER_ID).amount == 1) {
                c.freeToPlayOnce = true;
            }
        }
    }
}
