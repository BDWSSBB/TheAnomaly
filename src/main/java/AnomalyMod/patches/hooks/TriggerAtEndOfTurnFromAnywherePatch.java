package AnomalyMod.patches.hooks;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class TriggerAtEndOfTurnFromAnywherePatch {

    public static void Postfix(GameActionManager __instance) {
        for (final AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractAnomalyCard) {
                ((AbstractAnomalyCard) c).triggerAtEndOfTurnFromAnywhere();
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c instanceof AbstractAnomalyCard) {
                ((AbstractAnomalyCard) c).triggerAtEndOfTurnFromAnywhere();
            }
        }
        for (final AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c instanceof AbstractAnomalyCard) {
                ((AbstractAnomalyCard) c).triggerAtEndOfTurnFromAnywhere();
            }
        }
    }
}
