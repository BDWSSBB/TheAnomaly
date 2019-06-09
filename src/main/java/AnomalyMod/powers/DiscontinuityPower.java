package AnomalyMod.powers;

import AnomalyMod.actions.common.PlayCardFromNowhereAction;
import AnomalyMod.patches.correction.PurgeOnUseLaterPatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DiscontinuityPower extends AbstractAnomalyPower implements NonStackablePower {

    public static final String POWER_ID = "anomalyMod:Discontinuity";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static int failsafeCounter = 0;
    private AbstractCard card;

    public DiscontinuityPower(AbstractCreature owner, int amount, AbstractCard card) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.card = card;
        updateDescription();
        loadRegion("split");
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + FontHelper.colorString(this.card.name, "y") + DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + DESCRIPTIONS[3];
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        flashWithoutSound();
        changeAmount(-1);
        if (this.amount == 0 && failsafeCounter > 10) {
            failsafeCounter = 0;
            AbstractDungeon.actionManager.addToBottom(new TalkAction(true, DESCRIPTIONS[4], 0.5F, 2.0F));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else if (this.amount == 0) {
            failsafeCounter++;
            AbstractDungeon.actionManager.addToBottom(new PlayCardFromNowhereAction(this.card.makeStatEquivalentCopy()));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        if (!(card.purgeOnUse || PurgeOnUseLaterPatch.PurgeOnUseLaterField.anomalyModPurgeOnUseLater.get(card))) {
            failsafeCounter = 0;
        }
    }

    private void changeAmount(int amount) {
        this.amount += amount;
        this.updateDescription();
    }
}
