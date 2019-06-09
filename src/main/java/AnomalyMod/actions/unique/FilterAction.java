package AnomalyMod.actions.unique;

import AnomalyMod.AnomalyMod;
import AnomalyMod.powers.FilterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.ArrayList;

public class FilterAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("anomalyMod:PurgeAction").TEXT;
    private AbstractPlayer player;
    private int improbReductionAmount;
    private ArrayList<AbstractCard> cardsInDraw = new ArrayList<>();
    private ArrayList<AbstractCard> cardsInHand = new ArrayList<>();
    private ArrayList<AbstractCard> cardsInDiscard = new ArrayList<>();

    public FilterAction(int improbReductionAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.player = AbstractDungeon.player;
        this.improbReductionAmount = improbReductionAmount;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.player.drawPile.group) {
                this.cardsInDraw.add(c);
                temp.addToTop(c);
            }
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            for (AbstractCard c : this.player.hand.group) {
                c.angle = 0.0F;
                this.cardsInHand.add(c);
                temp.addToTop(c);
                c.beginGlowing();
            }
            for (AbstractCard c : this.player.discardPile.group) {
                this.cardsInDiscard.add(c);
                temp.addToTop(c);
            }
            if (temp.isEmpty()) {
                this.isDone = true;
                return;
            } else if (temp.size() == 1) {
                for (AbstractCard c : temp.group) {
                    if (this.cardsInDraw.contains(c)) {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                        this.player.drawPile.removeCard(c);
                    } else if (this.cardsInHand.contains(c)) {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                        this.player.hand.removeCard(c);
                    } else if (this.cardsInDiscard.contains(c)) {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                        this.player.discardPile.removeCard(c);
                    } else {
                        AnomalyMod.logger.info("WTF? Where'd it go?!");
                    }
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new FilterPower(this.player, this.improbReductionAmount, c), this.improbReductionAmount));
                }
            } else {
                AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], false);
            }
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (this.cardsInDraw.contains(c)) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                    this.player.drawPile.removeCard(c);
                } else if (this.cardsInHand.contains(c)) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                    this.player.hand.removeCard(c);
                } else if (this.cardsInDiscard.contains(c)) {
                    AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
                    this.player.discardPile.removeCard(c);
                } else {
                    AnomalyMod.logger.info("WTF? Where'd it go?!");
                }
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.player, this.player, new FilterPower(this.player, this.improbReductionAmount, c), this.improbReductionAmount));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.player.hand.refreshHandLayout();
            this.player.hand.glowCheck();
        }
        this.tickDuration();
    }
}
