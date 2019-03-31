package AnomalyMod.actions.common.VanillaImprovements;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

// Improved version of attack action for random enemies: Non-recursive, so doesn't add to top at the wrong time and do weird things with thorns and stuff. Still must input baseDamage instead of damage.
public class AttackDamageRandomEnemyAction extends AbstractGameAction {

    private DamageInfo info;
    private AttackEffect effect;

    public AttackDamageRandomEnemyAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.effect = effect;
    }

    public AttackDamageRandomEnemyAction(DamageInfo info) {
        this(info, AttackEffect.NONE);
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            this.info.applyPowers(this.info.owner, this.target);
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.info, this.effect));
        }
        this.isDone = true;
    }
}
