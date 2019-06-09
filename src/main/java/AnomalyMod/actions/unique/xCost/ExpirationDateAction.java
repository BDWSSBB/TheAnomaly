package AnomalyMod.actions.unique.xCost;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import AnomalyMod.powers.ExpirationDatePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class ExpirationDateAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int strengthAndDexBoost;
    private int numberOfTurns;
    private int improbability;
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public ExpirationDateAction(int strengthAndDexBoost, int numberOfTurns, int improbability, boolean freeToPlayOnce, int energyOnUse) {
        this.player = AbstractDungeon.player;
        this.strengthAndDexBoost = strengthAndDexBoost;
        this.numberOfTurns = numberOfTurns;
        this.improbability = improbability;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        int effect = this.improbability;
        if (this.player.hasRelic(ChemicalX.ID)) {
            effect -= 2;
            this.player.getRelic(ChemicalX.ID).flash();
        }
        if (effect < 0) {
            effect = 0;
        }
        int energyToUse = Math.min(effect, this.energyOnUse);
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new ExpirationDatePower(this.player, this.strengthAndDexBoost, this.numberOfTurns), this.strengthAndDexBoost));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new DexterityPower(this.player, this.strengthAndDexBoost), this.strengthAndDexBoost));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new StrengthPower(this.player, this.strengthAndDexBoost), this.strengthAndDexBoost));
        AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(effect - energyToUse));
        if (!this.freeToPlayOnce && energyToUse > 0) {
            this.player.energy.use(energyToUse);
        }
        this.isDone = true;
    }
}

