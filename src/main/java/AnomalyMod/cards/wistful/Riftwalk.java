package AnomalyMod.cards.wistful;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class Riftwalk extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Riftwalk";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String UPGRADE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/riftwalk.png";
    private static final int COST = 2;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int IMPROBABILITY_NUMBER = 3;
    private static final int BLOCK_AMOUNT = 5;
    private static final int MAGIC_NUMBER = 3;
    private static final int UPGRADE_PLUS_MAGIC_NUMBER = 1;
    private static final int SECOND_MAGIC_NUMBER = 3;
    private static final int UPGRADE_PLUS_SECOND_MAGIC_NUMBER = 1;

    public Riftwalk() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.improbabilityNumber = this.baseImprobabilityNumber = IMPROBABILITY_NUMBER;
        this.block = this.baseBlock = BLOCK_AMOUNT;
        this.magicNumber = this.baseMagicNumber = MAGIC_NUMBER;
        this.secondMagicNumber = this.baseSecondMagicNumber = SECOND_MAGIC_NUMBER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ChangeImprobabilityAction(this.improbabilityNumber));
        for (int i = 0; i < this.magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, this.secondMagicNumber, false), this.secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Riftwalk();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (ConfigHelper.useSpecialUpgradeNames) {
                this.name = UPGRADE_NAME;
                initializeTitle();
            }
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
            this.upgradeSecondMagicNumber(UPGRADE_PLUS_SECOND_MAGIC_NUMBER);
        }
    }
}
