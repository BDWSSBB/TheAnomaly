package AnomalyMod.blights.improbabilityDriveInfo;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.cards.status.BadSector;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ImprobabilityDriveMalfunctionInfo extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDriveMalfunctionInfo";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";

    public ImprobabilityDriveMalfunctionInfo() {
        super(ID, NAME, DESCRIPTION[0], IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        if (Settings.language == Settings.GameLanguage.ENG) {
            this.tips.add(new PowerTip(TipHelper.capitalize("bad sector"), GameDictionary.keywords.get("bad sector")));
        }
        this.initializeTips();
    }

    @Override
    public void onBossDefeat() {
        if (!Settings.isEndless) {
            this.flash();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new BadSector().makeCopy(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
    }
}
