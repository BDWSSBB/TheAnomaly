package AnomalyMod.patches.hardcodes;

import AnomalyMod.powers.DisablePower;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class DisablePowerPatch {

    @SpirePatch(
            clz = VulnerablePower.class,
            method = "atDamageReceive"
    )
    public static class VulnerablePatch {

        public static float Postfix(float __result, VulnerablePower __instance, float damage, DamageInfo.DamageType type) {
            if (__instance.owner.hasPower(DisablePower.POWER_ID)) {
                __result += __result - damage;
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = VulnerablePower.class,
            method = "updateDescription"
    )
    public static class VulnerableDescriptionPatch {

        public static void Postfix(VulnerablePower __instance) {
            DisablePower power = (DisablePower) __instance.owner.getPower(DisablePower.POWER_ID);
            if (power != null) {
                if (power.amount == 1) {
                    __instance.description += DisablePower.DESCRIPTIONS[3] +
                            FontHelper.colorString(Integer.toString(MathUtils.round((int) (__instance.atDamageReceive(100, DamageInfo.DamageType.NORMAL) - 100))), "b") +
                            DisablePower.DESCRIPTIONS[4];
                } else {
                    __instance.description += DisablePower.DESCRIPTIONS[3] +
                            FontHelper.colorString(Integer.toString(MathUtils.round((int) (__instance.atDamageReceive(100, DamageInfo.DamageType.NORMAL) - 100))), "b") +
                            DisablePower.DESCRIPTIONS[5] + power.amount + DisablePower.DESCRIPTIONS[6];
                }
            }
        }
    }

    @SpirePatch(
            clz = WeakPower.class,
            method = "atDamageGive"
    )
    public static class WeakPatch {

        public static float Postfix(float __result, WeakPower __instance, float damage, DamageInfo.DamageType type) {
            if (__instance.owner.hasPower(DisablePower.POWER_ID)) {
                __result += __result - damage;
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = WeakPower.class,
            method = "updateDescription"
    )
    public static class WeakDescriptionPatch {

        public static void Postfix(WeakPower __instance) {
            DisablePower power = (DisablePower) __instance.owner.getPower(DisablePower.POWER_ID);
            if (power != null) {
                if (power.amount == 1) {
                    __instance.description += DisablePower.DESCRIPTIONS[7] +
                            FontHelper.colorString(Integer.toString(MathUtils.round((100 - __instance.atDamageGive(100, DamageInfo.DamageType.NORMAL)))), "b") +
                            DisablePower.DESCRIPTIONS[8];
                } else {
                    __instance.description += DisablePower.DESCRIPTIONS[7] +
                            FontHelper.colorString(Integer.toString(MathUtils.round((100 - __instance.atDamageGive(100, DamageInfo.DamageType.NORMAL)))), "b") +
                            DisablePower.DESCRIPTIONS[9] + power.amount + DisablePower.DESCRIPTIONS[10];
                }
            }
        }
    }
}
