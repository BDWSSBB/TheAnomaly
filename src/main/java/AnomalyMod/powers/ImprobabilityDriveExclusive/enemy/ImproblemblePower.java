package AnomalyMod.powers.ImprobabilityDriveExclusive.enemy;

import AnomalyMod.helpers.interfaces.RandomBuff;
import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ImproblemblePower extends AbstractAnomalyPower implements RandomBuff {

    public static final String POWER_ID = "anomalyMod:Improblemble";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int DAMAGE_PERCENTAGE_PER_AMOUNT = 25;

    public ImproblemblePower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.priority = -99;
        updateDescription();
        loadRegion("sadistic");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DAMAGE_PERCENTAGE_PER_AMOUNT * this.amount + DESCRIPTIONS[1];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * (1 + (float) DAMAGE_PERCENTAGE_PER_AMOUNT * (float) this.amount * 0.01F);
        } else {
            return damage;
        }
    }
}
