package AnomalyMod.relics;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Nightshirt extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:Nightshirt";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");

    public Nightshirt() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayerEndTurn() {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) c).baseImprobabilityNumber > 0 && !c.isEthereal) {
                c.retain = true;
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Nightshirt();
    }
}
