package AnomalyMod.patches.theEnd;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.common.SlideMonsterInAction;
import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.events.theEnd.*;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import AnomalyMod.helpers.monsterEncounters.AnomalyMonsterHelper;
import AnomalyMod.helpers.nodeManagement.NodeManagementHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import com.megacrit.cardcrawl.monsters.exordium.SlaverBlue;
import com.megacrit.cardcrawl.monsters.exordium.SlaverRed;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.ui.buttons.DynamicBanner;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class TheEndExclusivePatch {

    @SpirePatch(
            clz = EventHelper.class,
            method = "roll",
            paramtypez = {
                    Random.class
            }
    )
    public static class GuaranteeEventsInQuestionRooms {

        public static SpireReturn<EventHelper.RoomResult> Prefix(Random eventRng) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                return SpireReturn.Return(EventHelper.RoomResult.EVENT);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = EventRoom.class,
            method = "onPlayerEntry"
    )
    public static class GuaranteeQuestEvents {

        public static SpireReturn<Void> Prefix(EventRoom __instance) {
            if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                if (AbstractDungeon.currMapNode == AnomalyTheEnding.launchTerminalNode) {
                    AbstractDungeon.getCurrRoom().setMapSymbol("R");
                    AbstractDungeon.getCurrRoom().setMapImg(ImageMaster.MAP_NODE_REST, ImageMaster.MAP_NODE_REST_OUTLINE);
                    __instance.event = EventHelper.getEvent(LaunchTerminal.ID);
                    __instance.event.onEnterRoom();
                    return SpireReturn.Return(null);
                } else {
                    ArrayList<String> eventsToDo = new ArrayList<>();
                    if (AnomalyModDungeonData.phaseControllerChip == AnomalyModDungeonData.QuestComponentQuality.NONE) {
                        eventsToDo.add(PhaseControllerChipQuest.ID);
                    }
                    if (AnomalyModDungeonData.neuralCables == AnomalyModDungeonData.QuestComponentQuality.NONE) {
                        eventsToDo.add(NeuralCablesQuest.ID);
                    }
                    if (AnomalyModDungeonData.dataDecoder == AnomalyModDungeonData.QuestComponentQuality.NONE) {
                        eventsToDo.add(DataDecoderQuest.ID);
                    }
                    if (AnomalyModDungeonData.coolantFan == AnomalyModDungeonData.QuestComponentQuality.NONE) {
                        eventsToDo.add(CoolantFanQuest.ID);
                    }
                    if (!eventsToDo.isEmpty()) {
                        Random eventRngDuplicate = new Random(Settings.seed, AbstractDungeon.eventRng.counter);
                        __instance.event = EventHelper.getEvent(eventsToDo.get(eventRngDuplicate.random(0, eventsToDo.size() - 1)));
                        __instance.event.onEnterRoom();
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }

    public static class PathDurability {

        @SpirePatch(
                clz = MapEdge.class,
                method = SpirePatch.CLASS
        )
        public static class PathDurabilityField {

            public static SpireField<Integer> pathDurability = new SpireField<>(() -> -1);
        }

        @SpirePatch(
                clz = MapEdge.class,
                method = "render"
        )
        public static class Render {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(MapEdge __instance, SpriteBatch sb) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    int pathDurability = PathDurabilityField.pathDurability.get(__instance);
                    if (pathDurability >= 3) {
                        sb.setColor(Color.YELLOW.cpy());
                    } else if (pathDurability == 2) {
                        sb.setColor(Color.ORANGE.cpy());
                    } else if (pathDurability == 1) {
                        sb.setColor(Color.RED.cpy());
                    }
                }
            }

            private static class Locator extends SpireInsertLocator {
                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(MapEdge.class, "dots");
                    return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
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
        public static class UpdatePathing {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) && !CardCrawlGame.loadingSave && AbstractDungeon.nextRoom != null) {
                    // Paths crumble naturally.
                    NodeManagementHelper.updatePathDurabilities();

                    // Destabilize the path the player took if they can still get to the key locations without it.
                    if (AbstractDungeon.currMapNode != null) {
                        if (NodeManagementHelper.safeToDestabilizeNodePath(AbstractDungeon.nextRoom, AbstractDungeon.currMapNode, AbstractDungeon.nextRoom)) {
                            NodeManagementHelper.destabilizeNodePath(AbstractDungeon.currMapNode, AbstractDungeon.nextRoom, 1);
                        }
                    }

                    // Select 2 paths to destabilize at random. They must be safe to destabilize.
                    ArrayList<MapRoomNode> nodesWithEdges = new ArrayList<>();
                    for (ArrayList<MapRoomNode> aM : AbstractDungeon.map) {
                        for (MapRoomNode m : aM) {
                            if (m.hasEdges()) {
                                nodesWithEdges.add(m);
                            }
                        }
                    }

                    int edgesDestabilized = 0;
                    for (int failsafe = 0; failsafe < 20 && edgesDestabilized < 2; failsafe++) {
                        MapRoomNode node1 = nodesWithEdges.get(AbstractDungeon.miscRng.random(0, nodesWithEdges.size() - 1));
                        ArrayList<MapEdge> eligibleEdges = NodeManagementHelper.getStableEdges(node1);
                        if (eligibleEdges.isEmpty()) {
                            continue;
                        }
                        MapEdge edge = eligibleEdges.get(AbstractDungeon.miscRng.random(0, eligibleEdges.size() - 1));
                        MapRoomNode node2;
                        try {
                            node2 = AbstractDungeon.map.get(edge.dstY).get(edge.dstX);
                        } catch (IndexOutOfBoundsException indexException) {
                            AnomalyMod.logger.info("WARNING: Node at (" + node1.x + ", " + node1.y + ") has edge(s) connecting to non-existent node.");
                            continue;
                        }
                        if (NodeManagementHelper.safeToDestabilizeNodePath(AbstractDungeon.nextRoom, node1, node2)) {
                            NodeManagementHelper.destabilizeNodePath(node1, node2, 3);
                            edgesDestabilized++;
                        }
                    }
                }
            }

            private static class Locator extends SpireInsertLocator {

                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "loadingSave");
                    return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
                }
            }
        }
    }

    public static class SpawnBackupMonsters {

        @SpirePatch(
                clz = AbstractRoom.class,
                method = SpirePatch.CLASS
        )
        public static class BackupMonstersField {

            public static SpireField<MonsterGroup> backupMonsters = new SpireField<>(() -> null);
        }

        @SpirePatch(
                clz = AbstractMonster.class,
                method = SpirePatch.CLASS
        )
        public static class SpawnBackupField {

            public static SpireField<Boolean> spawnBackupOnDeath = new SpireField<>(() -> false);
        }

        @SpirePatch(
                clz = AbstractDungeon.class,
                method = "getMonsterForRoomCreation"
        )
        public static class PrepareBackupMonsters {

            public static void Postfix(AbstractDungeon __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    MonsterGroup backupMonsters = AnomalyMonsterHelper.getBackupMonsters(AbstractDungeon.monsterList.get(0));
                    if (backupMonsters != null) {
                        BackupMonstersField.backupMonsters.set(AbstractDungeon.getCurrRoom(), backupMonsters);
                        AbstractDungeon.getCurrRoom().cannotLose = true;
                    }
                }
            }
        }

        @SpirePatch(
                clz = AbstractDungeon.class,
                method = "getEliteMonsterForRoomCreation"
        )
        public static class PrepareEliteBackupMonsters {

            public static void Postfix(AbstractDungeon __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    MonsterGroup backupMonsters = AnomalyMonsterHelper.getBackupMonsters(AbstractDungeon.eliteMonsterList.get(0));
                    if (backupMonsters != null) {
                        BackupMonstersField.backupMonsters.set(AbstractDungeon.getCurrRoom(), backupMonsters);
                        AbstractDungeon.getCurrRoom().cannotLose = true;
                    }
                }
            }
        }

        @SpirePatch(
                clz = MonsterGroup.class,
                method = "init"
        )
        public static class MarkMonstersThatSpawnBackup {

            public static void Prefix(MonsterGroup __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    for (AbstractMonster m : __instance.monsters) {
                        SpawnBackupField.spawnBackupOnDeath.set(m, true);
                    }
                }
            }
        }

        @SpirePatch(
                clz = MonsterGroup.class,
                method = "areMonstersDead"
        )
        @SpirePatch(
                clz = MonsterGroup.class,
                method = "areMonstersBasicallyDead"
        )
        public static class PreventPrematureBattleEnd {

            public static boolean Postfix(boolean __result, MonsterGroup __instance) {
                // Technically I don't need the dungeon check, but this is just to contain stuff in case it blows up in my face.
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    MonsterGroup leftoverMonsters = BackupMonstersField.backupMonsters.get(AbstractDungeon.getCurrRoom());
                    if (leftoverMonsters != null && !leftoverMonsters.monsters.isEmpty()) {
                        __result = false;
                    }
                }
                return __result;
            }
        }

        @SpirePatch(
                clz = GameActionManager.class,
                method = "getNextAction"
        )
        public static class SpawnBackupFromDeadEnemies {

            @SpireInsertPatch(
                    locator = Locator.class
            )
            public static void Insert(GameActionManager __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    MonsterGroup leftoverMonsters = BackupMonstersField.backupMonsters.get(AbstractDungeon.getCurrRoom());
                    if (leftoverMonsters != null) {
                        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                            if (!leftoverMonsters.monsters.isEmpty() && m.isDeadOrEscaped() && SpawnBackupField.spawnBackupOnDeath.get(m)) {
                                AbstractMonster monsterToSpawn = leftoverMonsters.monsters.get(0);

                                // Find where to put the monster.
                                monsterToSpawn.drawX = m.drawX;

                                // Make dead monster not spawn backup anymore and make new one able to.
                                SpawnBackupField.spawnBackupOnDeath.set(m, false);
                                SpawnBackupField.spawnBackupOnDeath.set(monsterToSpawn, true);

                                // Spawn the monster.
                                AbstractDungeon.actionManager.addToBottom(new SlideMonsterInAction(monsterToSpawn));
                                leftoverMonsters.monsters.remove(0);
                                if (leftoverMonsters.monsters.isEmpty()) {
                                    AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                                }
                            }
                        }
                    }
                }
            }

            private static class Locator extends SpireInsertLocator {

                @Override
                public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                    Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "monsterQueue");
                    int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                    found[found.length - 1] += 1;
                    return new int[]{found[found.length - 1]};
                }
            }
        }

        @SpirePatch(
                clz = MonsterGroup.class,
                method = "queueMonsters"
        )
        public static class QueueDeadMonstersThatSpawnBackup {

            public static void Postfix(MonsterGroup __instance) {
                if (AbstractDungeon.id.equals(AnomalyTheEnding.ID)) {
                    for (AbstractMonster m : __instance.monsters) {
                        if ((m.isDeadOrEscaped() || m.halfDead) && SpawnBackupField.spawnBackupOnDeath.get(m)) {
                            AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(m));
                        }
                    }
                }
            }
        }

        public static class RenderBackupQueue {

            private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString("anomalyMod:BackupQueue");

            // My tip info. These must be public for the instrument patch.
            public static String header = null;
            public static String body = null;
            public static float drawX = 0.0F;
            public static float drawY = 0.0F;

            @SpirePatch(
                    clz = TopPanel.class,
                    method = "update"
            )
            public static class QueueTip {

                public static void Prefix(TopPanel __instance) {
                    if (AbstractDungeon.id.equals(AnomalyTheEnding.ID) &&
                            BackupMonstersField.backupMonsters.get(AbstractDungeon.getCurrRoom()) != null &&
                            !BackupMonstersField.backupMonsters.get(AbstractDungeon.getCurrRoom()).monsters.isEmpty()) {
                        header = UI_STRINGS.TEXT[0];
                        if (header.equals(SlaverRed.NAME)) {
                            header += UI_STRINGS.TEXT[1];
                        } else if (header.equals(SlaverBlue.NAME)) {
                            header += UI_STRINGS.TEXT[2];
                        }
                        body = createMonsterList(BackupMonstersField.backupMonsters.get(AbstractDungeon.getCurrRoom()));
                        drawX = Settings.WIDTH - 360f * Settings.scale;
                        drawY = Settings.HEIGHT - 200f * Settings.scale;
                    } else {
                        header = null;
                        body = null;
                        drawX = 0.0F;
                        drawY = 0.0F;
                    }
                }

                private static String createMonsterList(MonsterGroup monsterGroup) {
                    String list = "";
                    for (int i = 0; i < monsterGroup.monsters.size(); i++) {
                        list += (i + 1) + ". " + monsterGroup.monsters.get(i).name;
                        if (i < monsterGroup.monsters.size() - 1) {
                            list += " NL ";
                        }
                    }
                    return list;
                }


            }

            @SpirePatch(
                    clz = TipHelper.class,
                    method = "render"
            )
            public static class RenderIndependentTip {

                public static ExprEditor Instrument() {
                    return new ExprEditor() {
                        int findIt = 0;

                        @Override
                        public void edit(FieldAccess f) throws CannotCompileException {
                            if (f.getFieldName().equals("renderedTipThisFrame")) {
                                if (findIt == 0) {
                                    f.replace(
                                            "{" +
                                                    // Do the original field access. We're just using this as a checkpoint for putting the code in.
                                                    "{$_ = $proceed($$);}" +
                                                    // Check if we should be rendering the backup queue. It's null when it shouldn't be rendered.
                                                    "if (!" + Nested.class.getName() + ".renderQueueNull(" +
                                                    RenderBackupQueue.class.getName() + ".header," + RenderBackupQueue.class.getName() + ".body)) {" +
                                                    // Change textHeight in TipHelper (it's private and uses private methods).
                                                    "textHeight = -" + FontHelper.class.getName() + ".getSmartHeight(" +
                                                    FontHelper.class.getName() + ".tipBodyFont," +
                                                    RenderBackupQueue.class.getName() + ".body," +
                                                    "BODY_TEXT_WIDTH, TIP_DESC_LINE_SPACING);" +
                                                    "textHeight -= " + Nested.class.getName() + ".getAdditionalValue();" +
                                                    // Use TipHelper's renderTipBox (also private method).
                                                    "renderTipBox(" +
                                                    RenderBackupQueue.class.getName() + ".drawX," +
                                                    RenderBackupQueue.class.getName() + ".drawY," +
                                                    "sb," +
                                                    RenderBackupQueue.class.getName() + ".header," +
                                                    RenderBackupQueue.class.getName() + ".body);" +
                                                    "}" +
                                                    "}");
                                }
                                findIt++;
                            }
                        }
                    };
                }

                public static class Nested {

                    public static boolean renderQueueNull(String header, String body) {
                        if (!CardCrawlGame.isInARun() || header == null || body == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    // It's safer changing float values with methods.
                    public static float getAdditionalValue() {
                        return 7f * Settings.scale;
                    }
                }
            }
        }
    }

    @SpirePatch(
            clz = DeathScreen.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class ChangeDeathScreen {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(DeathScreen __instance, MonsterGroup m) {
            if (AnomalyModDungeonData.triggerAnomalyDeathVictory) {
                __instance.isVictory = true;
            }
        }

        private static class Locator extends SpireInsertLocator {

            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(DeathScreen.class, "isVictory");
                int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
                return new int[]{found[1]};
            }
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if ((m.getClassName().equals(MusicMaster.class.getName()) && m.getMethodName().equals("dispose")) ||
                            (m.getClassName().equals(SoundMaster.class.getName()) && m.getMethodName().equals("play")) ||
                            (m.getClassName().equals(MusicMaster.class.getName()) && m.getMethodName().equals("playTempBgmInstantly"))) {
                        m.replace(
                                "{" +
                                        "if (!" + Nested.class.getName() + ".isAnomalyVictory()) {$_ = $proceed($$);}" +
                                        "}"
                        );
                    } else if (m.getClassName().equals(DynamicBanner.class.getName()) && m.getMethodName().equals("appear")) {
                        m.replace(
                                "{" +
                                        "if (" + Nested.class.getName() + ".isAnomalyVictory()) {$_ = $proceed(" + Nested.class.getName() + ".getProperDeathText());}" +
                                        "else {$_ = $proceed($$);}" +
                                        "}"
                        );
                    }
                }
            };
        }

        public static class Nested {

            public static boolean isAnomalyVictory() {
                if (AnomalyModDungeonData.triggerAnomalyDeathVictory) {
                    return true;
                } else {
                    return false;
                }
            }

            public static String getProperDeathText() {
                return DeathScreen.TEXT[1];
            }
        }
    }
}
