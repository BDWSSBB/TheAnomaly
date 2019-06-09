package AnomalyMod.cards.wistful;

import AnomalyMod.actions.unique.SocketSliceAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.cardPlay.CardPlayHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SocketSlice extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:SocketSlice";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/socketSlice.png";
    private static final int COST = 0;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int ATTACK_DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 2;
    private static final int MAGIC_NUMBER = 2;
    private static final int UPGRADE_PLUS_MAGIC_NUMBER = 1;

    public SocketSlice() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = ATTACK_DAMAGE;
        this.magicNumber = this.baseMagicNumber = MAGIC_NUMBER;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new SocketSliceAction(m, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        int findIt = 0;
        boolean foundIt = false;
        for (int i = CardPlayHelper.cardsActuallyPlayedThisCombat.size() - 1; i >= 0; i--) {
            if (CardPlayHelper.cardsActuallyPlayedThisCombat.get(i).type == AbstractCard.CardType.ATTACK) {
                findIt++;
                if (findIt == 2) {
                    foundIt = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (foundIt) {
            this.rawDescription = DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        } else {
            if (2 - findIt == 1) {
                this.rawDescription = DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[1] + (2 - findIt) + CARD_STRINGS.EXTENDED_DESCRIPTION[2];
            } else {
                this.rawDescription = DESCRIPTION + CARD_STRINGS.EXTENDED_DESCRIPTION[1] + (2 - findIt) + CARD_STRINGS.EXTENDED_DESCRIPTION[3];
            }
        }
        initializeDescription();
        super.applyPowers();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SocketSlice();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.upgradeMagicNumber(UPGRADE_PLUS_MAGIC_NUMBER);
        }
    }
}
