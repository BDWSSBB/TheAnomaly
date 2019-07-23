package AnomalyMod.helpers.config;

import AnomalyMod.AnomalyMod;
import AnomalyMod.helpers.loadedMods.LoadedModsHelper;
import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {

    // Config and Panel
    public static SpireConfig modConfig = null;
    private static ModPanel settingsPanel = null;

    // Configs with buttons on menu
    public static CustomMusicConfig useCustomMusic;
    public static ModLabeledToggleButton customMusicOffButton;
    public static ModLabeledToggleButton customMusicAnomalyOnlyButton;
    public static ModLabeledToggleButton customMusicOnButton;
    public static final String CUSTOM_MUSIC_KEY = "customMusic";

    public static CustomMusicConfig overrideJungleMusic;
    public static ModLabeledToggleButton overrideJungleMusicOffButton;
    public static ModLabeledToggleButton overrideJungleMusicAnomalyOnlyButton;
    public static ModLabeledToggleButton overrideJungleMusicOnButton;
    public static final String CUSTOM_JUNGLE_MUSIC_KEY = "customJungleMusic";

    public static boolean goToAnomalyAct4;
    public static ModLabeledToggleButton anomalyAct4Button;
    public static final String ANOMALY_ACT_4_KEY = "anomalyAct4";

    public static boolean useSpecialUpgradeNames;
    public static ModLabeledToggleButton upgradeNamesButton;
    public static final String UPGRADE_NAMES_KEY = "upgradeNames";

    public static boolean makePortableTerminalMoveCards;
    public static ModLabeledToggleButton portableTerminalMovesCardsButton;
    public static final String PORTABLE_TERMINAL_KEY = "portableTerminalMoveCards";

    public static boolean shouldFightMe;
    public static ModLabeledToggleButton fightMeButton;
    public static final String FIGHT_ME_KEY = "fightMe";

    // Configs without buttons on menu (basically preferences)
    public static boolean awakeningEndingUnlocked;
    public static final String AWAKENING_ENDING_KEY = "awakeningUnlocked";

    public static boolean foughtAprilFoolsMe;
    public static final String APRIL_FOOLS_KEY = "foughtAprilFoolsMe";

    // Config Display handling
    private static final float X_START = 350.0F;
    private static final float Y_START = 750.0F;
    private static final float Y_SPACING = -50.0F;
    private static float currentX = X_START;
    private static float currentY = Y_START;

    public enum CustomMusicConfig {
        OFF,
        ANOMALY,
        ON
    }

    public static void setupConfig() {
        try {
            Properties defaults = new Properties();
            defaults.put(CUSTOM_MUSIC_KEY, "ANOMALY_ONLY");
            defaults.put(CUSTOM_JUNGLE_MUSIC_KEY, "ANOMALY_ONLY");
            defaults.put(ANOMALY_ACT_4_KEY, Boolean.toString(true));
            defaults.put(UPGRADE_NAMES_KEY, Boolean.toString(true));
            defaults.put(PORTABLE_TERMINAL_KEY, Boolean.toString(true));
            defaults.put(FIGHT_ME_KEY, Boolean.toString(false));
            defaults.put(AWAKENING_ENDING_KEY, Boolean.toString(false));
            defaults.put(APRIL_FOOLS_KEY, Boolean.toString(false));
            modConfig = new SpireConfig("TheAnomaly", "config", defaults);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (modConfig.getString(CUSTOM_MUSIC_KEY)) {
            case "OFF": {
                useCustomMusic = CustomMusicConfig.OFF;
                break;
            }
            case "ANOMALY_ONLY": {
                useCustomMusic = CustomMusicConfig.ANOMALY;
                break;
            }
            case "ON": {
                useCustomMusic = CustomMusicConfig.ON;
                break;
            }
            default: {
                AnomalyMod.logger.info("WARNING: Key " + CUSTOM_MUSIC_KEY + " in config resorted to default.");
                useCustomMusic = CustomMusicConfig.ANOMALY;
                break;
            }
        }
        switch (modConfig.getString(CUSTOM_JUNGLE_MUSIC_KEY)) {
            case "OFF": {
                overrideJungleMusic = CustomMusicConfig.OFF;
                break;
            }
            case "ANOMALY_ONLY": {
                overrideJungleMusic = CustomMusicConfig.ANOMALY;
                break;
            }
            case "ON": {
                overrideJungleMusic = CustomMusicConfig.ON;
                break;
            }
            default: {
                AnomalyMod.logger.info("WARNING: Key " + CUSTOM_JUNGLE_MUSIC_KEY + " in config resorted to default.");
                overrideJungleMusic = CustomMusicConfig.ANOMALY;
                break;
            }
        }
        goToAnomalyAct4 = modConfig.getBool(ANOMALY_ACT_4_KEY);
        useSpecialUpgradeNames = modConfig.getBool(UPGRADE_NAMES_KEY);
        makePortableTerminalMoveCards = modConfig.getBool(PORTABLE_TERMINAL_KEY);
        shouldFightMe = modConfig.getBool(FIGHT_ME_KEY);
        awakeningEndingUnlocked = modConfig.getBool(AWAKENING_ENDING_KEY);
        foughtAprilFoolsMe = modConfig.getBool(APRIL_FOOLS_KEY);
    }

    public static void initializeConfig() {
        settingsPanel = new ModPanel();

        // Custom Music
        settingsPanel.addUIElement(new ModLabel("Custom Music", currentX, currentY, settingsPanel, label -> {
        }));
        spaceY();
        customMusicOffButton = new ModLabeledToggleButton("Off",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.OFF, settingsPanel, label -> {
        },
                button -> {
                    useCustomMusic = CustomMusicConfig.OFF;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString(CUSTOM_MUSIC_KEY, "OFF");
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicOffButton);
        if (LoadedModsHelper.jungleModLoaded) {
            overrideJungleMusicOffButton = new ModLabeledToggleButton("Jungle: Off",
                    currentX + 250.0F, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.OFF, settingsPanel, label -> {
            },
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.OFF;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString(CUSTOM_JUNGLE_MUSIC_KEY, "OFF");
                            try {
                                modConfig.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicOffButton);
        }
        spaceY();
        customMusicAnomalyOnlyButton = new ModLabeledToggleButton("Anomaly only",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.ANOMALY, settingsPanel, label -> {
        },
                button -> {
                    useCustomMusic = CustomMusicConfig.ANOMALY;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString(CUSTOM_MUSIC_KEY, "ANOMALY_ONLY");
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicAnomalyOnlyButton);
        if (LoadedModsHelper.jungleModLoaded) {
            overrideJungleMusicAnomalyOnlyButton = new ModLabeledToggleButton("Jungle: Anomaly only",
                    currentX + 250.0F, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.ANOMALY, settingsPanel, label -> {
            },
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.ANOMALY;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString(CUSTOM_JUNGLE_MUSIC_KEY, "ANOMALY_ONLY");
                            try {
                                modConfig.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicAnomalyOnlyButton);
        }
        spaceY();
        customMusicOnButton = new ModLabeledToggleButton("On",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.ON, settingsPanel, label -> {
        },
                button -> {
                    useCustomMusic = CustomMusicConfig.ON;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString(CUSTOM_MUSIC_KEY, "ON");
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicOnButton);
        if (LoadedModsHelper.jungleModLoaded) {
            overrideJungleMusicOnButton = new ModLabeledToggleButton("Jungle: On",
                    currentX + 250.0F, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.ON, settingsPanel, label -> {
            },
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.ON;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString(CUSTOM_JUNGLE_MUSIC_KEY, "ON");
                            try {
                                modConfig.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicOnButton);
        }
        spaceY();

        spaceY();

        // Anomaly goes to a special Act 4
        anomalyAct4Button = new ModLabeledToggleButton("Redirect Anomaly to a special Act 4.",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                goToAnomalyAct4, settingsPanel, label -> {
        },
                button -> {
                    goToAnomalyAct4 = button.enabled;
                    if (modConfig != null) {
                        modConfig.setBool(ANOMALY_ACT_4_KEY, button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(anomalyAct4Button);
        spaceY();

        // Use special upgrade names on certain Anomaly cards
        upgradeNamesButton = new ModLabeledToggleButton("Use special upgrade names for certain Anomaly cards.",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useSpecialUpgradeNames, settingsPanel, label -> {
        },
                button -> {
                    useSpecialUpgradeNames = button.enabled;
                    if (modConfig != null) {
                        modConfig.setBool(UPGRADE_NAMES_KEY, button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(upgradeNamesButton);
        spaceY();

        // Portable Terminal moves Improb cards to the left (for easy sorting)
        portableTerminalMovesCardsButton = new ModLabeledToggleButton("Have Portable Terminal move cards with Improbability to the left for sorting.",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                makePortableTerminalMoveCards, settingsPanel, label -> {
        },
                button -> {
                    makePortableTerminalMoveCards = button.enabled;
                    if (modConfig != null) {
                        modConfig.setBool(PORTABLE_TERMINAL_KEY, button.enabled);
                        try {
                            modConfig.save();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(portableTerminalMovesCardsButton);
        spaceY();

        // (I'm not ready to fight yet)
//        // FIGHT ME
//        if (LoadedModsHelper.strawberrySpireModLoaded) {
//            fightMeButton = new ModLabeledToggleButton("Fight the evil dev.",
//                    currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
//                    shouldFightMe, settingsPanel, label -> {},
//                    button -> {
//                        shouldFightMe = button.enabled;
//                        if (modConfig != null) {
//                            modConfig.setBool(FIGHT_ME_KEY, button.enabled);
//                            try {
//                                modConfig.save();
//                            }
//                            catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//            settingsPanel.addUIElement(fightMeButton);
//        }
//        spaceY();

        BaseMod.registerModBadge(ImageMaster.loadImage("AnomalyModResources/anomaly/placeholderBadge.png"), "The Anomaly", "BDWSSBB", "TODO", settingsPanel);
    }

    private static void spaceY() {
        currentY += Y_SPACING;
    }

    private static void resetCustomMusicButtons() {
        customMusicOffButton.toggle.enabled = false;
        customMusicAnomalyOnlyButton.toggle.enabled = false;
        customMusicOnButton.toggle.enabled = false;
    }

    private static void resetOverrideJungleMusicButtons() {
        overrideJungleMusicOffButton.toggle.enabled = false;
        overrideJungleMusicAnomalyOnlyButton.toggle.enabled = false;
        overrideJungleMusicOnButton.toggle.enabled = false;
    }
}
