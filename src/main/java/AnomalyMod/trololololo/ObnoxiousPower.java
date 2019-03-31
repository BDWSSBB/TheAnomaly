package AnomalyMod.trololololo;

import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ObnoxiousPower extends AbstractAnomalyPower {

    public static final String POWER_ID = "anomalyMod:Obnoxious";
    public static final String NAME = "Obnoxious";
    public static final String[] DESCRIPTIONS = {
            "Cannot die until it is time."
    };

    public ObnoxiousPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        updateDescription();
        loadRegion("minion");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
