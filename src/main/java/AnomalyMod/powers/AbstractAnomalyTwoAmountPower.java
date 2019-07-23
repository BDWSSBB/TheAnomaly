package AnomalyMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;

public abstract class AbstractAnomalyTwoAmountPower extends TwoAmountPower {

    public AbstractAnomalyTwoAmountPower() {
        super();
    }

    public float atImprobabilityGive(float improbability) {
        return improbability;
    }

    public float onGainImprobabilityStageOne(float calculatingImprobabilityNumber) {
        return calculatingImprobabilityNumber;
    }

    public int afterFinalImprobabilityCalculations(int finalImprobabilityNumber) {
        return finalImprobabilityNumber;
    }

    public void onActuallyGainImprobability(int improbabilityGained) {

    }
}
