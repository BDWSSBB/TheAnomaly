package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.helpers.interfaces.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.PlayerTurnEffect;

public class StitchedPower extends AbstractAnomalyPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:Stitched";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int ATTACKS_PER_PLAY = 6;

    public StitchedPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = ATTACKS_PER_PLAY;
        updateDescription();
        loadRegion("thousandCuts");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + PlayerTurnEffect.getOrdinalNaming(ATTACKS_PER_PLAY) + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.amount--;
            if (this.amount == 0) {
                flash();
                this.amount = ATTACKS_PER_PLAY;
            }
        }
    }
}
