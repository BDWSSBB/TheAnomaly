package AnomalyMod.powers;

import AnomalyMod.AnomalyMod;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DialMForMurderPower extends AbstractAnomalyTwoAmountPower {

    public static final String POWER_ID = "anomalyMod:DialMForMurder";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;

    public DialMForMurderPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount;
        updateDescription();
        loadRegion("sadistic");
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void stackPower(int amount) {
        super.stackPower(amount);
        this.amount2 += amount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && (card.costForTurn >= 2 || (card.costForTurn == -1 && card.energyOnUse >= 2)) && !card.purgeOnUse && this.amount2 > 0) {
            flash();
            this.amount2--;
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0f;
            tmp.freeToPlayOnce = true;
            tmp.purgeOnUse = true;
            tmp.applyPowers();
            if (!AbstractDungeon.actionManager.cardQueue.isEmpty()) {
                AbstractDungeon.actionManager.cardQueue.add(1, new CardQueueItem(tmp, m, card.energyOnUse/*, true*/));
            } else {
                AnomalyMod.logger.info("Why is cardQueue empty for Dial M For Murder?");
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse/*, true*/));
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        this.amount2 = this.amount;
    }
}
