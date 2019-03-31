package AnomalyMod.patches.improbabilityDriveEffects;

import AnomalyMod.blights.ImprobabilityDrive;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

public class ImprobabilityDriveFutureInvadersPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getMonsterForRoomCreation"
    )
    public static class RollFutureInvaders {

        public static MonsterGroup Postfix(MonsterGroup __result, AbstractDungeon __instance) {
            ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
            if (drive != null && AbstractDungeon.nextRoom != null && AbstractDungeon.nextRoom.room != null && AbstractDungeon.nextRoom.room.getClass().equals(MonsterRoom.class)) {
                __result = drive.rollFutureInvader(__result);
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = AbstractRoom.class,
            method = SpirePatch.CLASS
    )
    public static class FutureInvadersField {

        public static SpireField<Boolean> anomalyModInvadedByFuture = new SpireField<>(() -> false);
    }
}
