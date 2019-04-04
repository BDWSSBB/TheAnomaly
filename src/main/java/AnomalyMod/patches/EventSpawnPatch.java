package AnomalyMod.patches;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.events.exordium.Dedmos;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getEvent"
)
public class EventSpawnPatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"tmp"}
    )
    public static void Insert(Random rng, ArrayList<String> tmp) {
        if (AbstractDungeon.player instanceof AnomalyCharacter) {
            tmp.remove(Ghosts.ID);
            tmp.remove(BackToBasics.ID);
        }
        if (!Dedmos.canSpawn()) {
            tmp.remove(Dedmos.ID);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
