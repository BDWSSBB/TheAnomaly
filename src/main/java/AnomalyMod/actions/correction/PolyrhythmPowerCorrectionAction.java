package AnomalyMod.actions.correction;

import AnomalyMod.powers.ImprobabilityDriveExclusive.player.PolyrhythmPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// I have to make this as a workaround around bad stuff happening like Sword Boomerang not working with it.
public class PolyrhythmPowerCorrectionAction extends AbstractGameAction {

    private AbstractPlayer player;
    private PolyrhythmPower powerInstance;

    public PolyrhythmPowerCorrectionAction(PolyrhythmPower powerInstance) {
        this.player = AbstractDungeon.player;
        this.powerInstance = powerInstance;
    }

    public void update() {
        if (this.powerInstance.amount == 1) {
            this.powerInstance.shouldDoDoubleDamage = true;
        }
        else {
            this.powerInstance.shouldDoDoubleDamage = false;
        }
        if (this.powerInstance.amount2 == 1) {
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
