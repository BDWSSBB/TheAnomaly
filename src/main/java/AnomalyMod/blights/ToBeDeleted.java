package AnomalyMod.blights;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;

public class ToBeDeleted extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:ToBeDeleted";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/blights/pocketwatch.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/blights/outline/pocketwatch.png";
    public static final int TURNS_TILL_DEATH = 125;
    private static final int TURNS_PER_TRAVEL = 3;

    public ToBeDeleted() {
        super(ID, NAME, DESCRIPTION[0], IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        this.counter = TURNS_TILL_DEATH;
        changeDescription();
    }

    private void changeDescription() {
        if (this.counter == 1) {
            this.description = DESCRIPTION[1] + TURNS_PER_TRAVEL + DESCRIPTION[3];
        } else {
            this.description = DESCRIPTION[1] + this.counter + DESCRIPTION[2] + TURNS_PER_TRAVEL + DESCRIPTION[3];
        }
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        changeCounter(-TURNS_PER_TRAVEL);
    }

    @Override
    public void atTurnStart() {
        changeCounter(-1);
    }

    public void changeCounter(int amount) {
        this.counter += amount;
        changeDescription();
        if (this.counter <= 0) {
            AbstractDungeon.player.isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        }
    }
}
