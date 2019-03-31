package AnomalyMod.relics;

import AnomalyMod.cards.AbstractAnomalyCard;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MowsetrapCar extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:MowsetrapCar";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private static final int CARDS_TO_ACTIVATE = 4;
    private static final int DRAW_PER_ACTIVATE = 1;

    public MowsetrapCar() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.UNCOMMON, LandingSound.HEAVY);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + CARDS_TO_ACTIVATE + DESCRIPTIONS[1] + DRAW_PER_ACTIVATE + DESCRIPTIONS[2];
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card instanceof AbstractAnomalyCard && ((AbstractAnomalyCard) card).baseImprobabilityNumber > 0) {
            this.counter++;
            if (this.counter % CARDS_TO_ACTIVATE == 0) {
                flash();
                this.counter = 0;
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DrawCardNextTurnPower(AbstractDungeon.player, DRAW_PER_ACTIVATE), DRAW_PER_ACTIVATE));
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MowsetrapCar();
    }
}
