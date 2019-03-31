package AnomalyMod.relics;

import AnomalyMod.cards.colorless.DummyCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DummyFile extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:DummyFile";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private boolean usedThisTurn = false;

    public DummyFile() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atPreBattle() {
        beginPulse();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!this.usedThisTurn) {
            flash();
            this.usedThisTurn = true;
            this.pulse = false;
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new DummyCard(card.type)));
        }
    }

    @Override
    public void atTurnStart() {
        this.usedThisTurn = false;
        this.pulse = true;
    }

    @Override
    public void onVictory() {
        stopPulse();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DummyFile();
    }
}
