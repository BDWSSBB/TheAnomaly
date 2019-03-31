package AnomalyMod.actions.correction;

import AnomalyMod.powers.ImprobabilityDriveExclusive.player.ShellLoadedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ShellLoadedPowerCorrectionAction extends AbstractGameAction {

    private AbstractPlayer player;
    private ShellLoadedPower powerInstance;

    public ShellLoadedPowerCorrectionAction(ShellLoadedPower powerInstance) {
        this.player = AbstractDungeon.player;
        this.powerInstance = powerInstance;
    }

    public void update() {
        if (this.powerInstance.amount == 0) {
            this.powerInstance.shouldGainDoubleBlock = true;
        }
        else {
            this.powerInstance.shouldGainDoubleBlock = false;
        }
        for (AbstractCard c : this.player.hand.group) {
            c.applyPowers();
        }
        this.isDone = true;
    }
}
