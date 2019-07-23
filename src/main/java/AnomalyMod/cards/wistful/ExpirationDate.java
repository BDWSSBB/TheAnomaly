package AnomalyMod.cards.wistful;

import AnomalyMod.actions.unique.xCost.ExpirationDateAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.enums.CardColorEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ExpirationDate extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:ExpirationDate";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/xpirationDate.png";
    private static final int COST = -1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int MAGIC_NUMBER = 3;
    private static final int SECOND_MAGIC_NUMBER = 3;
    private static final int UPGRADE_PLUS_SECOND_MAGIC_NUMBER = 1;
    private static final int IMPROBABILITY_NUMBER = 3;

    public ExpirationDate() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.improbabilityNumber = this.baseImprobabilityNumber = IMPROBABILITY_NUMBER;
        this.magicNumber = this.baseMagicNumber = MAGIC_NUMBER;
        this.secondMagicNumber = this.baseSecondMagicNumber = SECOND_MAGIC_NUMBER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ExpirationDateAction(this.magicNumber, this.secondMagicNumber, this.improbabilityNumber, this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ExpirationDate();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_SECOND_MAGIC_NUMBER);
        }
    }
}
