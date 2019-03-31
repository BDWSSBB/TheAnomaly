package AnomalyMod.trololololo;

import AnomalyMod.trololololo.TheEvilDevModels.TheOriginalEvilDev;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getBoss"
)
public class ReplaceBossPatch {

    public static MonsterGroup Postfix(MonsterGroup __result, AbstractDungeon __instance) {
        if (AbstractDungeon.player.hasBlight(AragoltsBane.ID) && AbstractDungeon.id.equals(TheBeyond.ID)) {
            __result = MonsterHelper.getEncounter(TheOriginalEvilDev.ID);
        }
        return __result;
    }
}
