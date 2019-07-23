package AnomalyMod.patches.correction;

import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.powers.DelayedPowerPower;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

import java.util.ArrayList;

public class ApplyDamagePowersSafelyPatch {

    public static boolean isEnemyTurn = false;

    @SpirePatch(
            clz = GameActionManager.class,
            method = "callEndOfTurnActions"
    )
    public static class RegisterEnemyTurnBegin {

        public static void Postfix(GameActionManager __instance) {
            isEnemyTurn = true;
        }
    }

    @SpirePatch(
            clz = GameActionManager.class,
            method = "getNextAction"
    )
    public static class RegisterEnemyTurnEnd {

        // Technically not the end of the enemies's turn. Just when we're at the last monster.
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GameActionManager __instance) {
            boolean aliveMonsterInQueue = false;
            for (int i = 0; i < __instance.monsterQueue.size(); i++) {
                if (!(__instance.monsterQueue.get(i).monster.isDeadOrEscaped() || __instance.monsterQueue.get(i).monster.halfDead)) {
                    aliveMonsterInQueue = true;
                }
            }
            if (!aliveMonsterInQueue) {
                isEnemyTurn = false;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "monsterQueue");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 1]};
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "preBattlePrep"
    )
    public static class PrepIsEnemyTurn {

        public static void Prefix(AbstractPlayer __instance) {
            isEnemyTurn = false;
        }
    }

    @SpirePatch(
            clz = ApplyPowerAction.class,
            method = "update"
    )
    public static class ChangePowerToApply {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(ApplyPowerAction __instance) {
            // Containing to The End as it's only necessary there.
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && isEnemyTurn) {
                AbstractPower power = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
                if (power != null) {
                    if (power.owner == AbstractDungeon.player) {
                        // Something like Vulnerable, defer it so it doesn't screw up intents.
                        if (power.atDamageReceive(100, DamageInfo.DamageType.NORMAL) != 100 || power.atDamageFinalReceive(100, DamageInfo.DamageType.NORMAL) != 100) {
                            for (MonsterQueueItem mI : AbstractDungeon.actionManager.monsterQueue) {
                                if (mI.monster.intent == AbstractMonster.Intent.ATTACK
                                        || mI.monster.intent == AbstractMonster.Intent.ATTACK_BUFF
                                        || mI.monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF
                                        || mI.monster.intent == AbstractMonster.Intent.ATTACK_DEFEND
                                ) {
                                    ReflectionHacks.setPrivate(__instance, ApplyPowerAction.class, "powerToApply", new DelayedPowerPower(power.owner, power));
                                    break;
                                }
                            }
                        }
                    } else {
                        // Something like Strength, defer that if the enemy intends to attack.
                        if (power.atDamageGive(100, DamageInfo.DamageType.NORMAL) != 100 || power.atDamageFinalGive(100, DamageInfo.DamageType.NORMAL) != 100) {
                            if (power.owner instanceof AbstractMonster && (
                                    ((AbstractMonster) power.owner).intent == AbstractMonster.Intent.ATTACK
                                            || ((AbstractMonster) power.owner).intent == AbstractMonster.Intent.ATTACK_BUFF
                                            || ((AbstractMonster) power.owner).intent == AbstractMonster.Intent.ATTACK_DEBUFF
                                            || ((AbstractMonster) power.owner).intent == AbstractMonster.Intent.ATTACK_DEFEND
                            )) {
                                for (MonsterQueueItem mI : AbstractDungeon.actionManager.monsterQueue) {
                                    if (mI.monster == power.owner) {
                                        ReflectionHacks.setPrivate(__instance, ApplyPowerAction.class, "powerToApply", new DelayedPowerPower(power.owner, power));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = MonsterGroup.class,
            method = "applyEndOfTurnPowers"
    )
    public static class SpecialHook {

        public static void Prefix(MonsterGroup __instance) {
            isEnemyTurn = false;
            ArrayList<DelayedPowerPower> delayedPowerPowers = new ArrayList<>();
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof DelayedPowerPower) {
                    delayedPowerPowers.add((DelayedPowerPower) p);
                }
            }
            for (AbstractMonster m : __instance.monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof DelayedPowerPower) {
                        delayedPowerPowers.add((DelayedPowerPower) p);
                    }
                }
            }
            for (DelayedPowerPower p : delayedPowerPowers) {
                p.onTrigger();
            }
        }
    }
}
