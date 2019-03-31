package AnomalyMod.blights.improbabilityDriveInfo;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.DecimalFormat;

public class ImprobabilityDriveSurpriseElitesInfo extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDriveSurpriseElitesInfo";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";

    public ImprobabilityDriveSurpriseElitesInfo() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        changeDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
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
        if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID)) {
            ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
            return drive.getSurpriseElitesChance(drive.counter) * 100.0F;
        }
        else {
            return 0.0F;
        }
    }

    private static String getEligibleRooms() {
        String eligibleRooms = "";
        ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        if (drive != null) {
            if (drive.counter >= ImprobabilityDrive.SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[4] + DESCRIPTION[3];
            }
            if (drive.counter >= ImprobabilityDrive.SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[5] + DESCRIPTION[3];
            }
            if (drive.counter >= ImprobabilityDrive.SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM) {
                eligibleRooms += DESCRIPTION[6] + DESCRIPTION[3];
            }
            if (drive.counter >= ImprobabilityDrive.SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM) {
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
}
