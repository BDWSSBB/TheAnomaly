package AnomalyMod.actions.common;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class SetImprobabilityNumberAction extends AbstractGameAction {

    private UUID uuid;

    public SetImprobabilityNumberAction(UUID targetUUID, int amount) {
        setValues(this.target, this.source, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update() {
        for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
            if (c instanceof AbstractAnomalyCard) {
                ((AbstractAnomalyCard) c).improbabilityNumber = this.amount;
                ((AbstractAnomalyCard) c).baseImprobabilityNumber = this.amount;
                ((AbstractAnomalyCard) c).isImprobabilityNumberModified = false;
            }
        }
        this.isDone = true;
    }
}
