package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.actions.correction.SpringLoadedPowerCorrectionAction;
import AnomalyMod.helpers.interfaces.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class SpringLoadedPower extends AbstractAnomalyPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:SpringLoaded";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int NON_ATTACKS_TO_ACTIVATE = 3;
    public boolean shouldDoDoubleDamage = false;

    public SpringLoadedPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = NON_ATTACKS_TO_ACTIVATE;
        this.priority = 107;
        updateDescription();
        loadRegion("painfulStabs");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + NON_ATTACKS_TO_ACTIVATE + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            if (!this.shouldDoDoubleDamage) {
                this.amount--;
                if (this.amount == 0) {
                    flashWithoutSound();
                }
            }
        } else {
            if (this.shouldDoDoubleDamage) {
                flash();
            }
            this.amount = NON_ATTACKS_TO_ACTIVATE;
        }
        AbstractDungeon.actionManager.addToBottom(new SpringLoadedPowerCorrectionAction(this));
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.shouldDoDoubleDamage) {
            return damage * 2.0F;
        } else {
            return damage;
        }
    }
}
