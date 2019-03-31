package AnomalyMod.powers.FixedVanillaPowers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.InvinciblePower;

public class FixedInvinciblePower extends InvinciblePower {

    public FixedInvinciblePower(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    @Override
    public float atDamageFinalReceive(float damage, final DamageInfo.DamageType type) {
        return damage;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        return damageAmount;
    }

    public int whyMustIBabysitYouWithYourOwnOnAttackedMethod(DamageInfo info, int damageAmount) {
        return super.onAttacked(info, damageAmount);
    }
}
