package AnomalyMod.actions.unique;

import AnomalyMod.actions.utility.NoFastModeWaitAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

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
        if (this.duration == this.startDuration && this.target != null) {
            DamageInfo info = new DamageInfo(this.player, this.damage, DamageInfo.DamageType.NORMAL);
            info.applyPowers(this.player, this.target);
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
            this.target.damage(info);
            // I have no idea what the hell is wrong with Reptomancer, so this will have to do.
            if ((this.target.isDying || this.target.currentHealth <= 0) && !(this.target instanceof Reptomancer)) {
                AbstractDungeon.actionManager.addToTop(new ClusterCrunchAction(AbstractDungeon.getMonsters().getRandomMonster(true), this.damage));
                AbstractDungeon.actionManager.addToTop(new NoFastModeWaitAction(0.1F));
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        this.tickDuration();
    }
}
