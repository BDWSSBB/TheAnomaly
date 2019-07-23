package AnomalyMod.dungeons;

import AnomalyMod.AnomalyMod;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import AnomalyMod.helpers.modSaveData.EdgeData;
import AnomalyMod.helpers.monsterEncounters.AnomalyMonsterHelper;
import AnomalyMod.helpers.nodeManagement.NodeManagementHelper;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.map.RoomTypeAssigner;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheEndingScene;

import java.util.ArrayList;

public class AnomalyTheEnding extends AbstractDungeon {

    public static final String ID = "anomalyMod:AnomalyTheEnding";
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String NAME = TEXT[0];

    public static MapRoomNode launchTerminalNode;
    public static ArrayList<MapRoomNode> questNodes;

    public AnomalyTheEnding(AbstractPlayer p, ArrayList<String> theList) {
        super(NAME, ID, p, theList);
        if (scene != null) {
            scene.dispose();
        }
        scene = new TheEndingScene();
        fadeColor = Color.valueOf("140a1eff");
        initializeLevelSpecificChances();
        mapRng = new Random(Settings.seed + AbstractDungeon.actNum * 300);
        generateSpecialMap();
        firstRoomChosen = false;
        CardCrawlGame.music.changeBGM(TheEnding.ID);
    }

    public AnomalyTheEnding(AbstractPlayer p, SaveFile saveFile) {
        super(NAME, p, saveFile);
        if (scene != null) {
            scene.dispose();
        }
        scene = new TheEndingScene();
        fadeColor = Color.valueOf("140a1eff");
        initializeLevelSpecificChances();
        miscRng = new Random(Settings.seed + saveFile.floor_num);
        CardCrawlGame.music.changeBGM(TheEnding.ID);
        mapRng = new Random(Settings.seed + saveFile.act_num * 300);
        generateSpecialMap();
        firstRoomChosen = true;
        populatePathTaken(saveFile);
    }

    private void generateSpecialMap() {
        map = new ArrayList<>();

        // Make a 5x5 grid (without corners). In reality, we have a 7x7 and some aren't connected.
        for (int i = 0; i < 7; i++) {
            ArrayList<MapRoomNode> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(new MapRoomNode(j, i));
            }
            map.add(row);
        }

        // Connect the nodes.
        MapRoomNode sourceNode;
        MapRoomNode targetNode;
        for (int i = 1; i < 6; i++) {
            for (int j = 1; j < 6; j++) {
                sourceNode = map.get(i).get(j);
                // Restricted to the 5x5 without corners.
                if (isNodeInMap(sourceNode)) {
                    if ((sourceNode.x + sourceNode.y) % 2 == 0) {
                        // King connection
                        for (int a = i - 1; a < i + 2; a++) {
                            for (int b = j - 1; b < j + 2; b++) {
                                targetNode = map.get(a).get(b);
                                if (isNodeInMap(targetNode) && !(a == i && b == j)) {
                                    NodeManagementHelper.connectNode(sourceNode, targetNode);
                                }
                            }
                        }
                    } else {
                        // Rook connection
                        for (int a = i - 1; a < i + 2; a++) {
                            for (int b = j - 1; b < j + 2; b++) {
                                targetNode = map.get(a).get(b);
                                if (isNodeInMap(targetNode) && !(a == i && b == j) && (a == i || b == j)) {
                                    NodeManagementHelper.connectNode(sourceNode, targetNode);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fill it in with monsters by default.
        for (ArrayList<MapRoomNode> r : map) {
            RoomTypeAssigner.assignRowAsRoomType(r, MonsterRoom.class);
        }

        // Add Launch Terminal.
        try {
            // We'll use a rest room for the portable terminal and configure it to our needs.
            MapRoomNode node = map.get(3).get(3);
            node.setRoom(new EventRoom());
            node.room.setMapSymbol("R");
            node.room.setMapImg(ImageMaster.MAP_NODE_REST, ImageMaster.MAP_NODE_REST_OUTLINE);
            launchTerminalNode = node;
        } catch (Exception e) {
            e.printStackTrace();
            AnomalyMod.logger.info("WARNING: Failed to assign Launch Terminal in the map.");
        }

        // Add component locations.
        questNodes = new ArrayList<>();
        try {
            MapRoomNode node = map.get(1).get(mapRng.random(2, 4));
            node.setRoom(new EventRoom());
            node.hasEmeraldKey = true;
            questNodes.add(node);
        } catch (Exception e) {
            e.printStackTrace();
            AnomalyMod.logger.info("WARNING: Failed to assign an objective room in the map.");
        }
        try {
            MapRoomNode node = map.get(5).get(mapRng.random(2, 4));
            node.setRoom(new EventRoom());
            node.hasEmeraldKey = true;
            questNodes.add(node);
        } catch (Exception e) {
            e.printStackTrace();
            AnomalyMod.logger.info("WARNING: Failed to assign an objective room in the map.");
        }
        try {
            MapRoomNode node = map.get(mapRng.random(2, 4)).get(1);
            node.setRoom(new EventRoom());
            node.hasEmeraldKey = true;
            questNodes.add(node);
        } catch (Exception e) {
            e.printStackTrace();
            AnomalyMod.logger.info("WARNING: Failed to assign an objective room in the map.");
        }
        try {
            MapRoomNode node = map.get(mapRng.random(2, 4)).get(5);
            node.setRoom(new EventRoom());
            node.hasEmeraldKey = true;
            questNodes.add(node);
        } catch (Exception e) {
            e.printStackTrace();
            AnomalyMod.logger.info("WARNING: Failed to assign an objective room in the map.");
        }

        // Use Save File edges if loading from save.
        if (AnomalyModDungeonData.loadedEdgeData != null && !AnomalyModDungeonData.loadedEdgeData.isEmpty()) {

            for (ArrayList<MapRoomNode> aM : map) {
                for (MapRoomNode n : aM) {
                    ArrayList<MapEdge> edgesToDelete = new ArrayList<>();
                    for (MapEdge e : n.getEdges()) {
                        edgesToDelete.add(e);
                    }
                    for (MapEdge e : edgesToDelete) {
                        n.delEdge(e);
                    }
                }
            }

            for (EdgeData edgeData : AnomalyModDungeonData.loadedEdgeData) {
                try {
                    NodeManagementHelper.connectNode(map.get(edgeData.srcY).get(edgeData.srcX), map.get(edgeData.dstY).get(edgeData.dstX));
                    NodeManagementHelper.connectNode(map.get(edgeData.dstY).get(edgeData.dstX), map.get(edgeData.srcY).get(edgeData.srcX));
                    if (edgeData.durability != -1) {
                        NodeManagementHelper.destabilizeNodePath(map.get(edgeData.srcY).get(edgeData.srcX), map.get(edgeData.dstY).get(edgeData.dstX), edgeData.durability);
                    }
                } catch (IndexOutOfBoundsException exception) {
                    AnomalyMod.logger.info("WARNING: Save data could not connect nodes properly.");
                }
            }
        }

        fadeIn();
    }

    private boolean isNodeInMap(MapRoomNode node) {
        if (node.x >= 1 && node.x <= 5 && node.y >= 1 && node.y <= 5) {
            if ((node.x == 1 || node.x == 5) && (node.y == 1 || node.y == 5)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.0F;
        restRoomChance = 0.0F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.0F;
        eliteRoomChance = 0.0F;

        smallChestChance = 0;
        mediumChestChance = 100;
        largeChestChance = 0;

        commonRelicChance = 33;
        uncommonRelicChance = 50;
        rareRelicChance = 17;

        colorlessRareChance = 0.3f;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25f;
        } else {
            cardUpgradedChance = 0.5f;
        }
    }

    @Override
    protected void generateMonsters() {
        generateWeakEnemies(2);
        generateStrongEnemies(12);
        generateElites(10);
    }

    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.SLIME_HORDE_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.THUG_HORDE_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.SNECKO_CULT_ENC, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.CONSTRUCT_HORDE_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.WILDLIFE_HORDE_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.MAW_AND_FRIENDS_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.THE_REMINDER_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.BLUE_CREW_ENC, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.ACT_1_ELITE_MIX_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.ACT_2_ELITE_MIX_ENC, 1.0F));
        monsters.add(new MonsterInfo(AnomalyMonsterHelper.ACT_3_ELITE_MIX_ENC, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, true);
    }

    @Override
    protected ArrayList<String> generateExclusions() {
        return new ArrayList<String>();
    }

    @Override
    protected void initializeBoss() {
        // There's not really a boss but this is just in case.
        bossList.add(MonsterHelper.THE_HEART_ENC);
        bossList.add(MonsterHelper.THE_HEART_ENC);
        bossList.add(MonsterHelper.THE_HEART_ENC);
    }

    @Override
    protected void initializeEventList() {

    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }
        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList() {

    }
}
