package AnomalyMod.blights.improbabilityDriveInfo;

import AnomalyMod.blights.AbstractAnomalyBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.BlightStrings;

public class ImprobabilityDriveFutureInvadersInfo extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDriveFutureInvadersInfo";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";

    public ImprobabilityDriveFutureInvadersInfo() {
        super(ID, NAME, DESCRIPTION[0], IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
    }
}
