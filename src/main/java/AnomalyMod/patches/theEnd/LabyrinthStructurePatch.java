package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class LabyrinthStructurePatch {

    @SpirePatch(
            clz = MapRoomNode.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class ReduceNodeJitter {

        public static void Postfix(MapRoomNode __instance, int x, int y) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                __instance.offsetX /= 2.0F;
                __instance.offsetY /= 2.0F;
            }
        }
    }

    @SpirePatch(
            clz = MapEdge.class,
            method = SpirePatch.CLASS
    )
    public static class RenderingField {

        public static SpireField<Boolean> renderedThisFrame = new SpireField<>(() -> false);
    }

    public static class EraseRedundantEdges {

        @SpirePatch(
                clz = MapEdge.class,
                method = "render"
        )
        public static class SetRenderingField {

            public static void Postfix(MapEdge __instance, SpriteBatch sb) {
                RenderingField.renderedThisFrame.set(__instance, true);
            }
        }

        @SpirePatch(
                clz = MapRoomNode.class,
                method = "render"
        )
        public static class SelectiveRendering {

            @SpireInsertPatch(
                    rloc = 0,
                    localvars = {"edges"}
            )
            public static void Insert(MapRoomNode __instance, SpriteBatch sb, ArrayList<MapEdge> edges) {
                for (MapEdge e : edges) {
                    RenderingField.renderedThisFrame.set(e, false);
                }
            }

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getClassName().equals(MapEdge.class.getName()) && m.getMethodName().equals("render")) {
                            m.replace(
                                    "{" +
                                            "if (!" + Nested.class.getName() + ".isRedundantEdge($0)) {$_ = $proceed($$);}" +
                                            "}");
                        }
                    }
                };
            }

            public static class Nested {

                public static boolean isRedundantEdge(MapEdge edge) {
                    if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                        for (ArrayList<MapRoomNode> m : AbstractDungeon.map) {
                            for (MapRoomNode n : m) {
                                if (n.x == edge.dstX && n.y == edge.dstY) {
                                    for (MapEdge e : n.getEdges()) {
                                        if (e.dstX == edge.srcX && e.dstY == edge.srcY && RenderingField.renderedThisFrame.get(e)) {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return false;
                }
            }
        }
    }

    public static class ColorEdgesProperly {

        @SpirePatch(
                clz = MapRoomNode.class,
                method = "update"
        )
        public static class ColorAllConnections {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(MapRoomNode __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    if (__instance.equals(AbstractDungeon.getCurrMapNode())) {
                        for (ArrayList<MapRoomNode> m : AbstractDungeon.map) {
                            for (MapRoomNode n : m) {
                                if (n.isConnectedTo(__instance)) {
                                    for (MapEdge e : n.getEdges()) {
                                        if (__instance.x == e.dstX && __instance.y == e.dstY) {
                                            e.color = MapRoomNode.AVAILABLE_COLOR.cpy();
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
                    Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrMapNode");
                    int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                    return new int[] {found[2]};
                }
            }
        }
    }

    // TODO: Make this start at where the terminal will be
    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class StartAtDifferentRoom {

        // Just trick the system again.
        private static Integer tempY;

        @SpireInsertPatch(
                locator = LocatorOne.class
        )
        public static void InsertOne(MapRoomNode __instance) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && !AbstractDungeon.firstRoomChosen) {
                tempY = __instance.y;
                __instance.y = 0;
            }
        }

        private static class LocatorOne extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[] {found[found.length - 1]};
            }
        }

        @SpireInsertPatch(
                locator = LocatorTwo.class
        )
        public static void InsertTwo(MapRoomNode __instance) {
            if (tempY != null) {
                __instance.y = tempY;
                tempY = null;
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "hovered");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[] {found[found.length - 1]};
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class EraseTravelHistory {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"connectedEdge"}
        )
        public static void InsertOne(MapRoomNode __instance, MapEdge connectedEdge) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                AbstractDungeon.getCurrMapNode().taken = false;
                if (connectedEdge != null) {
                    connectedEdge.taken = false;
                    connectedEdge.color = new Color(0.34f, 0.34f, 0.34f, 1f);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "nextRoom");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "populatePathTaken"
    )
    public static class EraseTravelHistorySaveAndLoad {

        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                for (ArrayList<MapRoomNode> m : AbstractDungeon.map) {
                    for (MapRoomNode n : m) {
                        n.taken = false;
                        for (MapEdge e : n.getEdges()) {
                            e.taken = false;
                            e.color = new Color(0.34f, 0.34f, 0.34f, 1f);
                        }
                    }
                }
            }
        }
    }
}
