package AnomalyMod.relics;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Ratchet extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:Ratchet";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private static final int IMPROBABILITY_INTERVAL = 3;
    private static final int IMPROBABILITY_GAIN = 2;

    public Ratchet() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.BOSS, LandingSound.HEAVY);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + IMPROBABILITY_INTERVAL + DESCRIPTIONS[1] + IMPROBABILITY_GAIN + DESCRIPTIONS[2];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 2;
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 2;
    }

    @Override
    public void atTurnStart() {
        this.counter++;
        if (this.counter % IMPROBABILITY_INTERVAL == 0) {
            this.counter = 0;
            flash();
            AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(IMPROBABILITY_GAIN));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Ratchet();
    }
}
