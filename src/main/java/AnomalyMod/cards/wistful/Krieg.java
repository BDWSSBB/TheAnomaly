package AnomalyMod.cards.wistful;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.patches.enums.CardColorEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Krieg extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Krieg";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/krieg.png";
    private static final int COST = 1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int IMPROBABILITY_NUMBER = 1;
    private static final int ATTACK_DAMAGE = 3;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int DAMAGE_DIFFERENCE = 6;
    private static final int UPGRADE_PLUS_DAMAGE_DIFFERENCE = 2;

    public Krieg() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.improbabilityNumber = this.baseImprobabilityNumber = IMPROBABILITY_NUMBER;
        this.damage = this.baseDamage = ATTACK_DAMAGE;
        this.magicNumber = this.baseMagicNumber = ATTACK_DAMAGE + DAMAGE_DIFFERENCE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ChangeImprobabilityAction(this.improbabilityNumber));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(0.15F));
        }
        this.baseDamage += DAMAGE_DIFFERENCE + this.timesUpgraded * UPGRADE_PLUS_DAMAGE_DIFFERENCE;
        this.isMultiDamage = true;
        this.calculateCardDamage(null);
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.baseDamage -= DAMAGE_DIFFERENCE + this.timesUpgraded * UPGRADE_PLUS_DAMAGE_DIFFERENCE;
        this.isMultiDamage = false;
    }

    @Override
    public void applyPowers() {
        this.baseDamage += DAMAGE_DIFFERENCE + this.timesUpgraded * UPGRADE_PLUS_DAMAGE_DIFFERENCE;
        this.baseMagicNumber = this.baseDamage;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        this.baseDamage -= DAMAGE_DIFFERENCE + this.timesUpgraded * UPGRADE_PLUS_DAMAGE_DIFFERENCE;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Krieg();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.baseMagicNumber = this.baseDamage + DAMAGE_DIFFERENCE + this.timesUpgraded * UPGRADE_PLUS_DAMAGE_DIFFERENCE;
            this.upgradedMagicNumber = this.upgradedDamage;
        }
    }
}
