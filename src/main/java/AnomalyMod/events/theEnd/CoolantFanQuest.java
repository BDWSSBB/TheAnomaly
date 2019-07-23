package AnomalyMod.events.theEnd;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.blights.ToBeDeleted;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;

public class CoolantFanQuest extends AbstractImageEvent {

    public static final String ID = "anomalyMod:CoolantFanQuest";
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private CurScreen screen;
    private static final int TURN_COST = 7;
    private static final int ASC_TURN_COST = 10;
    private int turnCost;
    private static final int[] IMPROBABILITY_COSTS = new int[]{6, 9, 12, 18};
    private static final int[] ASC_IMPROBABILITY_COSTS = new int[]{8, 12, 16, 24};
    private int improbabilityCost;

    private enum CurScreen {
        INTRO, END
    }

    public CoolantFanQuest() {
        super(EVENT_STRINGS.NAME, DESCRIPTIONS[0], null);

        if (AbstractDungeon.ascensionLevel >= 15) {
            this.turnCost = ASC_TURN_COST;
            this.improbabilityCost = getAscImprobabilityCost(AnomalyModDungeonData.improbabilityOptionsUsed);
        } else {
            this.turnCost = TURN_COST;
            this.improbabilityCost = getImprobabilityCost(AnomalyModDungeonData.improbabilityOptionsUsed);
        }

        this.screen = CurScreen.INTRO;
        this.imageEventText.setDialogOption(OPTIONS[0]);
        if (ConfigHelper.awakeningEndingUnlocked) {
            this.imageEventText.setDialogOption(OPTIONS[1] + this.turnCost + OPTIONS[2]);
            this.imageEventText.setDialogOption(OPTIONS[3] + this.improbabilityCost + OPTIONS[4]);
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO: {
                switch (buttonPressed) {
                    case 0: {
                        AnomalyModDungeonData.coolantFan = AnomalyModDungeonData.QuestComponentQuality.NORMAL;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        this.screen = CurScreen.END;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[5]);
                        break;
                    }
                    case 1: {
                        ToBeDeleted timeCounter = (ToBeDeleted) AbstractDungeon.player.getBlight(ToBeDeleted.ID);
                        if (timeCounter != null) {
                            timeCounter.changeCounter(-this.turnCost);
                        }
                        AnomalyModDungeonData.coolantFan = AnomalyModDungeonData.QuestComponentQuality.PERFECT;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.END;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[5]);
                        break;
                    }
                    case 2: {
                        ImprobabilityDrive.changeImprobability(this.improbabilityCost);
                        AnomalyModDungeonData.improbabilityOptionsUsed++;
                        AnomalyModDungeonData.coolantFan = AnomalyModDungeonData.QuestComponentQuality.PERFECT;
                        this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        this.screen = CurScreen.END;
                        this.imageEventText.clearAllDialogs();
                        this.imageEventText.setDialogOption(OPTIONS[5]);
                        break;
                    }
                }
                break;
            }
            case END: {
                this.openMap();
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[5]);
                break;
            }
        }
    }

    private int getImprobabilityCost(int improbabilityOptionsUsed) {
        if (improbabilityOptionsUsed > IMPROBABILITY_COSTS.length - 1) {
            return IMPROBABILITY_COSTS[IMPROBABILITY_COSTS.length - 1];
        } else if (improbabilityOptionsUsed < 0) {
            AnomalyMod.logger.info("WTF are you doing? Negative number of improbability options selected detected.");
            return IMPROBABILITY_COSTS[0];
        } else {
            return IMPROBABILITY_COSTS[improbabilityOptionsUsed];
        }
    }

    private int getAscImprobabilityCost(int improbabilityOptionsUsed) {
        if (improbabilityOptionsUsed > IMPROBABILITY_COSTS.length - 1) {
            return ASC_IMPROBABILITY_COSTS[IMPROBABILITY_COSTS.length - 1];
        } else if (improbabilityOptionsUsed < 0) {
            AnomalyMod.logger.info("WTF are you doing? Negative number of improbability options selected detected.");
            return ASC_IMPROBABILITY_COSTS[0];
        } else {
            return ASC_IMPROBABILITY_COSTS[improbabilityOptionsUsed];
        }
    }
}
