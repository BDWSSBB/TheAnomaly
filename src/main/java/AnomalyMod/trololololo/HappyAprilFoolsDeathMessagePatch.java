package AnomalyMod.trololololo;

import AnomalyMod.trololololo.TheEvilDevModels.AprilFoolsEvilDev;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.DeathScreen;

@SpirePatch(
        clz = DeathScreen.class,
        method = "getDeathText"
)
public class HappyAprilFoolsDeathMessagePatch {

    public static SpireReturn<String> Prefix(DeathScreen __instance) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m instanceof AprilFoolsEvilDev) {
                return SpireReturn.Return("Happy April Fools!");
            }
        }
        return SpireReturn.Continue();
    }
}
