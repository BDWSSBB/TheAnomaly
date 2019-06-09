package AnomalyMod.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class WaitTillIntentsLoadAction extends AbstractGameAction {

    private int softlockCounter = 400;

    public WaitTillIntentsLoadAction() {
    }

    @Override
    public void update() {
        boolean intentsLoaded = true;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m.intentAlphaTarget == 1f && m.intentAlpha < 0.6F) {
                intentsLoaded = false;
            }
        }
        if (intentsLoaded || this.softlockCounter < 0) {
            this.isDone = true;
        }
        this.softlockCounter--;
    }
}
