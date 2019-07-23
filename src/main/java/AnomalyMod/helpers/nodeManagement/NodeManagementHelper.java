package AnomalyMod.helpers.nodeManagement;

import AnomalyMod.AnomalyMod;
import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.patches.theEnd.TheEndExclusivePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;

import java.util.ArrayList;

public class NodeManagementHelper {

    public static void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    public static void destabilizeNodePath(MapRoomNode node1, MapRoomNode node2, int durability) {
        try {
            TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.set(node1.getEdgeConnectedTo(node2), durability);
            TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.set(node2.getEdgeConnectedTo(node1), durability);
        } catch (NullPointerException e) {
            AnomalyMod.logger.info("WARNING: Tried to destabilize a non-existent connection between two nodes.");
            e.printStackTrace();
        }
    }

    public static void updatePathDurabilities() {
        for (ArrayList<MapRoomNode> aM : AbstractDungeon.map) {
            for (MapRoomNode m : aM) {
                if (m.hasEdges()) {
                    ArrayList<MapEdge> edgesToDelete = new ArrayList<>();
                    for (MapEdge e : m.getEdges()) {
                        int durability = TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.get(e);
                        if (durability != -1) {
                            durability--;
                            if (durability <= 0) {
                                edgesToDelete.add(e);
                            } else {
                                TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.set(e, durability);
                            }
                        }
                    }
                    for (MapEdge e : edgesToDelete) {
                        if (hasPathTo(AbstractDungeon.nextRoom, AnomalyTheEnding.launchTerminalNode, false, new ArrayList<>(), e)) {
                            m.delEdge(e);
                        }
                    }
                }
            }
        }
    }

    // Recursive method. checkedNodes should start off as an empty list.
    public static boolean hasPathTo(MapRoomNode start, MapRoomNode end, boolean mustBeStable, ArrayList<MapRoomNode> checkedNodes, MapEdge edgeException) {
        if (start == end) {
            return true;
        } else {
            checkedNodes.add(start);

            boolean caughtException = false;
            for (MapEdge e : start.getEdges()) {
                // If we want to check for a stable path, the edge is ineligible if it's gonna break.
                if (mustBeStable && TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.get(e) != -1) {
                    continue;
                }

                if (edgeException != null && e == edgeException) {
                    continue;
                }

                // Safety first.
                MapRoomNode nodeToCheck;
                try {
                    nodeToCheck = AbstractDungeon.map.get(e.dstY).get(e.dstX);
                } catch (IndexOutOfBoundsException indexException) {
                    if (!caughtException) {
                        caughtException = true;
                        AnomalyMod.logger.info("WARNING: Node at (" + start.x + ", " + start.y + ") has edge(s) connecting to non-existent node.");
                    }
                    continue;
                }

                if (nodeToCheck != null && !checkedNodes.contains(nodeToCheck) && hasPathTo(nodeToCheck, end, mustBeStable, checkedNodes, edgeException)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPathTo(MapRoomNode start, MapRoomNode end, boolean mustBeStable, ArrayList<MapRoomNode> checkedNodes) {
        return hasPathTo(start, end, mustBeStable, checkedNodes, null);
    }

    public static boolean safeToDestabilizeNodePath(MapRoomNode currentNode, MapRoomNode disconnectNode1, MapRoomNode disconnectNode2) {
        // Assume the path is breakable for now. Revert this later.
        destabilizeNodePath(disconnectNode1, disconnectNode2, 1);

        boolean canDestabilize = true;

        if (!NodeManagementHelper.hasPathTo(currentNode, AnomalyTheEnding.launchTerminalNode, true, new ArrayList<>())) {
            canDestabilize = false;
        }

        for (MapRoomNode n : AnomalyTheEnding.questNodes) {
            // We can ignore it if we've been there already.
            if (!n.taken) {
                if (!NodeManagementHelper.hasPathTo(currentNode, n, true, new ArrayList<>())) {
                    canDestabilize = false;
                }
            }
        }

        // Revert the path stability.
        destabilizeNodePath(disconnectNode1, disconnectNode2, -1);

        if (canDestabilize) {
            return true;
        }
        return false;
    }

    public static boolean isStableEdge(MapEdge edge) {
        return TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.get(edge) == -1;
    }

    public static ArrayList<MapEdge> getStableEdges(MapRoomNode node) {
        ArrayList<MapEdge> stableEdges = new ArrayList<>();
        for (MapEdge e : node.getEdges()) {
            if (isStableEdge(e)) {
                stableEdges.add(e);
            }
        }
        return stableEdges;
    }
}
