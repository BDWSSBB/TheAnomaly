package AnomalyMod.trololololo;

import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.trololololo.TheEvilDevModels.AprilFoolsEvilDev;
import AnomalyMod.trololololo.TheEvilDevModels.TheOriginalEvilDev;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getBoss"
)
public class ReplaceBossPatch {

    public static MonsterGroup Postfix(MonsterGroup __result, AbstractDungeon __instance) {
        if (AbstractDungeon.player.hasBlight(AragoltsBane.ID) && AbstractDungeon.id.equals(TheBeyond.ID)) {
            __result = MonsterHelper.getEncounter(TheOriginalEvilDev.ID);
        }
        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date date = new Date();
        String datePrinted = dateFormat.format(date);
        if (datePrinted.equals("04/01") && !ConfigHelper.foughtAprilFoolsMe && AbstractDungeon.id.equals(TheBeyond.ID)) {
            __result = MonsterHelper.getEncounter(AprilFoolsEvilDev.ID);
        }
        return __result;
    }
}
