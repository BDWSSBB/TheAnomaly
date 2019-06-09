package AnomalyMod.cards.wistful;

import AnomalyMod.actions.unique.EveryCardIsDecoyToYouAction;
import AnomalyMod.actions.unique.NegativeReceptionAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.enums.CardColorEnum;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class NegativeReception extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:NegativeReception";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/negativeReception.png";
    private static final int COST = 1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int ATTACK_DAMAGE = 8;
    private static final int UPGRADE_PLUS_DAMAGE = 4;

    private boolean isStupidVersion = false;

    public NegativeReception() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = ATTACK_DAMAGE;
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (this.isStupidVersion) {
            AbstractDungeon.actionManager.addToBottom(new EveryCardIsDecoyToYouAction());
        } else {
            AbstractDungeon.actionManager.addToBottom(new NegativeReceptionAction());
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NegativeReception();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (CardCrawlGame.playerName.equals("gameshark")) {
                this.rawDescription = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
                initializeDescription();
                this.isStupidVersion = true;
            } else {
                this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            }
        }
    }
}
