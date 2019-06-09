package AnomalyMod.blights;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.improbabilityDriveInfo.*;
import AnomalyMod.powers.AbstractAnomalyPower;
import AnomalyMod.powers.AbstractAnomalyTwoAmountPower;
import AnomalyMod.relics.AbstractAnomalyRelic;
import AnomalyMod.vfx.ObtainBlightLater;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ImprobabilityDrive extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDrive";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";
    private static int queuedImprobability = 0;

    public ImprobabilityDrive() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        this.counter = 0;
        checkForSpawnInfoBlights();
        changeDescription();
    }

    @Override
    public void updateDescriptionFromImprobabilityChange() {
        changeDescription();
    }

    private void changeDescription() {
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        String toEdit = "";
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveTryNewThingsInfo.ID, ImprobabilityDriveTryNewThingsInfo.CONVERT_GOLD_IMPROBABILITY_MINIMUM);
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveSpecialtyInfo.ID, ImprobabilityDriveSpecialtyInfo.SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM);
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveSurpriseElitesInfo.ID, ImprobabilityDriveSurpriseElitesInfo.SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM);
        return DESCRIPTION[0] + toEdit;
    }

    private static String upcomingInfoBlightCheck(String toEdit, String blightID, int effectMinimum) {
        if (toEdit.equals("") && !AbstractDungeon.player.hasBlight(blightID)) {
            return DESCRIPTION[1] + effectMinimum + DESCRIPTION[2] + BlightHelper.getBlight(blightID).name;
        }
        return toEdit;
    }

    public static int getImprobability() {
        if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID)) {
            return AbstractDungeon.player.getBlight(ImprobabilityDrive.ID).counter;
        }
        return 0;
    }

    public static void changeImprobability(int improbabilityNumber) {
        ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        queueChangeImprobability(improbabilityNumber);
        if (drive != null && queuedImprobability != 0) {
            drive.counter += queuedImprobability;
            queuedImprobability = 0;
            drive.flash();
        }
        if (drive.counter < 0) {
            drive.counter = 0;
        }
        checkForSpawnInfoBlights();
        drive.changeDescription();
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                ((AbstractAnomalyBlight) b).updateDescriptionFromImprobabilityChange();
            }
        }
    }

    public static void queueChangeImprobability(int improbabilityNumber) {
        int calculatedImprobabilityNumber = (int) doOnGainImprobabilityStageOne((float) improbabilityNumber);
        if (calculatedImprobabilityNumber != 0) {
            int finalImprobabilityNumber = doAfterFinalImprobabilityCalculations(calculatedImprobabilityNumber);
            if (finalImprobabilityNumber != 0) {
                if (!AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID) && finalImprobabilityNumber > 0) {
                    AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, new ImprobabilityDrive());
                }
                queuedImprobability += finalImprobabilityNumber;
                doOnActuallyGainImprobability(finalImprobabilityNumber);
            }
        }
    }

    private static float doOnGainImprobabilityStageOne(float calculatingImprobabilityNumber) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                calculatingImprobabilityNumber = ((AbstractAnomalyRelic) r).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                calculatingImprobabilityNumber = ((AbstractAnomalyPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                calculatingImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        calculatingImprobabilityNumber = ((AbstractAnomalyPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        calculatingImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                calculatingImprobabilityNumber = ((AbstractAnomalyBlight) b).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        return calculatingImprobabilityNumber;
    }

    private static int doAfterFinalImprobabilityCalculations(int finalImprobabilityNumber) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                finalImprobabilityNumber = ((AbstractAnomalyRelic) r).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                finalImprobabilityNumber = ((AbstractAnomalyPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                finalImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        finalImprobabilityNumber = ((AbstractAnomalyPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        finalImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                finalImprobabilityNumber = ((AbstractAnomalyBlight) b).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        return finalImprobabilityNumber;
    }

    private static void doOnActuallyGainImprobability(int improbabilityGained) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                ((AbstractAnomalyRelic) r).onActuallyGainImprobability(improbabilityGained);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                ((AbstractAnomalyPower) p).onActuallyGainImprobability(improbabilityGained);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                ((AbstractAnomalyTwoAmountPower) p).onActuallyGainImprobability(improbabilityGained);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        ((AbstractAnomalyPower) p).onActuallyGainImprobability(improbabilityGained);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        ((AbstractAnomalyTwoAmountPower) p).onActuallyGainImprobability(improbabilityGained);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                ((AbstractAnomalyBlight) b).onActuallyGainImprobability(improbabilityGained);
            }
        }
    }

    public static void checkForSpawnInfoBlights() {
        spawnInfoBlightCheck(ImprobabilityDrivePortableTerminalInfo.ID, 0, true);
        spawnInfoBlightCheck(ImprobabilityDriveMalfunctionInfo.ID, 0, !Settings.isEndless);
        spawnInfoBlightCheck(ImprobabilityDriveRandomBuffsInfo.ID, 0, true);
        spawnInfoBlightCheck(ImprobabilityDriveTryNewThingsInfo.ID, ImprobabilityDriveTryNewThingsInfo.CONVERT_GOLD_IMPROBABILITY_MINIMUM, true);
        spawnInfoBlightCheck(ImprobabilityDriveSpecialtyInfo.ID, ImprobabilityDriveSpecialtyInfo.SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM, true);
        spawnInfoBlightCheck(ImprobabilityDriveSurpriseElitesInfo.ID, ImprobabilityDriveSurpriseElitesInfo.SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM, true);
        if (Settings.isEndless && AbstractDungeon.player.hasBlight(ImprobabilityDriveMalfunctionInfo.ID)) { // BLAAAAAANK
            AbstractDungeon.player.blights.remove(AbstractDungeon.player.getBlight(ImprobabilityDrive.ID));
        }
    }

    private static void spawnInfoBlightCheck(String blightID, int effectMinimum, boolean extraRequirements) {
        ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        if (!AbstractDungeon.player.hasBlight(blightID) && drive != null && drive.counter >= effectMinimum && extraRequirements) {
            AbstractDungeon.effectsQueue.add(new ObtainBlightLater(BlightHelper.getBlight(blightID), true));
        }
    }
}
