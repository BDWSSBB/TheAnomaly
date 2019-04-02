package AnomalyMod.actions.unique;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class ClusterCrunchAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private int damage;

    public ClusterCrunchAction(AbstractCreature target, int damage) {
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.target = target;
        this.player = AbstractDungeon.player;
        this.damage = damage;
    }

    public void update() {
        if (this.target == null && AbstractDungeon.getMonsters().monsters.size() >= 1) {
            ArrayList<AbstractMonster> leastHpMonster = new ArrayList<>();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && (leastHpMonster.isEmpty() || m.currentHealth < leastHpMonster.get(0).currentHealth)) {
                    leastHpMonster.clear();
                    leastHpMonster.add(m);
                }
                else if (!leastHpMonster.isEmpty() && m.currentHealth == leastHpMonster.get(0).currentHealth) {
                    leastHpMonster.add(m);
                }
            }
            if (leastHpMonster.isEmpty()) {
                AnomalyMod.logger.info("Why's there no target for Cluster Crunch?");
            }
            else if (leastHpMonster.size() == 1) {
                this.target = leastHpMonster.get(0);
            }
            else {
                this.target = leastHpMonster.get(AbstractDungeon.cardRandomRng.random(0, leastHpMonster.size() - 1));
            }
        }
        if (this.duration == this.startDuration && this.target != null) {
            DamageInfo info = new DamageInfo(this.player, this.damage, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this.player, this.target);
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
            this.target.damage(info);
            // I have no idea what the hell is wrong with Reptomancer, so this will have to do.
            if ((this.target.isDying || this.target.currentHealth <= 0) && !(this.target instanceof Reptomancer)) {
                AbstractDungeon.actionManager.addToTop(new ClusterCrunchAction(null, this.damage));
                AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(0.1F));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.tickDuration();
    }
}
