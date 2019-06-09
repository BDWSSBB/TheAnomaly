package AnomalyMod.relics;

import AnomalyMod.blights.improbabilityDriveInfo.ImprobabilityDrivePortableTerminalInfo;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AuditingHijack extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:AuditingHijack";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    public static final int AUDITING_HIJACK_IMPROBABILITY_LOSS = 2;

    public AuditingHijack() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.BOSS, LandingSound.MAGICAL);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, getUpdatedDescription()));
        this.tips.add(new PowerTip(DESCRIPTIONS[1], FontHelper.colorString(new ImprobabilityDrivePortableTerminalInfo().name, "y") + DESCRIPTIONS[2] + AUDITING_HIJACK_IMPROBABILITY_LOSS + DESCRIPTIONS[3]));
        initializeTips();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FontHelper.colorString(new ImprobabilityDrivePortableTerminalInfo().name, "y");
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(ControlHijack.ID)) {
            instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.indexOf(AbstractDungeon.player.getRelic(ControlHijack.ID)), true);
        } else {
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(ControlHijack.ID);
    }

    public AbstractRelic makeCopy() {
        return new AuditingHijack();
    }
}
