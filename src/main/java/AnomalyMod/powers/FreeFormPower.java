package AnomalyMod.powers;

import AnomalyMod.actions.correction.AddActionLaterAction;
import AnomalyMod.actions.unique.UseFreeFormAction;
import AnomalyMod.actions.correction.HandApplyPowersAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FreeFormPower extends AbstractAnomalyPower {

    public static final String POWER_ID = "anomalyMod:FreeForm";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public FreeFormPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        updateDescription();
        loadRegion("shift");
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        }
        else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurnPostDraw() {
        for (int i = 0; i < this.amount; i++) {
            AbstractDungeon.actionManager.addToBottom(new UseFreeFormAction());
        }
        // There's some weirdness with the hand not updating properly, so this is here to do a check afterwards.
        AbstractDungeon.actionManager.addToBottom(new AddActionLaterAction(new HandApplyPowersAction(), 1));
    }
}

