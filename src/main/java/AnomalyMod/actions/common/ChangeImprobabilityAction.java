package AnomalyMod.actions.common;

import AnomalyMod.blights.ImprobabilityDrive;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ChangeImprobabilityAction extends AbstractGameAction {

    private AbstractPlayer player;
    private int improbabilityNumber;

    public ChangeImprobabilityAction(int improbabilityNumber) {
        this.player = AbstractDungeon.player;
        this.improbabilityNumber = improbabilityNumber;
    }

    public void update() {
        if (this.player.hasBlight(ImprobabilityDrive.ID)) {
            ((ImprobabilityDrive) this.player.getBlight(ImprobabilityDrive.ID)).changeImprobability(this.improbabilityNumber);
        }
        else {
            ImprobabilityDrive drive = new ImprobabilityDrive();
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, drive);
            drive.checkForSpawnInfoBlights();
            drive.changeImprobability(this.improbabilityNumber);
        }
        this.isDone = true;
    }
}
