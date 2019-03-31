package AnomalyMod.cards.wistful;

import AnomalyMod.actions.common.VanillaImprovements.BetterDiscardPileToHandAction;
import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Hook extends AbstractAnomalyCard {

    public static final String ID = "anomalyMod:Hook";
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = CARD_STRINGS.NAME;
    public static final String JOKE_UPGRADE_NAME = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
    public static final String IMAGE_PATH = "AnomalyModResources/cards/wistful/hook.png";
    private static final int COST = 1;
    public static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = CARD_STRINGS.UPGRADE_DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.ANOMALY_WISTFUL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final int ATTACK_DAMAGE = 6;
    private static final int UPGRADE_PLUS_ATTACK_DAMAGE = 2;
    private static final int EXHAUSTIVE_AMOUNT = 2;

    public Hook() {
        super(ID, NAME, IMAGE_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = ATTACK_DAMAGE;
        ExhaustiveVariable.setBaseValue(this, EXHAUSTIVE_AMOUNT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(new BetterDiscardPileToHandAction(1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hook();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            if (ConfigHelper.useSpecialUpgradeNames && MathUtils.randomBoolean(0.02F) && AbstractDungeon.id != null) {
                this.name = JOKE_UPGRADE_NAME;
                initializeTitle();
            }
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            this.upgradeDamage(UPGRADE_PLUS_ATTACK_DAMAGE);
            ExhaustiveVariable.setBaseValue(this, -1);
        }
    }
}
