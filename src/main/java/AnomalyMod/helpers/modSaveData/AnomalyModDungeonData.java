package AnomalyMod.helpers.modSaveData;

import AnomalyMod.AnomalyMod;
import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.patches.theEnd.TheEndExclusivePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;

public class AnomalyModDungeonData {

    // RNG
    public static Random anomalyRNG;

    // The End components and data
    public static boolean goToAnomalyAct4;
    public static QuestComponentQuality phaseControllerChip;
    public static QuestComponentQuality neuralCables;
    public static QuestComponentQuality dataDecoder;
    public static QuestComponentQuality coolantFan;
    public static int improbabilityOptionsUsed;
    public static ArrayList<EdgeData> loadedEdgeData;

    public static boolean triggerAnomalyDeathVictory;

    public enum QuestComponentQuality {
        NONE, NORMAL, PERFECT
    }

    public static AnomalyModSaveFile createSaveData() {
        AnomalyModSaveFile saveData = new AnomalyModSaveFile();
        saveData.goToAnomalyAct4 = goToAnomalyAct4;
        saveData.anomalyRNGCounter = anomalyRNG.counter;
        saveData.phaseControllerChip = phaseControllerChip;
        saveData.neuralCables = neuralCables;
        saveData.dataDecoder = dataDecoder;
        saveData.coolantFan = coolantFan;
        saveData.improbabilityOptionsUsed = improbabilityOptionsUsed;
        saveData.edgeData = new ArrayList<>();
        if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
            for (ArrayList<MapRoomNode> aM : AbstractDungeon.map) {
                for (MapRoomNode n : aM) {
                    for (MapEdge e : n.getEdges()) {
                        saveData.edgeData.add(new EdgeData(n.x, n.y, e.dstX, e.dstY, TheEndExclusivePatch.PathDurability.PathDurabilityField.pathDurability.get(e)));
                    }
                }
            }
        }
        return saveData;
    }

    public static void loadSaveData(AnomalyModSaveFile loadedData) {
        generateSeeds();
        if (loadedData != null) {
            try {
                anomalyRNG.counter = loadedData.anomalyRNGCounter;
                goToAnomalyAct4 = loadedData.goToAnomalyAct4;
                phaseControllerChip = loadedData.phaseControllerChip;
                neuralCables = loadedData.neuralCables;
                dataDecoder = loadedData.dataDecoder;
                coolantFan = loadedData.coolantFan;
                improbabilityOptionsUsed = loadedData.improbabilityOptionsUsed;
                loadedEdgeData = loadedData.edgeData;
            } catch (Exception e) {
                AnomalyMod.logger.info("WARNING: Save data for Anomaly Mod is corrupted! Resetting data to default values...");
                e.printStackTrace();
                goToAnomalyAct4 = false;
                resetTheEndComponents();
            }
        } else {
            AnomalyMod.logger.info("WARNING: Save data for Anomaly Mod is null!");
        }
    }

    public static void generateSeeds() {
        AnomalyMod.logger.info("generateSeeds was called");
        triggerAnomalyDeathVictory = false;
        if (Settings.seed != null) {
            anomalyRNG = new Random(Settings.seed);
        } else {
            AnomalyMod.logger.info("generateSeeds called with null Settings.seed");
            anomalyRNG = new Random();
        }
    }

    public static void resetTheEndComponents() {
        AnomalyTheEnding.launchTerminalNode = null;
        AnomalyTheEnding.questNodes = null;
        phaseControllerChip = QuestComponentQuality.NONE;
        neuralCables = QuestComponentQuality.NONE;
        dataDecoder = QuestComponentQuality.NONE;
        coolantFan = QuestComponentQuality.NONE;
        improbabilityOptionsUsed = 0;
        loadedEdgeData = null;
    }
}
