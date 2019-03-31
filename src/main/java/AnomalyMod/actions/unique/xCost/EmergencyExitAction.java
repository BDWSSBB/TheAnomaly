package AnomalyMod.actions.unique.xCost;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class EmergencyExitAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int block;
    private int improbability;
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public EmergencyExitAction(int block, int improbability, boolean freeToPlayOnce, int energyOnUse) {
        this.player = AbstractDungeon.player;
        this.block = block;
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
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.player, this.player, this.block));
        AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(effect - energyToUse));
        if (!this.freeToPlayOnce && energyToUse > 0) {
            this.player.energy.use(energyToUse);
        }
        this.isDone = true;
    }
}
