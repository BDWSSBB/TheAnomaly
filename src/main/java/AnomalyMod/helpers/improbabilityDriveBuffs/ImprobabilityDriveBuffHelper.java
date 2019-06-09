package AnomalyMod.helpers.improbabilityDriveBuffs;

import AnomalyMod.AnomalyMod;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class ImprobabilityDriveBuffHelper {

    private static final float NORMALIZATION_CONSTANT = 1.5F;

    public static void doBuffs(ArrayList<AbstractImprobabilityDriveBuffModule> buffModules, float initialPowerLevel) {

        // We'll use this to keep track of the levels we have left to invest.
        float powerLevel = initialPowerLevel;

        // For the selection process. It'll thin down over trials.
        ArrayList<AbstractImprobabilityDriveBuffModule> possibleBuffs = new ArrayList<>();


        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            if (m.isUnique) {
                // Unique buffs take a % of the power level and are guaranteed.
                powerLevel *= 1.0F - m.costPerSelect;
            } else if (!(m.costPerSelect > powerLevel || m.selectionWeight <= 0.0F)) {
                possibleBuffs.add(m);
            }
            if (m.freeridingFactor >= 0.0F) {
                // Freeriding factor is basically how much the module scales regardless of how the selection process goes.
                m.totalAmount += initialPowerLevel * m.freeridingFactor * m.amountPerSelect / m.costPerSelect;
            }
        }

        // We need these for a normalization process.
        float powerLevelAfterUniqueBuffs = powerLevel;
        float sumOfOriginalWeights = 0.0F;
        for (AbstractImprobabilityDriveBuffModule m : possibleBuffs) {
            sumOfOriginalWeights += m.originalSelectionWeight;
        }

        // Keep selecting modules at random until we can't select anymore, or someone screws up the system.
        for (int failsafeCounter = 0; !possibleBuffs.isEmpty() && failsafeCounter < 10000; failsafeCounter++) {

            // Distribute the module's weights.
            float sumOfWeights = 0.0F;
            float[] weightCheckpoints = new float[possibleBuffs.size()];
            for (AbstractImprobabilityDriveBuffModule m : possibleBuffs) {
                sumOfWeights += m.selectionWeight;
                weightCheckpoints[possibleBuffs.indexOf(m)] = sumOfWeights;
            }

            // ROLL!
            float roll = AbstractDungeon.miscRng.random(sumOfWeights);

            for (int i = 0; i < weightCheckpoints.length; i++) {

                // Locate where we rolled.
                if (roll <= weightCheckpoints[i]) {
                    powerLevel -= possibleBuffs.get(i).costPerSelect;
                    possibleBuffs.get(i).totalAmount += possibleBuffs.get(i).amountPerSelect;

                    // This is some weird math to adjust a module's selection weight so it's less likely it'll be rerolled (until other modules are rolled). Take stats.
                    possibleBuffs.get(i).selectionWeight /= Math.pow(2, NORMALIZATION_CONSTANT * sumOfOriginalWeights / possibleBuffs.get(i).originalSelectionWeight / powerLevelAfterUniqueBuffs);

                    AnomalyMod.logger.info("DEBUG: Buff Module " + i + "'s weight: " + possibleBuffs.get(i).selectionWeight);
                    break;
                }
            }

            // Any modules with selection weights over what we have left are removed.
            for (AbstractImprobabilityDriveBuffModule m : buffModules) {
                if (possibleBuffs.contains(m) && m.costPerSelect > powerLevel) {
                    possibleBuffs.remove(m);
                }
            }

            // Just how.
            if (failsafeCounter == 9999) {
                // Seriously, what the hell.
                AnomalyMod.logger.info("WTF have you done to Random Buffs?");
            }
        }

        // Ideally there should only be one of these. Distribute the leftover points to a selected module, hence its name.
        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            if (m.isRemainderModule) {
                m.totalAmount += powerLevel * m.amountPerSelect / m.costPerSelect;
                break;
            }
        }

        // LET'S GET STRONK
        for (AbstractImprobabilityDriveBuffModule m : buffModules) {
            m.doAction();
        }
    }
}
