package AnomalyMod.trololololo;

import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.relics.Ratchet;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

public class AragoltsBane extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:AragoltsBane";
    public static final String NAME = "Aragolt's Bane";
    public static final String[] DESCRIPTION = {
            "Upon pickup, obtain #b2 #yRatchets. At the start of your turn, draw #b2 additional cards."
    };
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";

    public AragoltsBane() {
        super(ID, NAME, DESCRIPTION[0], IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, RelicLibrary.getRelic(Ratchet.ID).makeCopy());
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, RelicLibrary.getRelic(Ratchet.ID).makeCopy());
        AbstractDungeon.player.masterHandSize += 2;
    }
}
