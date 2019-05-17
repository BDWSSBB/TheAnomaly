package AnomalyMod.blights.improbabilityDriveInfo;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.rooms.*;

import java.text.DecimalFormat;

public class ImprobabilityDriveSurpriseElitesInfo extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDriveSurpriseElitesInfo";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/blights/surpriseElites.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/character/placeholder/empty128x128.png";
    public static final int SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM = 40;
    public static final int SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM = 50;
    public static final int SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM = 70;
    public static final int SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM = 80;

    public ImprobabilityDriveSurpriseElitesInfo() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        this.counter = 0;
    }

    @Override
    public void onEquip() {
        changeDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (ImprobabilityDrive.getImprobability() >= SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM) {
            this.counter += 2;
        }
        changeDescription();
    }

    @Override
    public void onActuallyGainImprobability(int improbabilityGained) {
        changeDescription();
    }

    private void changeDescription() {
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        return DESCRIPTION[0] + DESCRIPTION[1] + new DecimalFormat("#.#").format(getPercent()) + DESCRIPTION[2] + getEligibleRooms();
    }

    private static float getPercent() {
        return getSurpriseElitesChance() * 100.0F;
    }

    private static String getEligibleRooms() {
        String eligibleRooms = "";
        ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        if (drive != null) {
            if (drive.counter >= SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[4] + DESCRIPTION[3];
            }
            if (drive.counter >= SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[5] + DESCRIPTION[3];
            }
            if (drive.counter >= SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[6] + DESCRIPTION[3];
            }
            if (drive.counter >= SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[7] + DESCRIPTION[3];
            }
            if (eligibleRooms.endsWith(DESCRIPTION[3])) {
                eligibleRooms = eligibleRooms.substring(0, eligibleRooms.length() - DESCRIPTION[3].length());
            }
        }
        if (eligibleRooms.equals("")) {
            eligibleRooms += DESCRIPTION[8];
        }
        return eligibleRooms;
    }

    public static void rollSurpriseElite() {
        int initialCounter = ImprobabilityDrive.getImprobability();
        if (isValidRoomForSurpriseElite(initialCounter)) {
            ImprobabilityDriveSurpriseElitesInfo blight = (ImprobabilityDriveSurpriseElitesInfo) AbstractDungeon.player.getBlight(ImprobabilityDriveSurpriseElitesInfo.ID);
            if (AnomalyMod.anomalyRNG.randomBoolean(getSurpriseElitesChance())) {
                blight.counter /= 5;
                blight.counter -= 4;
                if (blight.counter < 0) {
                    blight.counter = 0;
                }
                ImprobabilityDrive.changeImprobability(getSurpriseElitesImprobabilityChange(initialCounter));
                AbstractDungeon.nextRoom.room = new MonsterRoomElite();
            }
            else {
                if (AbstractDungeon.nextRoom.room instanceof EventRoom) {
                    blight.counter += 12 * ImprobabilityDrive.getImprobability() / SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM - 7;
                }
                else if (AbstractDungeon.nextRoom.room.getClass().equals(MonsterRoom.class)) {
                    blight.counter += 12 * ImprobabilityDrive.getImprobability() / SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM - 7;
                }
                else if (AbstractDungeon.nextRoom.room instanceof ShopRoom) {
                    blight.counter += 12 * ImprobabilityDrive.getImprobability() / SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM - 7;
                }
                else if (AbstractDungeon.nextRoom.room instanceof RestRoom) {
                    blight.counter += 12 * ImprobabilityDrive.getImprobability() / SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM - 7;
                }
            }
        }
    }

    private static boolean isValidRoomForSurpriseElite(int initialCounter) {
        // The following format is just for readability's sake.
        boolean validRoom = false;
        if (AbstractDungeon.nextRoom == null) {
            AnomalyMod.logger.info("WTF? Why is AbstractDungeon.nextRoom null?");
            return false;
        }
        if (AbstractDungeon.nextRoom.room instanceof EventRoom && initialCounter >= SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room.getClass().equals(MonsterRoom.class) && initialCounter >= SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room instanceof ShopRoom && initialCounter >= SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room instanceof RestRoom && initialCounter >= SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.id == null || AbstractDungeon.id.equals(TheEnding.ID)) {
            validRoom = false;
        }
        return validRoom;
    }

    private static float getSurpriseElitesChance() {
        if (!AbstractDungeon.player.hasBlight(ImprobabilityDriveSurpriseElitesInfo.ID)) {
            return 0.0F;
        }
        ImprobabilityDriveSurpriseElitesInfo blight = (ImprobabilityDriveSurpriseElitesInfo) AbstractDungeon.player.getBlight(ImprobabilityDriveSurpriseElitesInfo.ID);
        return (float) blight.counter / 100.0F;
    }

    private static int getSurpriseElitesImprobabilityChange(int initialCounter) {
        return -initialCounter / 10;
    }
}
