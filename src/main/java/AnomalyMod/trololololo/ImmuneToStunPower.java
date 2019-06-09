package AnomalyMod.trololololo;

import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ImmuneToStunPower extends AbstractAnomalyPower /*implements OnReceivePowerPower*/ {

    public static final String POWER_ID = "anomalyMod:ImmuneToStun";
    public static final String NAME = "Stun Immunity";
    public static final String[] DESCRIPTIONS = {
            "Cannot be #yStunned."
    };

    public ImmuneToStunPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        updateDescription();
        loadRegion("curiosity");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

//    @Override
//    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//        if (power instanceof StunMonsterPower) {
//            flashWithoutSound();
//            return false;
//        }
//        return true;
//    }
}
