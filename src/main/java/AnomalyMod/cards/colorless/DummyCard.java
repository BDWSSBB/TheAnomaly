package AnomalyMod.cards.colorless;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.common.VanillaImprovements.FasterExhaustSpecificCardAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EvolvePower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import com.megacrit.cardcrawl.relics.BlueCandle;

public class DummyCard extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:DummyCard";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String ATTACK_IMAGE_PATH = "AnomalyModResources/cards/colorless/dummyAttack.png";
    public static final String SKILL_IMAGE_PATH = "AnomalyModResources/cards/colorless/dummySkill.png";
    public static final String POWER_IMAGE_PATH = "AnomalyModResources/cards/colorless/dummyPower.png";
    private static final int COST = 0;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;

    public DummyCard(CardType cardType) {
        super(ID, NAME, SKILL_IMAGE_PATH, COST, DESCRIPTION, cardType, COLOR, RARITY, TARGET);
        if (cardType == CardType.ATTACK) {
            this.loadCardImage(ATTACK_IMAGE_PATH);
            this.textureImg = ATTACK_IMAGE_PATH;
        }
        else if (cardType == CardType.POWER) {
            this.loadCardImage(POWER_IMAGE_PATH);
            this.textureImg = POWER_IMAGE_PATH;
        }
        this.isEthereal = true;
        this.exhaust = true;
    }

    public DummyCard() {
        this(CardType.SKILL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.type == CardType.CURSE) {
            if (p.hasRelic(BlueCandle.ID)) {
                useBlueCandle(p);
            }
            else {
                AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return true;
    }

    @Override
    public void triggerWhenDrawn() {
        if (this.type == CardType.STATUS && AbstractDungeon.player.hasPower(EvolvePower.POWER_ID) && !AbstractDungeon.player.hasPower(NoDrawPower.POWER_ID)) {
            AbstractDungeon.player.getPower(EvolvePower.POWER_ID).flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
        }
    }

    @Override
    public void triggerAtEndOfTurnFromAnywhere() {
        if (AbstractDungeon.player.drawPile.contains(this)) {
            AbstractDungeon.actionManager.addToTop(new FasterExhaustSpecificCardAction(this, AbstractDungeon.player.drawPile));
        }
        else if (AbstractDungeon.player.hand.contains(this)) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }
        else if (AbstractDungeon.player.discardPile.contains(this)) {
            AbstractDungeon.actionManager.addToTop(new FasterExhaustSpecificCardAction(this, AbstractDungeon.player.discardPile));
        }
        else {
            AnomalyMod.logger.info("WARNING: Couldn't find Dummy to exhaust.");
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DummyCard();
    }

    @Override
    public void upgrade() {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }
}
