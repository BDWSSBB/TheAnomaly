package AnomalyMod.actions.common;

import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.blights.driveEffects.RandomBuffs;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Lethality;
import com.megacrit.cardcrawl.daily.mods.TimeDilation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SlideMonsterInAction extends AbstractGameAction {

    private AbstractMonster monster;

    public SlideMonsterInAction(AbstractMonster monster) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_LONG;
        this.monster = monster;

        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onSpawnMonster(this.monster);
        }

        if (AbstractDungeon.player.hasBlight(RandomBuffs.ID)) {
            RandomBuffs.buffMonster(monster, RandomBuffs.getRandomBuffsPowerLevel(ImprobabilityDrive.getImprobability()));
        }
    }

    private int getSmartPosition() {
        int position = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (this.monster.drawX > mo.drawX) {
                position++;
            }
        }
        return position;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            this.monster.animX = 4800f * Settings.scale;
            this.monster.init();
            this.monster.applyPowers();
            AbstractDungeon.getCurrRoom().monsters.addMonster(getSmartPosition(), this.monster);

            if (ModHelper.isModEnabled(Lethality.ID)) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(this.monster, this.monster, new StrengthPower(this.monster, Lethality.STR_AMT), Lethality.STR_AMT));
            }

            if (ModHelper.isModEnabled(TimeDilation.ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.monster, this.monster, new SlowPower(this.monster, 0)));
            }
        }

        tickDuration();

        if (isDone) {
            this.monster.animX = 0f;
            this.monster.showHealthBar();
            this.monster.usePreBattleAction();
        } else {
            this.monster.animX = Interpolation.fade.apply(0f, 4800f * Settings.scale, duration);
        }
    }
}
