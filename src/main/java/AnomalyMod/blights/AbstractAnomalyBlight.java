package AnomalyMod.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractAnomalyBlight extends AbstractBlight {

    public AbstractAnomalyBlight(String setId, String name, String description, String imgName, String outlineName, boolean unique) {
        super(setId, name, description, imgName, unique);
        this.img = ImageMaster.loadImage(imgName);
        this.outlineImg = ImageMaster.loadImage(outlineName);
    }

    public void atStartOfTurnPostPostDraw() {

    }

    public float onGainImprobabilityStageOne(float calculatingImprobabilityNumber) {
        return calculatingImprobabilityNumber;
    }

    public int afterFinalImprobabilityCalculations(int finalImprobabilityNumber) {
        return finalImprobabilityNumber;
    }

    public void onActuallyGainImprobability(int improbabilityGained) {

    }

    public void onSetupItemRewards() {

    }

    public void onEnterRoom(AbstractRoom room) {

    }
}
