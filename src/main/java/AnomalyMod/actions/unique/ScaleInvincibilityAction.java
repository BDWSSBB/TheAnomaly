package AnomalyMod.actions.unique;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.InvinciblePower;

public class ScaleInvincibilityAction extends AbstractGameAction {

    private static final float SCALE_PERCENT = 0.2F;
    private static final float ASC2_SCALE_PERCENT = 0.15F;
    private AbstractCreature probablyTheHeart;

    public ScaleInvincibilityAction(AbstractCreature probablyTheHeart) {
        this.probablyTheHeart = probablyTheHeart;
    }

    public void update() {
        InvinciblePower power = (InvinciblePower) this.probablyTheHeart.getPower(InvinciblePower.POWER_ID);
        if (power != null) {
            int currentMax = (int) ReflectionHacks.getPrivate(power, InvinciblePower.class, "maxAmt");
            int difference;
            if (AbstractDungeon.ascensionLevel >= 19) {
                difference = (MathUtils.ceil(ASC2_SCALE_PERCENT * probablyTheHeart.maxHealth)) - currentMax;
            } else {
                difference = (MathUtils.ceil(SCALE_PERCENT * probablyTheHeart.maxHealth)) - currentMax;
            }

            if (difference > 0) {
                ReflectionHacks.setPrivate(power, InvinciblePower.class, "maxAmt", currentMax + difference);
                power.amount += difference;
                power.updateDescription();
            }
        }
        this.isDone = true;
    }
}
