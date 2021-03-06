package AnomalyMod.blights.driveEffects;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.text.DecimalFormat;

public class Specialty extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:Specialty";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/blights/specialty.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/blights/outline/specialty.png";
    public static final int SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM = 20;

    public Specialty() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
    }

    @Override
    public void onEquip() {
        changeDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        changeDescription();
    }

    @Override
    public void onActuallyGainImprobability(int improbabilityGained) {
        changeDescription();
    }

    private void changeDescription() {
        this.counter = (int) (getSingleCardChoiceChance(ImprobabilityDrive.getImprobability()) * 100.0F);
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        return DESCRIPTION[0] + DESCRIPTION[1] + new DecimalFormat("#.#").format(getPercent()) + DESCRIPTION[2];
    }

    private static float getPercent() {
        return getSingleCardChoiceChance(ImprobabilityDrive.getImprobability()) * 100.0F;
    }

    public static void specializeCardRewards() {
        int initialCounter = ImprobabilityDrive.getImprobability();

        // I just use cardRng for this stuff to avoid the mess of save and load problems with my own RNG.
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.CARD && r.cards.size() > 1 && initialCounter >= SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM && AbstractDungeon.cardRng.randomBoolean(getSingleCardChoiceChance(initialCounter))) {
                while (r.cards.size() > 1) {
                    // Because people just usually add cards to the end, we gotta remove at random.
                    r.cards.remove(AbstractDungeon.cardRng.random(0, r.cards.size() - 1));
                }
                r.cards.get(0).upgrade();
            }
        }
    }

    private static float getSingleCardChoiceChance(int initialCounter) {
        if (initialCounter < SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM) {
            return 0.0F;
        } else {
            return (-6.0F / 5.0F) + ((2.0F * initialCounter) / ((float) initialCounter + 10.0F));
        }
    }
}
