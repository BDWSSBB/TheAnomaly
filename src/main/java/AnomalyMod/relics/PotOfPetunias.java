package AnomalyMod.relics;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PotOfPetunias extends AbstractAnomalyRelic implements CustomSavable<CardSave> {

    public static final String ID = "anomalyMod:PotOfPetunias";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private AbstractCard lastCardPlayed = null;

    public PotOfPetunias() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart() {
        if (this.lastCardPlayed != null) {
            AbstractCard cardToAdd = this.lastCardPlayed.makeStatEquivalentCopy();
            if (cardToAdd.cost > 0) {
                cardToAdd.cost = 0;
                cardToAdd.costForTurn = 0;
            }
            cardToAdd.isCostModified = true;
            cardToAdd.exhaust = true;
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(cardToAdd));
            // Note: There is a patch to make sure the exhaust field carries properly in MakeStatEquivalentCopy.
        }
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action){
        this.lastCardPlayed = card;
    }

    public CardSave onSave() {
        if (this.lastCardPlayed != null) {
            return new CardSave(this.lastCardPlayed.cardID, this.lastCardPlayed.timesUpgraded, this.lastCardPlayed.misc);
        }
        else {
            return null;
        }
    }

    public void onLoad(CardSave lastCardPlayedData) {
        if (lastCardPlayedData != null) {
            this.lastCardPlayed = CardLibrary.getCopy(lastCardPlayedData.id, lastCardPlayedData.upgrades, lastCardPlayedData.misc);
        }
    }

    public AbstractRelic makeCopy() {
        return new PotOfPetunias();
    }
}
