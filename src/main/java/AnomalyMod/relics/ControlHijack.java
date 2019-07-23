package AnomalyMod.relics;

import AnomalyMod.blights.driveEffects.PortableTerminal;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class ControlHijack extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:ControlHijack";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    public static final int CONTROL_HIJACK_IMPROBABILITY_LOSS = 2;

    public ControlHijack() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.STARTER, LandingSound.MAGICAL);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, getUpdatedDescription()));
        this.tips.add(new PowerTip(DESCRIPTIONS[2], FontHelper.colorString(new PortableTerminal().name, "y") + DESCRIPTIONS[3] + CONTROL_HIJACK_IMPROBABILITY_LOSS + DESCRIPTIONS[4]));
        this.tips.add(new PowerTip(DESCRIPTIONS[5], DESCRIPTIONS[6]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FontHelper.colorString(new PortableTerminal().name, "y") + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ControlHijack();
    }
}
