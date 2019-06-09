package AnomalyMod.actions.unique;

import AnomalyMod.actions.common.PlayCardFromNowhereAction;
import AnomalyMod.patches.hardcodes.FreeFormPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class UseFreeFormAction extends AbstractGameAction {

    private boolean retrieveCard;

    public UseFreeFormAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.retrieveCard = false;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            FreeFormPatch.doingFreeFormSelection = true;
            AbstractDungeon.cardRewardScreen.discoveryOpen();
            FreeFormPatch.doingFreeFormSelection = false;
            this.tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractDungeon.actionManager.addToBottom(new PlayCardFromNowhereAction(AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy()));
            }
            this.retrieveCard = true;
        }
        this.tickDuration();
    }
}
