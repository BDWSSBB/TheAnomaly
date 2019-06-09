package AnomalyMod.trololololo;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;

@SpirePatch(
        clz = CharacterOption.class,
        method = "update"
)
public class ILoveYouAragoltPatch {

    private static float waitForIt = 0.0F;

    public static void Prefix(CharacterOption __instance) {
//        __instance.hb.update();
//        if (CardCrawlGame.playerName.equals("Aragolt") && __instance.hb.clicked && MathUtils.randomBoolean(waitForIt)) {
//            waitForIt = 0.0F;
//            CardCrawlGame.sound.playA("JEVIL-ANYTHING", 0.0F);
//        }
//        else {
//            waitForIt += 0.03F;
//        }
    }
}
