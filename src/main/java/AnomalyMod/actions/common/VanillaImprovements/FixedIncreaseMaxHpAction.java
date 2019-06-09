package AnomalyMod.actions.common.VanillaImprovements;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FixedIncreaseMaxHpAction extends IncreaseMaxHpAction {

    private float increasePercent;

    public FixedIncreaseMaxHpAction(AbstractMonster m, float increasePercent, boolean showEffect) {
        super(m, increasePercent, showEffect);
        this.increasePercent = increasePercent;
    }

    @Override
    public void update() {
        if (MathUtils.round((float) this.target.maxHealth * this.increasePercent) > 0) {
            super.update();
        } else {
            this.isDone = true;
        }
    }
}
