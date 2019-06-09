package AnomalyMod.patches.balance;

import AnomalyMod.helpers.blacklistedContent.BlacklistedContentHelper;
import AnomalyMod.patches.enums.PlayerClassEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;

public class BanStuffPatch {

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeCardPools"
    )
    public static class CardPatch {

        public static void Postfix(AbstractDungeon __instance) {
            if (AbstractDungeon.player.chosenClass == PlayerClassEnum.ANOMALY_CLASS) {
                AbstractDungeon.colorlessCardPool.group.removeIf(i -> BlacklistedContentHelper.bannedCards.contains(i.cardID));
            }
        }
    }

    @SpirePatch(
            clz = AbstractDungeon.class,
            method = "initializeRelicList"
    )
    public static class RelicPatch {

        public static void Prefix(AbstractDungeon __instance) {
            if (AbstractDungeon.player.chosenClass == PlayerClassEnum.ANOMALY_CLASS) {
                AbstractDungeon.relicsToRemoveOnStart.addAll(BlacklistedContentHelper.bannedRelics);
            }
        }
    }

    @SpirePatch(
            clz = PotionHelper.class,
            method = "initialize"
    )
    public static class PotionPatch {

        public static void Postfix(AbstractPlayer.PlayerClass chosenClass) {
            if (AbstractDungeon.player.chosenClass == PlayerClassEnum.ANOMALY_CLASS) {
                PotionHelper.potions.removeIf(i -> BlacklistedContentHelper.bannedPotions.contains(i));
            }
        }
    }
}
