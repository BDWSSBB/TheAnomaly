package AnomalyMod.events.theEnd;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.ToBeDeleted;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import AnomalyMod.helpers.monsterEncounters.AnomalyMonsterHelper;
import AnomalyMod.patches.theEnd.TheEndExclusivePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;

import java.io.IOException;
import java.util.ArrayList;

public class LaunchTerminal extends AbstractImageEvent {

    public static final String ID = "anomalyMod:LaunchTerminal";
    public static final String EVAL_ID = "anomalyMod:LaunchTerminalResults";
    public static final String END1_ID = "anomalyMod:LaunchTerminalSadEnding";
    public static final String END2_ID = "anomalyMod:LaunchTerminalPartialEnding";
    public static final String END3_ID = "anomalyMod:LaunchTerminalTrueEnding";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    private static final EventStrings EVAL_EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(EVAL_ID);
    private static final EventStrings END1_EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(END1_ID);
    private static final EventStrings END2_EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(END2_ID);
    private static final EventStrings END3_EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(END3_ID);
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] EVAL_DESCRIPTIONS = EVAL_EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] END1_DESCRIPTIONS = END1_EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] END2_DESCRIPTIONS = END2_EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] END3_DESCRIPTIONS = END3_EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    public static final String[] EVAL_OPTIONS = EVAL_EVENT_STRINGS.OPTIONS;
    public static final String[] END1_OPTIONS = END1_EVENT_STRINGS.OPTIONS;
    public static final String[] END2_OPTIONS = END2_EVENT_STRINGS.OPTIONS;
    public static final String[] END3_OPTIONS = END3_EVENT_STRINGS.OPTIONS;
    private CurScreen screen;
    private static final int TURN_COST = 8;
    private static final float HEAL_PERCENT = 0.25F;
    private int launchStability = 0;
    private ArrayList<String> componentDialog = new ArrayList<>();
    private ArrayList<String> endingDialog = new ArrayList<>();

    private enum CurScreen {
        INTRO, REVISIT, FIGHT_1, FIGHT_2, EVALUATION, TRANSFER_TO_ENDING, SAD_ENDING, NORMAL_ENDING, TRUE_ENDING
    }

    public LaunchTerminal() {
        super(EVENT_STRINGS.NAME, getStartingDescription(), null);
        if (AbstractDungeon.currMapNode.taken) {
            this.screen = CurScreen.REVISIT;
            this.imageEventText.setDialogOption(OPTIONS[1] + TURN_COST + OPTIONS[2] + getHealAmount() + OPTIONS[3]);
            this.imageEventText.setDialogOption(OPTIONS[4]);
            this.imageEventText.setDialogOption(OPTIONS[5]);
        } else {
            if (!AbstractDungeon.player.hasBlight(ToBeDeleted.ID)) {
                AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, new ToBeDeleted());
            }
            this.screen = CurScreen.INTRO;
            this.imageEventText.setDialogOption(OPTIONS[0]);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO: {
                this.imageEventText.updateBodyText(DESCRIPTIONS[1] + getMissingComponents() + DESCRIPTIONS[8]);
                this.screen = CurScreen.REVISIT;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1] + TURN_COST + OPTIONS[2] + getHealAmount() + OPTIONS[3]);
                this.imageEventText.setDialogOption(OPTIONS[4]);
                this.imageEventText.setDialogOption(OPTIONS[5]);
                break;
            }
            case REVISIT: {
                switch (buttonPressed) {
                    case 0: {
                        ToBeDeleted timeCounter = (ToBeDeleted) AbstractDungeon.player.getBlight(ToBeDeleted.ID);
                        if (timeCounter != null) {
                            timeCounter.changeCounter(-TURN_COST);
                        }
                        AbstractDungeon.player.heal(getHealAmount(), true);
                        this.imageEventText.updateBodyText(DESCRIPTIONS[11]);
                        break;
                    }
                    case 1: {
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.EVENT;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[12]);
                        this.screen = CurScreen.FIGHT_1;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[6]);
                        break;
                    }
                    case 2: {
                        this.openMap();
                        break;
                    }
                }
                break;
            }
            case FIGHT_1: {
                this.screen = CurScreen.FIGHT_2;
                CardCrawlGame.music.dispose();
                CardCrawlGame.music.playTempBgmInstantly("BOSS_ENDING", true);
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC);
                TheEndExclusivePatch.SpawnBackupMonsters.BackupMonstersField.backupMonsters.set(AbstractDungeon.getCurrRoom(), AnomalyMonsterHelper.getBackupMonsters(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC));
                AbstractDungeon.getCurrRoom().cannotLose = true;
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
                AbstractDungeon.lastCombatMetricKey = AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC;
                enterCombatFromImage();
                break;
            }
            case FIGHT_2: {
                this.screen = CurScreen.EVALUATION;
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC2);
                TheEndExclusivePatch.SpawnBackupMonsters.BackupMonstersField.backupMonsters.set(AbstractDungeon.getCurrRoom(), AnomalyMonsterHelper.getBackupMonsters(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC2));
                AbstractDungeon.getCurrRoom().cannotLose = true;
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
                AbstractDungeon.lastCombatMetricKey = AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC2;
                enterCombatFromImage();
                break;
            }
            case EVALUATION: {
                if (!this.componentDialog.isEmpty()) {
                    this.imageEventText.updateBodyText(EVAL_DESCRIPTIONS[1] + getPercentColor(this.launchStability) + this.launchStability + EVAL_DESCRIPTIONS[2] + this.componentDialog.get(0));
                    increaseLaunchStability(componentDialog.get(0));
                    this.componentDialog.remove(0);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(EVAL_OPTIONS[0]);
                } else {
                    this.screen = CurScreen.TRANSFER_TO_ENDING;
                    this.imageEventText.updateBodyText(EVAL_DESCRIPTIONS[1] + getPercentColor(this.launchStability) + this.launchStability + EVAL_DESCRIPTIONS[2] + EVAL_DESCRIPTIONS[15]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(EVAL_OPTIONS[1]);
                }
                break;
            }
            case TRANSFER_TO_ENDING: {
                if (this.launchStability < 60) {
                    this.screen = CurScreen.SAD_ENDING;
                } else if (this.launchStability < 100) {
                    this.screen = CurScreen.NORMAL_ENDING;
                } else {
                    this.screen = CurScreen.TRUE_ENDING;
                }
                CardCrawlGame.music.silenceTempBgmInstantly();
                CardCrawlGame.music.playTempBgmInstantly("ANOMALY_ENDING", true);
                setEndingDialog();
                buttonEffect(0);
                break;
            }
            case SAD_ENDING: {
                if (!this.endingDialog.isEmpty()) {
                    switch (buttonPressed) {
                        case 0: {
                            this.imageEventText.updateBodyText(this.endingDialog.get(0));
                            this.endingDialog.remove(0);
                            this.imageEventText.clearAllDialogs();
                            if (!endingDialog.isEmpty()) {
                                this.imageEventText.setDialogOption(END1_OPTIONS[0]);
                                this.imageEventText.setDialogOption(END1_OPTIONS[1]);
                            } else {
                                this.imageEventText.setDialogOption(END1_OPTIONS[2]);
                            }
                            break;
                        }
                        case 1: {
                            doDeathSequence();
                            break;
                        }
                    }
                } else {
                    doDeathSequence();
                }
                break;
            }
            case NORMAL_ENDING: {
                if (!this.endingDialog.isEmpty()) {
                    switch (buttonPressed) {
                        case 0: {
                            this.imageEventText.updateBodyText(this.endingDialog.get(0));
                            this.endingDialog.remove(0);
                            this.imageEventText.clearAllDialogs();
                            if (!endingDialog.isEmpty()) {
                                this.imageEventText.setDialogOption(END2_OPTIONS[0]);
                                this.imageEventText.setDialogOption(END2_OPTIONS[1]);
                            } else {
                                this.imageEventText.setDialogOption(END2_OPTIONS[2]);
                            }
                            break;
                        }
                        case 1: {
                            ConfigHelper.awakeningEndingUnlocked = true;
                            ConfigHelper.modConfig.setBool(ConfigHelper.AWAKENING_ENDING_KEY, true);
                            try {
                                ConfigHelper.modConfig.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            doDeathSequence();
                            break;
                        }
                    }
                } else {
                    ConfigHelper.awakeningEndingUnlocked = true;
                    ConfigHelper.modConfig.setBool(ConfigHelper.AWAKENING_ENDING_KEY, true);
                    try {
                        ConfigHelper.modConfig.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    doDeathSequence();
                }
                break;
            }
            case TRUE_ENDING: {
                if (!this.endingDialog.isEmpty()) {
                    switch (buttonPressed) {
                        case 0: {
                            this.imageEventText.updateBodyText(this.endingDialog.get(0));
                            this.endingDialog.remove(0);
                            this.imageEventText.clearAllDialogs();
                            if (!endingDialog.isEmpty()) {
                                this.imageEventText.setDialogOption(END3_OPTIONS[0]);
                                this.imageEventText.setDialogOption(END3_OPTIONS[1]);
                            } else {
                                this.imageEventText.setDialogOption(END3_OPTIONS[2]);
                            }
                            break;
                        }
                        case 1: {
                            AbstractDungeon.victoryScreen = new VictoryScreen(null);
                            break;
                        }
                    }
                } else {
                    AbstractDungeon.victoryScreen = new VictoryScreen(null);
                }
                break;
            }
        }
    }

    @Override
    public void reopen() {
        if (this.screen == CurScreen.FIGHT_2 || this.screen == CurScreen.EVALUATION) {
            AbstractDungeon.resetPlayer();
            AbstractDungeon.player.drawX = Settings.WIDTH * 0.25f;
            AbstractDungeon.player.preBattlePrep();
            enterImageFromCombat();

            if (this.screen == CurScreen.FIGHT_2) {
                this.imageEventText.updateBodyText(DESCRIPTIONS[13]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[6]);
            } else if (this.screen == CurScreen.EVALUATION) {
                setComponentDialog();
                this.imageEventText.show(EVAL_EVENT_STRINGS.NAME, EVAL_DESCRIPTIONS[0]);
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(EVAL_OPTIONS[0]);
            }
        }
    }

    private int getHealAmount() {
        return Math.max(1, (int) (AbstractDungeon.player.maxHealth * HEAL_PERCENT));
    }

    private static String getStartingDescription() {
        if (AbstractDungeon.currMapNode.taken) {
            if (getMissingComponents().equals("")) {
                return DESCRIPTIONS[10];
            } else {
                return DESCRIPTIONS[9] + getMissingComponents() + DESCRIPTIONS[8];
            }
        } else {
            return DESCRIPTIONS[0];
        }
    }

    private static String getMissingComponents() {
        ArrayList<String> missingComponentsStrings = new ArrayList<>();

        if (AnomalyModDungeonData.phaseControllerChip == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            missingComponentsStrings.add(DESCRIPTIONS[2]);
        }
        if (AnomalyModDungeonData.neuralCables == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            missingComponentsStrings.add(DESCRIPTIONS[3]);
        }
        if (AnomalyModDungeonData.dataDecoder == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            missingComponentsStrings.add(DESCRIPTIONS[4]);
        }
        if (AnomalyModDungeonData.coolantFan == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            missingComponentsStrings.add(DESCRIPTIONS[5]);
        }
        if (missingComponentsStrings.size() == 0) {
            return "";
        } else if (missingComponentsStrings.size() == 1) {
            return missingComponentsStrings.get(0) + LocalizedStrings.PERIOD;
        } else if (missingComponentsStrings.size() == 2) {
            return missingComponentsStrings.get(0) + DESCRIPTIONS[7] + missingComponentsStrings.get(1) + LocalizedStrings.PERIOD;
        } else {
            String missingComponentsList = "";
            for (int i = 0; i < missingComponentsStrings.size(); i++) {
                missingComponentsList += missingComponentsStrings.get(i);
                if (i == missingComponentsStrings.size() - 1) {
                    missingComponentsList += LocalizedStrings.PERIOD;
                } else {
                    missingComponentsList += DESCRIPTIONS[6];
                }
                if (i == missingComponentsStrings.size() - 2) {
                    missingComponentsList += DESCRIPTIONS[7];
                }
            }
            return missingComponentsList;
        }
    }

    private void setComponentDialog() {
        this.componentDialog.clear();

        if (AnomalyModDungeonData.phaseControllerChip == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[3]);
        } else if (AnomalyModDungeonData.phaseControllerChip == AnomalyModDungeonData.QuestComponentQuality.NORMAL) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[4]);
        } else if (AnomalyModDungeonData.phaseControllerChip == AnomalyModDungeonData.QuestComponentQuality.PERFECT) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[5]);
        }

        if (AnomalyModDungeonData.neuralCables == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[6]);
        } else if (AnomalyModDungeonData.neuralCables == AnomalyModDungeonData.QuestComponentQuality.NORMAL) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[7]);
        } else if (AnomalyModDungeonData.neuralCables == AnomalyModDungeonData.QuestComponentQuality.PERFECT) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[8]);
        }

        if (AnomalyModDungeonData.dataDecoder == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[9]);
        } else if (AnomalyModDungeonData.dataDecoder == AnomalyModDungeonData.QuestComponentQuality.NORMAL) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[10]);
        } else if (AnomalyModDungeonData.dataDecoder == AnomalyModDungeonData.QuestComponentQuality.PERFECT) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[11]);
        }

        if (AnomalyModDungeonData.coolantFan == AnomalyModDungeonData.QuestComponentQuality.NONE) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[12]);
        } else if (AnomalyModDungeonData.coolantFan == AnomalyModDungeonData.QuestComponentQuality.NORMAL) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[13]);
        } else if (AnomalyModDungeonData.coolantFan == AnomalyModDungeonData.QuestComponentQuality.PERFECT) {
            this.componentDialog.add(EVAL_DESCRIPTIONS[14]);
        }
    }

    private void increaseLaunchStability(String componentDialog) {
        if (componentDialog.equals(EVAL_DESCRIPTIONS[5]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[8]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[11]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[14])) {
            this.launchStability += 25;
        } else if (componentDialog.equals(EVAL_DESCRIPTIONS[4]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[7]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[10]) ||
                componentDialog.equals(EVAL_DESCRIPTIONS[13])) {
            this.launchStability += 15;
        }
    }

    private String getPercentColor(int percent) {
        if (percent < 60) {
            return "#r";
        } else if (percent < 100) {
            return "#y";
        } else {
            return "#g";
        }
    }

    private void setEndingDialog() {
        endingDialog.clear();
        switch (this.screen) {
            case SAD_ENDING: {
                this.imageEventText.show(END1_EVENT_STRINGS.NAME, "");
                for (int i = 0; i < END1_DESCRIPTIONS.length; i++) {
                    if (i == 2) {
                        this.endingDialog.add(END1_DESCRIPTIONS[i] + CardCrawlGame.playerName + END1_DESCRIPTIONS[i + 1]);
                        i++;
                    } else {
                        this.endingDialog.add(END1_DESCRIPTIONS[i]);
                    }
                }
                break;
            }
            case NORMAL_ENDING: {
                this.imageEventText.show(END2_EVENT_STRINGS.NAME, "");
                for (int i = 0; i < END2_DESCRIPTIONS.length; i++) {
                    if (i == 2) {
                        this.endingDialog.add(END2_DESCRIPTIONS[i] + CardCrawlGame.playerName + END2_DESCRIPTIONS[i + 1]);
                        i++;
                    } else {
                        this.endingDialog.add(END2_DESCRIPTIONS[i]);
                    }
                }
                break;
            }
            case TRUE_ENDING: {
                this.imageEventText.show(END3_EVENT_STRINGS.NAME, "");
                for (int i = 0; i < END3_DESCRIPTIONS.length; i++) {
                    if (i == 2) {
                        this.endingDialog.add(END3_DESCRIPTIONS[i] + CardCrawlGame.playerName + END3_DESCRIPTIONS[i + 1]);
                        i++;
                    } else {
                        this.endingDialog.add(END3_DESCRIPTIONS[i]);
                    }
                }
                break;
            }
            default: {
                AnomalyMod.logger.info("Wtf!");
            }
        }
    }

    private void doDeathSequence() {
        AnomalyModDungeonData.triggerAnomalyDeathVictory = true;
        AbstractDungeon.player.isDying = true;
        hasFocus = false;
        roomEventText.hide();
        AbstractDungeon.player.isDead = true;
        AbstractDungeon.deathScreen = new DeathScreen(null);
    }
}
