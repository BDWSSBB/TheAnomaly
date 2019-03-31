package AnomalyMod.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class MaladaptationAction extends AbstractGameAction {

    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString("DiscardAction").TEXT;
    private AbstractMonster target;
    private AbstractPlayer player;
    private int debuffAmount;

    public MaladaptationAction(AbstractMonster target, int debuffAmount) {
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.target = target;
        this.player = AbstractDungeon.player;
        this.debuffAmount = debuffAmount;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (this.player.hand.isEmpty()) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new WeakPower(this.target, this.debuffAmount, false), this.debuffAmount));
            }
            else if (this.player.hand.size() == 1) {
                AbstractCard tempCard = this.player.hand.getBottomCard();
                if (tempCard.type == AbstractCard.CardType.SKILL) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new VulnerablePower(this.target, this.debuffAmount, false), this.debuffAmount));
                }
                else {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new WeakPower(this.target, this.debuffAmount, false), this.debuffAmount));
                }
                this.player.hand.moveToDiscardPile(tempCard);
                tempCard.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
            }
            else {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                this.player.hand.moveToDiscardPile(c);
                c.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                if (c.type == AbstractCard.CardType.SKILL) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new VulnerablePower(this.target, this.debuffAmount, false), this.debuffAmount));
                }
                else {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.player, new WeakPower(this.target, this.debuffAmount, false), this.debuffAmount));
                }
            }
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            this.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
}
