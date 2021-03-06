package AnomalyMod.relics;

import AnomalyMod.powers.StapledPower;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class Megachette extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:Megachette";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private static final int MINIMUM_DAMAGE_TO_ACTIVATE = 24;
    private static final int GAPING_PER_ACTIVATE = 2;
    private ArrayList<AbstractCreature> enemiesHitAlready = new ArrayList<>();
    private boolean usedThisTurn = false;

    public Megachette() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.RARE, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MINIMUM_DAMAGE_TO_ACTIVATE + DESCRIPTIONS[1] + GAPING_PER_ACTIVATE + DESCRIPTIONS[2];
    }

    @Override
    public void atPreBattle() {
        this.pulse = true;
        beginPulse();
    }

    @Override
    public void atTurnStart() {
        this.usedThisTurn = false;
        this.enemiesHitAlready.clear();
        this.pulse = true;
        beginPulse();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && !this.enemiesHitAlready.isEmpty()) {
            this.usedThisTurn = true;
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.owner.equals(AbstractDungeon.player) && info.type == DamageInfo.DamageType.NORMAL && damageAmount >= MINIMUM_DAMAGE_TO_ACTIVATE && !this.enemiesHitAlready.contains(target) && !this.usedThisTurn) {
            this.pulse = false;
            this.enemiesHitAlready.add(target);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new StapledPower(target, GAPING_PER_ACTIVATE), GAPING_PER_ACTIVATE));
        }
    }

    @Override
    public void onVictory() {
        this.pulse = false;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Megachette();
    }
}
