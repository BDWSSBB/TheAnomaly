package AnomalyMod.dungeons;

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
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.TheEndingScene;

import java.util.ArrayList;

public class AnomalyTheEnding extends AbstractDungeon {

    public static final String ID = "anomalyMod:AnomalyTheEnding";
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String NAME = TEXT[0];

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
                                    connectNode(sourceNode, targetNode);
                                }
                            }
                        }
                    } else {
                        // Rook connection
                        for (int a = i - 1; a < i + 2; a++) {
                            for (int b = j - 1; b < j + 2; b++) {
                                targetNode = map.get(a).get(b);
                                if (isNodeInMap(targetNode) && !(a == i && b == j) && (a == i || b == j)) {
                                    connectNode(sourceNode, targetNode);
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

        // TODO: Work on the rest of the rooms

        fadeIn();
    }

    private void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
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
        monsters.add(new MonsterInfo(MonsterHelper.THREE_DARKLINGS_ENC, 2f));
        monsters.add(new MonsterInfo(MonsterHelper.ORB_WALKER_ENC, 2f));
        monsters.add(new MonsterInfo(MonsterHelper.THREE_SHAPES_ENC, 2f));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(MonsterHelper.SPIRE_GROWTH_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.TRANSIENT_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.FOUR_SHAPES_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.MAW_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.SPHERE_TWO_SHAPES_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.JAW_WORMS_HORDE, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.THREE_DARKLINGS_ENC, 1f));
        monsters.add(new MonsterInfo(MonsterHelper.WRITHING_MASS_ENC, 1f));
        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(MonsterHelper.GIANT_HEAD_ENC, 2f));
        monsters.add(new MonsterInfo(MonsterHelper.NEMESIS_ENC, 2f));
        monsters.add(new MonsterInfo(MonsterHelper.REPTOMANCER_ENC, 2f));
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
