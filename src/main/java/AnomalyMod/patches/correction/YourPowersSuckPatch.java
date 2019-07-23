package AnomalyMod.patches.correction;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.powers.FixedVanillaPowers.BetterNextTurnDrawPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        }
)
public class YourPowersSuckPatch {

    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (AbstractDungeon.player instanceof AnomalyCharacter) {
            AbstractPower power = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            if (power != null) {
                if (power.getClass().equals(DrawCardNextTurnPower.class)) {
                    AbstractCreature owner = power.owner;
                    int amount = power.amount;
                    ReflectionHacks.setPrivate(__instance, ApplyPowerAction.class, "powerToApply", new BetterNextTurnDrawPower(owner, amount));
                }
            }
        }
    }
}
