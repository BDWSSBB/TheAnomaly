package AnomalyMod.patches.balance;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.events.exordium.Dedmos;
import AnomalyMod.events.theEnd.CoolantFanQuest;
import AnomalyMod.events.theEnd.DataDecoderQuest;
import AnomalyMod.events.theEnd.NeuralCablesQuest;
import AnomalyMod.events.theEnd.PhaseControllerChipQuest;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MysteriousSphere;
import com.megacrit.cardcrawl.events.city.BackToBasics;
import com.megacrit.cardcrawl.events.city.Ghosts;
import com.megacrit.cardcrawl.events.city.KnowingSkull;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;

import java.util.ArrayList;

public class EventAndShrineSpawnPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getEvent"
    )
    public static class EventSpawn {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void Insert(Random rng, ArrayList<String> tmp) {
            // Changes to vanilla spawning:
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                // Act 2 changes:
                tmp.remove(Ghosts.ID);
                tmp.remove(BackToBasics.ID);

                // Act 3 changes:
                if (AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.y < AbstractDungeon.map.size() / 2) {
                    // Same issue with Colosseum.
                    tmp.remove(MysteriousSphere.ID);
                }
            }

            // This mod's events:
            if (!Dedmos.canSpawn()) {
                tmp.remove(Dedmos.ID);
            }

            // Remove the quest events from normal generation, use a manual one for The End.
            tmp.remove(PhaseControllerChipQuest.ID);
            tmp.remove(NeuralCablesQuest.ID);
            tmp.remove(DataDecoderQuest.ID);
            tmp.remove(CoolantFanQuest.ID);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "getShrine"
    )
    public static class ShrineSpawn {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"tmp"}
        )
        public static void Insert(Random rng, ArrayList<String> tmp) {
            // Changes to vanilla spawning:
            if (AbstractDungeon.player instanceof AnomalyCharacter) {
                // BAD GAME DESIGN (at least the wheel one)
                tmp.remove(GremlinMatchGame.ID);
                tmp.remove(GremlinWheelGame.ID);

                // Especially hurts for him, don't bother.
                tmp.remove(KnowingSkull.ID);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "get");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
