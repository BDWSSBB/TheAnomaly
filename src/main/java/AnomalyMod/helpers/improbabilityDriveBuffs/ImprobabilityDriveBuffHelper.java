package AnomalyMod.helpers.improbabilityDriveBuffs;

import AnomalyMod.AnomalyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ImprobabilityDriveBuffHelper {

    private static final float NORMALIZATION_CONSTANT = 1.5F;

    public static void doBuffs(ArrayList<AbstractImprobabilityDriveBuffModule> buffModules, float initialPowerLevel) {
        float powerLevel = initialPowerLevel;
        ArrayList<AbstractImprobabilityDriveBuffModule> possibleBuffs = new ArrayList<>();
        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            if (m.isUnique) {
                powerLevel *= 1.0F - m.costPerSelect;
            }
            else if (!(m.costPerSelect > powerLevel || m.selectionWeight <= 0.0F)) {
                possibleBuffs.add(m);
            }
            if (m.freeridingFactor >= 0.0F) {
                m.totalAmount += initialPowerLevel * m.freeridingFactor * m.amountPerSelect / m.costPerSelect;
            }
        }
        float powerLevelAfterUniqueBuffs = powerLevel;
        float sumOfOriginalWeights = 0.0F;
        for (AbstractImprobabilityDriveBuffModule m : possibleBuffs) {
            sumOfOriginalWeights += m.originalSelectionWeight;
        }
        for (int failsafeCounter = 0; !possibleBuffs.isEmpty() && failsafeCounter < 10000; failsafeCounter++) {
            float sumOfWeights = 0.0F;
            float[] weightCheckpoints = new float[possibleBuffs.size()];
            for (AbstractImprobabilityDriveBuffModule m : possibleBuffs) {
                sumOfWeights += m.selectionWeight;
                weightCheckpoints[possibleBuffs.indexOf(m)] = sumOfWeights;
            }
            float roll = AbstractDungeon.miscRng.random(sumOfWeights);
            for (int i = 0; i < weightCheckpoints.length; i++) {
                if (roll <= weightCheckpoints[i]) {
                    possibleBuffs.get(i).totalAmount += possibleBuffs.get(i).amountPerSelect;
                    possibleBuffs.get(i).selectionWeight /= Math.pow(2, NORMALIZATION_CONSTANT * sumOfOriginalWeights / possibleBuffs.get(i).originalSelectionWeight / powerLevelAfterUniqueBuffs);
                    powerLevel -= possibleBuffs.get(i).costPerSelect;
                    AnomalyMod.logger.info("DEBUG: Buff Module " + i + "'s weight: " + possibleBuffs.get(i).selectionWeight);
                    break;
                }
            }
            for (AbstractImprobabilityDriveBuffModule m : buffModules) {
                if (possibleBuffs.contains(m) && m.costPerSelect > powerLevel) {
                    possibleBuffs.remove(m);
                }
            }
            if (failsafeCounter == 9999) {
                AnomalyMod.logger.info("WTF have you done to Random Buffs?");
            }
        }
        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            if (m.isRemainderModule) {
                m.totalAmount += powerLevel * m.amountPerSelect / m.costPerSelect;
                break;
            }
        }
        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            m.doAction();
        }
    }
}
