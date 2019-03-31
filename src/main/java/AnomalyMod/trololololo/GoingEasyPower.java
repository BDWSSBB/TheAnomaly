package AnomalyMod.trololololo;

import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class GoingEasyPower extends AbstractAnomalyPower {

    public static final String POWER_ID = "anomalyMod:GoingEasy";
    public static final String NAME = "Going Easy";
    public static final String[] DESCRIPTIONS = {
            "Receives double damage from #yAttacks."
    };

    public GoingEasyPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        updateDescription();
        loadRegion("vulnerable");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 2.0F;
        }
        else {
            return damage;
        }
    }
}
