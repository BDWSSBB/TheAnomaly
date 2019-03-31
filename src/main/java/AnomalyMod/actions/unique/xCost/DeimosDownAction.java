package AnomalyMod.actions.unique.xCost;

import AnomalyMod.actions.common.ChangeImprobabilityAction;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import java.util.ArrayList;

public class DeimosDownAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int[] multiDamage;
    private int improbability;
    private boolean freeToPlayOnce;
    private int energyOnUse;

    public DeimosDownAction(int[] multiDamage, int improbability, boolean freeToPlayOnce, int energyOnUse) {
        this.player = AbstractDungeon.player;
        this.multiDamage = multiDamage;
        this.improbability = improbability;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
    }

    public void update() {
        int effect = this.improbability;
        if (this.player.hasRelic(ChemicalX.ID)) {
            effect -= 2;
            this.player.getRelic(ChemicalX.ID).flash();
        }
        if (effect < 0) {
            effect = 0;
        }
        int energyToUse = Math.min(effect, this.energyOnUse);
        AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(this.player, this.multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
        AbstractMonster monsterVFXTarget = null;
        if (!AbstractDungeon.getMonsters().monsters.isEmpty()) {
            ArrayList<AbstractMonster> aliveMonsters = new ArrayList<>();
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped()) {
                    aliveMonsters.add(m);
                }
            }
            if (!aliveMonsters.isEmpty()) {
                monsterVFXTarget = AbstractDungeon.getMonsters().monsters.get(MathUtils.random(0, aliveMonsters.size() - 1));
            }
        }
        if (monsterVFXTarget != null) {
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.8F));
            AbstractDungeon.actionManager.addToTop(new VFXAction(new WeightyImpactEffect(monsterVFXTarget.hb.cX, monsterVFXTarget.hb.cY)));
        }
        AbstractDungeon.actionManager.addToTop(new ChangeImprobabilityAction(effect - energyToUse));
        if (!this.freeToPlayOnce && energyToUse > 0) {
            this.player.energy.use(energyToUse);
        }
        this.isDone = true;
    }
}
