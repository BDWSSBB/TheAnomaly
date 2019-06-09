package AnomalyMod.actions.correction;

import AnomalyMod.powers.ImprobabilityDriveExclusive.player.SpringLoadedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SpringLoadedPowerCorrectionAction extends AbstractGameAction {

    private AbstractPlayer player;
    private SpringLoadedPower powerInstance;

    public SpringLoadedPowerCorrectionAction(SpringLoadedPower powerInstance) {
        this.player = AbstractDungeon.player;
        this.powerInstance = powerInstance;
    }

    public void update() {
        if (this.powerInstance.amount == 0) {
            this.powerInstance.shouldDoDoubleDamage = true;
        } else {
            this.powerInstance.shouldDoDoubleDamage = false;
        }
        for (AbstractCard c : this.player.hand.group) {
            c.applyPowers();
        }
        this.isDone = true;
    }
}
