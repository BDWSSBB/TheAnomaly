package AnomalyMod.cards.wistful;

import AnomalyMod.cards.AbstractAnomalyCard;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class DeepDitch extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:DeepDitch";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/deepDitch.png";
    private static final int COST = 2;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int ATTACK_DAMAGE = 16;
    private static final int UPGRADE_PLUS_DAMAGE = 6;

    public DeepDitch() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = ATTACK_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public float atDamageGiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        if (power.ID.equals(StrengthPower.POWER_ID)) {
            return power.atDamageGive(power.atDamageGive(tmp, damageTypeForTurn), damageTypeForTurn);
        }
        else {
            return super.atDamageGiveWithPower(tmp, damageTypeForTurn, power);
        }
    }

    @Override
    public float atDamageReceiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        if (power.ID.equals(VulnerablePower.POWER_ID)) {
            return power.atDamageReceive(power.atDamageReceive(tmp, damageTypeForTurn), damageTypeForTurn);
        }
        else {
            return super.atDamageReceiveWithPower(tmp, damageTypeForTurn, power);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeepDitch();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
        }
    }
}
