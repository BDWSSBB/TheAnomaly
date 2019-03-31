package AnomalyMod.trololololo;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SpawnRelicAction extends AbstractGameAction {

    private String relicID;

    public SpawnRelicAction(String relicID) {
        this.relicID = relicID;
    }

    public SpawnRelicAction(AbstractRelic relic) {
        this.relicID = relic.relicId;
    }

    public void update() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, RelicLibrary.getRelic(this.relicID).makeCopy());
        this.isDone = true;
    }
}
