package AnomalyMod.blights.driveEffects;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FattenUp extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:FattenUp";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/blights/fattenUp.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/blights/outline/fattenUp.png";
    public static final int CONVERT_GOLD_IMPROBABILITY_MINIMUM = 12;
    public static int cardRewardStrength = 0;
    public static boolean convertingGoldToCard = false;

    public FattenUp() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
    }

    @Override
    public void onEquip() {
        changeDescription();
    }

    @Override
    public void updateDescriptionFromImprobabilityChange() {
        changeDescription();
    }

    private void changeDescription() {
        this.counter = (int) (getConvertGoldChance(ImprobabilityDrive.getImprobability()) * 100.0F);
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        return DESCRIPTION[0] + DESCRIPTION[1] + new DecimalFormat("#.#").format(getPercent()) + DESCRIPTION[2];
    }

    private static float getPercent() {
        return getConvertGoldChance(ImprobabilityDrive.getImprobability()) * 100.0F;
    }

    public static void convertGoldToRewards() {
        int initialCounter = ImprobabilityDrive.getImprobability();

        // I just use cardRng for this stuff to avoid the mess of save and load problems with my own RNG.
        if (initialCounter >= CONVERT_GOLD_IMPROBABILITY_MINIMUM && AbstractDungeon.cardRng.randomBoolean(getConvertGoldChance(initialCounter))) {
            cardRewardStrength = 0;
            ArrayList<RewardItem> rewardsToRemove = new ArrayList<>();
            for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
                if (r.goldAmt > 0 || r.bonusGold > 0) {
                    rewardsToRemove.add(r);
                }
            }
            for (RewardItem r : rewardsToRemove) {
                AbstractDungeon.combatRewardScreen.rewards.remove(r);
                cardRewardStrength += r.goldAmt + r.bonusGold;
            }
            if (cardRewardStrength > 0) {
                convertingGoldToCard = true;
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
                convertingGoldToCard = false;
            }
            cardRewardStrength = 0;
        }
        // Sometimes there are empty card rewards (e.g. Busted Crown), so this should do another check.
        ArrayList<RewardItem> emptyRewards = new ArrayList<>();
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.CARD && r.cards.size() == 0) {
                emptyRewards.add(r);
            }
        }
        for (RewardItem r : emptyRewards) {
            AbstractDungeon.combatRewardScreen.rewards.remove(r);
        }
        AbstractDungeon.combatRewardScreen.positionRewards();
    }

    private static float getConvertGoldChance(int initialCounter) {
        if (initialCounter < CONVERT_GOLD_IMPROBABILITY_MINIMUM) {
            return 0.0F;
        } else {
            int translatedInitialCounter = initialCounter + 3;
            return (-7.0F / 10.0F) + ((3.0F * translatedInitialCounter + 30.0F) / (2.0F * translatedInitialCounter + 70.0F));
        }
    }
}
