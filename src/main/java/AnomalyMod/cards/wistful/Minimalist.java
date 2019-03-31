package AnomalyMod.cards.wistful;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.enums.CardColorEnum;
import AnomalyMod.powers.MinimalistPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Minimalist extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Minimalist";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/minimalist.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Minimalist() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new MinimalistPower(p, 1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Minimalist();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
        }
    }
}
