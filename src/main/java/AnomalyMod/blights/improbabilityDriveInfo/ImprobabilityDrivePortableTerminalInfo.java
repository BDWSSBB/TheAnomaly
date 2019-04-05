package AnomalyMod.blights.improbabilityDriveInfo;

import AnomalyMod.actions.unique.PortableTerminalAction;
import AnomalyMod.blights.AbstractAnomalyBlight;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.relics.GamblingChip;

public class ImprobabilityDrivePortableTerminalInfo extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ImprobabilityDrivePortableTerminalInfo";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";
    public static final int IMPROBABILITY_LOSS = 1;

    public ImprobabilityDrivePortableTerminalInfo() {
        super(ID, NAME, DESCRIPTION[0] + IMPROBABILITY_LOSS + DESCRIPTION[1], IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        this.initializeTips();
    }

    @Override
    public void atBattleStart() {
        if (AbstractDungeon.player.hasRelic(GamblingChip.ID)) {
            AbstractDungeon.actionManager.addToTop(new TalkAction(true, DESCRIPTION[2] + new GamblingChip().name + DESCRIPTION[3], 0.0F, 2.0F));
        }
    }

    @Override
    public void atStartOfTurnPostPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new PortableTerminalAction(ImprobabilityDrivePortableTerminalInfo.IMPROBABILITY_LOSS));
    }
}
