package AnomalyMod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractAnomalyCard extends CustomCard {

    public int baseSecondMagicNumber;
    public int secondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradedSecondMagicNumber;
    public int baseImprobabilityNumber;
    public int improbabilityNumber;
    public boolean isImprobabilityNumberModified;
    public boolean upgradedImprobabilityNumber;

    public AbstractAnomalyCard(
            String id,
            String name,
            String imagePath,
            int cost,
            String rawDescription,
            AbstractCard.CardType type,
            AbstractCard.CardColor color,
            AbstractCard.CardRarity rarity,
            AbstractCard.CardTarget target) {
        super(id, name, imagePath, cost, rawDescription, type, color, rarity, target);
    }

    public void upgradeSecondMagicNumber(int amount) {
        this.baseSecondMagicNumber += amount;
        this.secondMagicNumber = this.baseSecondMagicNumber;
        this.upgradedSecondMagicNumber = true;
    }

    public void upgradeImprobabilityNumber(int amount, boolean floorZero) {
        this.changeImprobabilityNumber(amount, floorZero);
        this.improbabilityNumber = this.baseImprobabilityNumber;
        this.upgradedImprobabilityNumber = true;
    }

    public void upgradeImprobabilityNumber(int amount) {
        this.upgradeImprobabilityNumber(amount, true);
    }

    public void changeImprobabilityNumber(int amount, boolean floorZero) {
        this.baseImprobabilityNumber += amount;
        if (floorZero && this.baseImprobabilityNumber < 0) {
            this.baseImprobabilityNumber = 0;
        }
    }

    public void triggerAtEndOfTurnFromAnywhere() {

    }

    public float atDamageGiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        return power.atDamageGive(tmp, damageTypeForTurn);
    }

    public float atDamageReceiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        return power.atDamageReceive(tmp, damageTypeForTurn);
    }

    public float atDamageFinalGiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        return power.atDamageFinalGive(tmp, damageTypeForTurn);
    }

    public float atDamageFinalReceiveWithPower(float tmp, DamageInfo.DamageType damageTypeForTurn, AbstractPower power) {
        return power.atDamageFinalReceive(tmp, damageTypeForTurn);
    }

    public float modifyBlockWithPower(float tmp, AbstractPower power) {
        return power.modifyBlock(tmp);
    }

    public static class SecondMagicNumber extends DynamicVariable {

        @Override
        public String key() {
            return "anomalyMod:M2";
        }

        @Override
        public int baseValue(AbstractCard card) {
            return ((AbstractAnomalyCard) card).baseSecondMagicNumber;
        }

        @Override
        public int value(AbstractCard card) {
            return ((AbstractAnomalyCard) card).secondMagicNumber;
        }

        @Override
        public boolean isModified(AbstractCard card) {
            return ((AbstractAnomalyCard) card).isSecondMagicNumberModified;
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            return ((AbstractAnomalyCard) card).upgradedSecondMagicNumber;
        }
    }

    public static class ImprobabilityNumber extends DynamicVariable {

        @Override
        public String key() {
            return "anomalyMod:I";
        }

        @Override
        public int baseValue(AbstractCard card) {
            return ((AbstractAnomalyCard) card).baseImprobabilityNumber;
        }

        @Override
        public int value(AbstractCard card) {
            return ((AbstractAnomalyCard) card).improbabilityNumber;
        }

        @Override
        public boolean isModified(AbstractCard card) {
            return ((AbstractAnomalyCard) card).isImprobabilityNumberModified;
        }

        @Override
        public boolean upgraded(AbstractCard card) {
            return ((AbstractAnomalyCard) card).upgradedImprobabilityNumber;
        }
    }
}
