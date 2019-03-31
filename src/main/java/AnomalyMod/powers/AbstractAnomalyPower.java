package AnomalyMod.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractAnomalyPower extends AbstractPower {

    public AbstractAnomalyPower() {
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

    public int onAttackedButCanActuallyChangeNumbers(DamageInfo info, int damageAmount) {
        return damageAmount;
    }
}
