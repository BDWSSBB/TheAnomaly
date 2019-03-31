package AnomalyMod.cards.wistful;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Blink extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Blink";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String UPGRADE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
    public static final String JOKE_UPGRADE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[1];
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/blink.png";
    private static final int COST = 1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int BLOCK_AMOUNT = 7;
    private static final int UPGRADE_PLUS_BLOCK_AMOUNT = 3;

    public Blink() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = BLOCK_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(p.hand, p.drawPile, AbstractCard -> p.drawPile.getBottomCard() == AbstractCard));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Blink();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (ConfigHelper.useSpecialUpgradeNames) {
                if (MathUtils.randomBoolean(0.002F) && AbstractDungeon.id != null) {
                    this.name = JOKE_UPGRADE_NAME;
                }
                else {
                    this.name = UPGRADE_NAME;
                }
                initializeTitle();
            }
            this.upgradeBlock(UPGRADE_PLUS_BLOCK_AMOUNT);
        }
    }
}
