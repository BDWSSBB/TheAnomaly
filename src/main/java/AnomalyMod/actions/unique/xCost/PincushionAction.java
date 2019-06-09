package AnomalyMod.actions.unique.xCost;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import AnomalyMod.powers.StapledPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;

public class PincushionAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private DamageInfo info;
    private int magicNumber;
    private int secondMagicNumber;
    private int improbability;
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public PincushionAction(AbstractCreature target, DamageInfo info, int magicNumber, int secondMagicNumber, int improbability, boolean freeToPlayOnce, int energyOnUse) {
        this.target = target;
        this.player = AbstractDungeon.player;
        this.info = info;
        this.magicNumber = magicNumber;
        this.secondMagicNumber = secondMagicNumber;
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
        for (int i = 0; i < this.secondMagicNumber; i++) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new StapledPower(this.target, this.magicNumber), this.magicNumber, true));
        }
        for (int i = 0; i < this.secondMagicNumber; i++) {
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
        }
        AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(effect - energyToUse));
        if (!this.freeToPlayOnce && energyToUse > 0) {
            this.player.energy.use(energyToUse);
        }
        this.isDone = true;
    }
}
