package AnomalyMod.cards.colorless;

import AnomalyMod.actions.common.ExhaustFromAnywhereAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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
        } else if (cardType == CardType.POWER) {
            this.loadCardImage(POWER_IMAGE_PATH);
            this.textureImg = POWER_IMAGE_PATH;
        }
        if (cardType != CardType.POWER) {
            this.exhaust = true;
        } else {
            this.rawDescription = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        }
    }

    public DummyCard() {
        this(CardType.SKILL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void triggerAtEndOfTurnFromAnywhere() {
        AbstractDungeon.actionManager.addToBottom(new ExhaustFromAnywhereAction(this));
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
