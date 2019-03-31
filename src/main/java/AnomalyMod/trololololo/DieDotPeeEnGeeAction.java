package AnomalyMod.trololololo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DieDotPeeEnGeeAction extends AbstractGameAction {

    public DieDotPeeEnGeeAction() {
    }

    public void update() {
        // Too lazy, just got hubris's action
        AbstractPlayer p = AbstractDungeon.player;
        p.powers.removeIf(power -> "hubris:Potato".equals(power.ID));
        p.currentHealth = 0;
        p.healthBarUpdatedEvent();
        p.damage(new DamageInfo(p, 1, DamageInfo.DamageType.HP_LOSS));
        this.isDone = true;
    }
}
