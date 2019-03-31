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

    private static SpireConfig modConfig = null;
    private static ModPanel settingsPanel = null;
    public static CustomMusicConfig useCustomMusic;
    public static ModLabeledToggleButton customMusicOffButton;
    public static ModLabeledToggleButton customMusicAnomalyOnlyButton;
    public static ModLabeledToggleButton customMusicOnButton;
    public static CustomMusicConfig overrideJungleMusic;
    public static ModLabeledToggleButton overrideJungleMusicOffButton;
    public static ModLabeledToggleButton overrideJungleMusicAnomalyOnlyButton;
    public static ModLabeledToggleButton overrideJungleMusicOnButton;
    public static boolean useSpecialUpgradeNames;
    public static ModLabeledToggleButton upgradeNamesButton;
    private static final float X_START = 350.0F;
    private static final float Y_START = 750.0F;
    private static final float Y_SPACING = -50.0F;
    private static float currentX = X_START;
    private static float currentY = Y_START;

    public static void setupConfig() {
        try {
            Properties defaults = new Properties();
            defaults.put("customMusic", "ANOMALY_ONLY");
            defaults.put("customJungleMusic", "ANOMALY_ONLY");
            defaults.put("upgradeNames", true);
            modConfig = new SpireConfig("TheAnomaly", "config", defaults);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        switch (modConfig.getString("customMusic")) {
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
                AnomalyMod.logger.info("WARNING: Key customMusic in config resorted to default.");
                useCustomMusic = CustomMusicConfig.ANOMALY;
                break;
            }
        }
        switch (modConfig.getString("customJungleMusic")) {
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
                AnomalyMod.logger.info("WARNING: Key customJungleMusic in config resorted to default.");
                overrideJungleMusic = CustomMusicConfig.ANOMALY;
                break;
            }
        }
        if (modConfig.getBool("upgradeNames")) {
            useSpecialUpgradeNames = true;
        }
        else {
            useSpecialUpgradeNames = false;
        }
    }

    public static void initializeConfig() {
        settingsPanel = new ModPanel();

        // Custom Music
        settingsPanel.addUIElement(new ModLabel("Custom Music", currentX, currentY, settingsPanel, label -> {}));
        spaceY();
        customMusicOffButton = new ModLabeledToggleButton("Off",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.OFF, settingsPanel, label -> {},
                button -> {
                    useCustomMusic = CustomMusicConfig.OFF;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString("customMusic", "OFF");
                        try {
                            modConfig.save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicOffButton);
        spaceY();
        customMusicAnomalyOnlyButton = new ModLabeledToggleButton("Anomaly only",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.ANOMALY, settingsPanel, label -> {},
                button -> {
                    useCustomMusic = CustomMusicConfig.ANOMALY;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString("customMusic", "ANOMALY_ONLY");
                        try {
                            modConfig.save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicAnomalyOnlyButton);
        spaceY();
        customMusicOnButton = new ModLabeledToggleButton("On",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useCustomMusic == CustomMusicConfig.ON, settingsPanel, label -> {},
                button -> {
                    useCustomMusic = CustomMusicConfig.ON;
                    resetCustomMusicButtons();
                    button.enabled = true;
                    if (modConfig != null) {
                        modConfig.setString("customMusic", "ON");
                        try {
                            modConfig.save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(customMusicOnButton);
        spaceY();

        // Override Jungle Music
        if (LoadedModsHelper.jungleModLoaded) {
            settingsPanel.addUIElement(new ModLabel("Override Jungle Music (Independent of Custom Music)", currentX, currentY, settingsPanel, label -> {}));
            spaceY();
            overrideJungleMusicOffButton = new ModLabeledToggleButton("Off",
                    currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.OFF, settingsPanel, label -> {},
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.OFF;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString("customJungleMusic", "OFF");
                            try {
                                modConfig.save();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicOffButton);
            spaceY();
            overrideJungleMusicAnomalyOnlyButton = new ModLabeledToggleButton("Anomaly only",
                    currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.ANOMALY, settingsPanel, label -> {},
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.ANOMALY;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString("customJungleMusic", "ANOMALY_ONLY");
                            try {
                                modConfig.save();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicAnomalyOnlyButton);
            spaceY();
            overrideJungleMusicOnButton = new ModLabeledToggleButton("On",
                    currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                    overrideJungleMusic == CustomMusicConfig.ON, settingsPanel, label -> {},
                    button -> {
                        overrideJungleMusic = CustomMusicConfig.ON;
                        resetOverrideJungleMusicButtons();
                        button.enabled = true;
                        if (modConfig != null) {
                            modConfig.setString("customJungleMusic", "ON");
                            try {
                                modConfig.save();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            settingsPanel.addUIElement(overrideJungleMusicOnButton);
            spaceY();
        }

        // Use special upgrade names on certain Anomaly cards
        spaceY();
        upgradeNamesButton = new ModLabeledToggleButton("Use special upgrade names for certain Anomaly cards.",
                currentX, currentY, Settings.CREAM_COLOR, FontHelper.charDescFont,
                useSpecialUpgradeNames = true, settingsPanel, label -> {},
                button -> {
                    if (modConfig != null) {
                        modConfig.setBool("upgradeNames", button.enabled);
                        try {
                            modConfig.save();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        settingsPanel.addUIElement(upgradeNamesButton);
        spaceY();

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

    public enum CustomMusicConfig {
        OFF,
        ANOMALY,
        ON
    }
}
