package AnomalyMod.patches.theEnd;

import AnomalyMod.dungeons.AnomalyTheEnding;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
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

    public static class DungeonMapPatches {

        @SpirePatch(
                clz = DungeonMap.class,
                method = "update"
        )
        public static class WhyAmIGoingToTheBoss {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(DungeonMap __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && __instance.bossHb.hovered) {
                    __instance.bossHb.hovered = false;
                }
            }

            private static class Locator extends SpireInsertLocator {
                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "hovered");
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                }
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "calculateMapSize"
        )
        public static class CalculateMapSize {

            public static float Postfix(float __result, DungeonMap __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    __result = Settings.MAP_DST_Y * (AbstractDungeon.FINAL_ACT_MAP_HEIGHT + 1) - 1380f * Settings.scale;
                }
                return __result;
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "render"
        )
        public static class Render {

            // It's just easier to trick the system.
            private static String tempID;

            public static void Prefix(DungeonMap __instance, SpriteBatch sb) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    tempID = AbstractDungeon.id;
                    AbstractDungeon.id = TheEnding.ID;
                }
            }

            public static void Postfix(DungeonMap __instance, SpriteBatch sb) {
                if (tempID != null) {
                    AbstractDungeon.id = tempID;
                    tempID = null;
                }
            }
        }

        @SpirePatch(
                clz = DungeonMap.class,
                method = "renderBossIcon"
        )
        public static class RenderBossIcon {

            public static SpireReturn<Void> Prefix(DungeonMap __instance, SpriteBatch sb) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(
                clz = DungeonMapScreen.class,
                method = "open"
        )
        public static class ShortenMapScroll {

            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    int findIt = 0;

                    @Override
                    public void edit(FieldAccess f) throws CannotCompileException {
                        if (f.getFieldName().equals("mapScrollUpperLimit")) {
                            if (findIt == 0) {
                                f.replace(
                                        "{" +
                                                "$_ = $proceed($$);" +
                                                "if (" + Nested.class.getName() + ".isAnomalyAct()) {" +
                                                "mapScrollUpperLimit = MAP_UPPER_SCROLL_FINAL_ACT;" +
                                                "}" +
                                                "}");
                            }
                            findIt++;
                        }
                    }
                };
            }

            public static class Nested {

                public static boolean isAnomalyAct() {
                    if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
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
                    int findit = 0;

                    @Override
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getClassName().equals(MapEdge.class.getName()) && m.getMethodName().equals("render")) {
                            // Don't render redundant edges.
                            m.replace(
                                    "{" +
                                            "if (!" + Nested.class.getName() + ".isRedundantEdge($0)) {$_ = $proceed($$);}" +
                                            "}");
                        } else if (m.getMethodName().equals("draw")) {
                            // Don't render circle (unless it's the current node).
                            if (findit == 2) {
                                m.replace(
                                        "{" +
                                                "if (" + Nested.class.getName() + ".isCurrentNode(this)) {$_ = $proceed($$);}" +
                                                "}");
                            }
                            findit++;
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

                public static boolean isCurrentNode(MapRoomNode node) {
                    if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                        if (AbstractDungeon.currMapNode == node) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        // Not the ending, proceed as normal.
                        return true;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "render"
    )
    public static class ColorTakenNodesBetter {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(MapRoomNode __instance, SpriteBatch sb) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && (__instance.taken)) {
                Color newColor = Color.GREEN.cpy();
                newColor.a = __instance.color.a;
                sb.setColor(newColor);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "draw");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[found.length - 2]};
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
                    return new int[]{found[2]};
                }
            }
        }
    }

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
                return new int[]{found[found.length - 1]};
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
                return new int[]{found[found.length - 1]};
            }
        }

        public static void Postfix(MapRoomNode __instance) {
            if (tempY != null) {
                __instance.y = tempY;
                tempY = null;
            }
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                int findIt = 0;

                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getFieldName().equals("firstRoomChosen")) {
                        if (findIt == 1) {
                            f.replace(
                                    "{" +
                                            "$_ = !" + Nested.class.getName() + ".isCorrectRoom(this);" +
                                            "}");
                        }
                        findIt++;
                    }
                }
            };
        }

        public static class Nested {

            public static boolean isCorrectRoom(MapRoomNode node) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && !AbstractDungeon.firstRoomChosen) {
                    if (node == AnomalyTheEnding.launchTerminalNode) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return !AbstractDungeon.firstRoomChosen;
                }
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "update"
    )
    public static class ErasePathHistory {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"connectedEdge"}
        )
        public static void Insert(MapRoomNode __instance, MapEdge connectedEdge) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
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
    public static class ErasePathHistorySaveAndLoad {

        public static void Postfix(AbstractDungeon __instance, SaveFile saveFile) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                for (ArrayList<MapRoomNode> m : AbstractDungeon.map) {
                    for (MapRoomNode n : m) {
                        for (MapEdge e : n.getEdges()) {
                            e.taken = false;
                            e.color = new Color(0.34f, 0.34f, 0.34f, 1f);
                        }
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "oscillateColor"
    )
    public static class MakeTakenNodesStillOscillate {

        private static Boolean temp;

        public static void Prefix(MapRoomNode __instance) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                temp = __instance.taken;
                __instance.taken = false;
            }
        }

        public static void Postfix(MapRoomNode __instance) {
            if (temp != null) {
                __instance.taken = temp;
                temp = null;
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "populatePathTaken"
    )
    public static class FixSaveAndLoadPathHistory {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && !AbstractDungeon.pathX.isEmpty()) {
                boolean wasThere = false;
                MapRoomNode lastNode = AbstractDungeon.map.get(AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1)).get(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
                for (int i = 0; i < AbstractDungeon.pathX.size() - 2; i++) {
                    if (lastNode == AbstractDungeon.map.get(AbstractDungeon.pathY.get(i)).get(AbstractDungeon.pathX.get(i))) {
                        wasThere = true;
                    }
                }
                if (!wasThere) {
                    lastNode.taken = false;
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "nextRoomTransition");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "nextRoomTransition",
            paramtypez = {
                    SaveFile.class
            }
    )
    public static class EmptyVisitedNodes {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
            // The Launch Terminal exclusion is more The End exclusive, but it's just more convenient to put it here.
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && AbstractDungeon.currMapNode.taken && AbstractDungeon.currMapNode != AnomalyTheEnding.launchTerminalNode) {
                AbstractRoom emptyRoom = new EmptyRoom();
                emptyRoom.setMapSymbol(AbstractDungeon.getCurrRoom().getMapSymbol());
                emptyRoom.setMapImg(AbstractDungeon.getCurrRoom().getMapImg(), AbstractDungeon.getCurrRoom().getMapImgOutline());
                AbstractDungeon.currMapNode.room = emptyRoom;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "onPlayerEntry");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = MapRoomNode.class,
            method = "wingedIsConnectedTo"
    )
    public static class DisableWingBootsAndFlight {

        public static SpireReturn<Boolean> Prefix(MapRoomNode __instance, MapRoomNode node) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                return SpireReturn.Return(__instance.isConnectedTo(node));
            }
            return SpireReturn.Continue();
        }
    }
}
