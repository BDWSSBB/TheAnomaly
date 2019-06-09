package AnomalyMod.patches.hooks;

import AnomalyMod.powers.AbstractAnomalyPower;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import AnomalyMod.powers.FixedVanillaPowers.FixedInvinciblePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

public class WhatBufferShouldHaveBeenLikePatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "damage"
    )
    public static class DamagePlayerPatches {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"damageAmount"}
        )
        public static void Insert(AbstractPlayer __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractPower p : __instance.powers) {
                if (p instanceof AbstractAnomalyPower) {
                    damageAmount[0] = ((AbstractAnomalyPower) p).onAttackedButCanActuallyChangeNumbers(info, damageAmount[0]);
                }
                if (p instanceof AbstractAnomalyTwoAmountPower) {
                    damageAmount[0] = ((AbstractAnomalyTwoAmountPower) p).onAttackedButCanActuallyChangeNumbers(info, damageAmount[0]);
                }
                if (p instanceof FixedInvinciblePower) {
                    damageAmount[0] = ((FixedInvinciblePower) p).whyMustIBabysitYouWithYourOwnOnAttackedMethod(info, damageAmount[0]);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "decrementBlock");
                int[] found = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                found[0] += 1;
                return found;
            }
        }
    }

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "damage"
    )
    public static class DamageMonsterPatches {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"damageAmount"}
        )
        public static void Insert(AbstractMonster __instance, DamageInfo info, @ByRef int[] damageAmount) {
            for (AbstractPower p : __instance.powers) {
                if (p instanceof AbstractAnomalyPower) {
                    damageAmount[0] = ((AbstractAnomalyPower) p).onAttackedButCanActuallyChangeNumbers(info, damageAmount[0]);
                }
                if (p instanceof AbstractAnomalyTwoAmountPower) {
                    damageAmount[0] = ((AbstractAnomalyTwoAmountPower) p).onAttackedButCanActuallyChangeNumbers(info, damageAmount[0]);
                }
                if (p instanceof FixedInvinciblePower) {
                    damageAmount[0] = ((FixedInvinciblePower) p).whyMustIBabysitYouWithYourOwnOnAttackedMethod(info, damageAmount[0]);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "decrementBlock");
                int[] found = LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                found[0] += 1;
                return found;
            }
        }
    }
}
