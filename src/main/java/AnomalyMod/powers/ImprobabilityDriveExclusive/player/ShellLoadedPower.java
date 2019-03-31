package AnomalyMod.powers.ImprobabilityDriveExclusive.player;

import AnomalyMod.actions.correction.ShellLoadedPowerCorrectionAction;
import AnomalyMod.powers.AbstractAnomalyPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ShellLoadedPower extends AbstractAnomalyPower {

    public static final String POWER_ID = "anomalyMod:ShellLoaded";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private static final int NON_SKILLS_TO_ACTIVATE = 3;
    public boolean shouldGainDoubleBlock = false;

    public ShellLoadedPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = NON_SKILLS_TO_ACTIVATE;
        this.priority = 107;
        updateDescription();
        loadRegion("closeUp");
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + NON_SKILLS_TO_ACTIVATE + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.SKILL) {
            if (!this.shouldGainDoubleBlock) {
                this.amount--;
                if (this.amount == 0) {
                    flashWithoutSound();
                }
            }
        }
        else {
            if (this.shouldGainDoubleBlock) {
                flash();
            }
            this.amount = NON_SKILLS_TO_ACTIVATE;
        }
        AbstractDungeon.actionManager.addToBottom(new ShellLoadedPowerCorrectionAction(this));
    }

    @Override
    public final float modifyBlock(float block) {
        if (this.shouldGainDoubleBlock) {
            return block * 2.0F;
        }
        else {
            return block;
        }
    }
}
