package AnomalyMod.cards.wistful;

import AnomalyMod.actions.unique.FilterAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Filter extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Filter";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String JOKE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
    public static final String JOKE_UPGRADE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[1];
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/filter.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int MAGIC_NUMBER = 3;

    public Filter() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // For some reason, the main menu upgrade screen creates new instances of the card per frame. Why are you like this game.
        if (ConfigHelper.useSpecialUpgradeNames && MathUtils.randomBoolean(0.02F) && AbstractDungeon.id != null) {
            this.name = JOKE_NAME;
        }
        this.magicNumber = this.baseMagicNumber = MAGIC_NUMBER;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new FilterAction(this.magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Filter();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            boolean hasJokeName = false;
            if (this.name.equals(JOKE_NAME)) {
                hasJokeName = true;
            }
            this.upgradeName();
            if (hasJokeName) {
                this.name = JOKE_UPGRADE_NAME;
                initializeTitle();
            }
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}

