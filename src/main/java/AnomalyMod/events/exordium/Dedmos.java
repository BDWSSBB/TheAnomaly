package AnomalyMod.events.exordium;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.relics.DeimosCap;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;

public class Dedmos extends AbstractImageEvent {

    public static final String ID = "anomalyMod:Dedmos";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private CurScreen screen;

    private enum CurScreen {
        INTRO, END
    }

    public Dedmos() {
        super(EVENT_STRINGS.NAME, DESCRIPTIONS[0], "AnomalyModResources/events/exordium/dedmos.png");
        this.screen = CurScreen.INTRO;
        this.imageEventText.setDialogOption(OPTIONS[0]);
        this.imageEventText.setDialogOption(OPTIONS[1]);
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO: {
                switch (buttonPressed) {
                    case 0: {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, RelicLibrary.getRelic(DeimosCap.ID).makeCopy());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.END;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[1]);
                        break;
                    }
                    case 1: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.END;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[1]);
                        break;
                    }
                }
                break;
            }
            case END: {
                this.openMap();
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1]);
                break;
            }
        }
    }

    public static boolean canSpawn() {
        if (AbstractDungeon.player instanceof AnomalyCharacter) {
            return true;
        } else {
            return false;
        }
    }
}
