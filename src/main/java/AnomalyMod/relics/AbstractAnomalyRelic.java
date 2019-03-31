package AnomalyMod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;

public abstract class AbstractAnomalyRelic extends CustomRelic {

    public AbstractAnomalyRelic(final String id, final Texture imagePath, final Texture imageOutlinePath, final RelicTier relicTier, final LandingSound landingSound) {
        super(id, imagePath, imageOutlinePath, relicTier, landingSound);
    }

    public AbstractAnomalyRelic(final String id, final Texture imagePath, final RelicTier relicTier, final LandingSound landingSound) {
        super(id, imagePath, relicTier, landingSound);
    }

    public void atStartOfTurnPostPostDraw() {

    }

    public float onGainImprobabilityStageOne(float improbability) {
        return improbability;
    }

    public int afterFinalImprobabilityCalculations(int improbability) {
        return improbability;
    }

    public void onActuallyGainImprobability(int improbabilityGained) {

    }
}
