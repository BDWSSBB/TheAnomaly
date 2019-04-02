package AnomalyMod.cards.wistful;

import AnomalyMod.actions.unique.DiscontinuityAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.enums.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Discontinuity extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Discontinuity";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/continuityMishap.png";
    private static final int COST = 1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final int MAGIC_NUMBER = 4;

    public Discontinuity() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MAGIC_NUMBER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscontinuityAction(this.magicNumber));
    }

    @Override
    public void applyPowers() {
        String cardName = null;
        for (int i = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1; i >= 0; i--) {
            if (!AbstractDungeon.actionManager.cardsPlayedThisCombat.get(i).dontTriggerOnUseCard) {
                cardName = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(i).name;
                break;
            }
        }
        if (cardName != null) {
            if (this.upgraded) {
                this.rawDescription = UPGRADE_DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + cardName + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            }
            else {
                this.rawDescription = DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + cardName + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
            }
        }
        else {
            if (this.upgraded) {
                this.rawDescription = UPGRADE_DESCRIPTION;
            }
            else {
                this.rawDescription = DESCRIPTION;
            }
        }
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        if (this.upgraded) {
            this.rawDescription = UPGRADE_DESCRIPTION;
        }
        else {
            this.rawDescription = DESCRIPTION;
        }
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Discontinuity();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            AlwaysRetainField.alwaysRetain.set(this, true);
        }
    }
}
