package AnomalyMod.patches.balance;

import AnomalyMod.AnomalyMod;
import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.events.exordium.Dedmos;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.beyond.MysteriousSphere;
import com.megacrit.cardcrawl.events.city.*;
import com.megacrit.cardcrawl.events.exordium.Cleric;
import com.megacrit.cardcrawl.events.shrines.GremlinMatchGame;
import com.megacrit.cardcrawl.events.shrines.GremlinWheelGame;
import com.megacrit.cardcrawl.map.MapRoomNode;
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
                // Act 1 changes:
                if (AbstractDungeon.player.gold < (int) ReflectionHacks.getPrivateStatic(Cleric.class, "HEAL_COST")) {
                    tmp.remove(Cleric.ID);
                }

                // Act 2 changes:
                tmp.remove(Ghosts.ID);
                tmp.remove(BackToBasics.ID);
                if (AbstractDungeon.player.gold < (int) ReflectionHacks.getPrivateStatic(Beggar.class, "GOLD_COST")) {
                    tmp.remove(Beggar.ID);
                }

                if (AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.y < getHalfOfHighestRelativeFloor()) {
                    // This thing should really only be seen top half of act 2. Double slavers for no reward is cancer.
                    tmp.remove(Colosseum.ID);
                }

                // Act 3 changes:
                if (AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.y < getHalfOfHighestRelativeFloor()) {
                    // Same issue with Colosseum.
                    tmp.remove(MysteriousSphere.ID);
                }
            }

            // This mod's events:
            if (!Dedmos.canSpawn()) {
                tmp.remove(Dedmos.ID);
            }
        }

        private static int getHalfOfHighestRelativeFloor() {
            int highestRelativeFloor = -1;
            for (ArrayList<MapRoomNode> aM : AbstractDungeon.map) {
                for (MapRoomNode m : aM) {
                    if (highestRelativeFloor < m.y) {
                        highestRelativeFloor = m.y;
                    }
                }
            }
            if (highestRelativeFloor == -1) {
                AnomalyMod.logger.info("Hey, what happened to the map?");
                return 0;
            }
            return highestRelativeFloor / 2;
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
