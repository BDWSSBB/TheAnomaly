package AnomalyMod.trololololo.TheEvilDevModels;

import AnomalyMod.actions.utility.NoFastModeTalkAction;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.cards.wistful.DeimosDown;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.SpikierStripPower;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.WhiteFlagPower;
import AnomalyMod.trololololo.ImmuneToStunPower;
import AnomalyMod.trololololo.ObnoxiousPower;
import AnomalyMod.trololololo.SpawnRelicAction;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.TimeMazePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class AragoltEvilDevV2 extends AbstractEvilDev {

    /*
    Notes for fight:
    - Innate abilities include:
     - Time Maze
     - Converts buffs to "debuffs" (except Lazy Mountain)
    - 666x666 move has a train sound with it
    - The actual insta-kill move should do what cracked hourglass does
    - Drop Hubris Potato on death (to make heart fight easier)
     */
    public static final String ID = "anomalyMod:AragoltEvilDevV2";
    public static final String NAME = "BD, The Evil Dev";
    public static final String[] DIALOG = {
            "Oh, you're back.", // Turn 1: ??? (nothing)
            "I'm sure you had fun fighting me last time.",
            ""
    };
    private static final float HB_X = 8.0F;
    private static final float HB_Y = 136.0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private static final int HP_MAX = 666;

    public AragoltEvilDevV2(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, "AnomalyModResources/trololololo/bigPlaceholder4.png", x, y);
        this.type = EnemyType.BOSS;
        setHp(HP_MAX);
        this.damage.add(new DamageInfo(this, 666));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 66)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeMazePower(this, 15)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ImmuneToStunPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ObnoxiousPower(this)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case (byte) 1: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[0], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[1], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[2], 3.0F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("JEVIL-ANYTHING"));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(2.0F));
                break;
            }
            case (byte) 2: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[3], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[4], 2.5F, 2.5F));
                ImprobabilityDrive drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
                if (drive != null) {
                    //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SpikeStripPower(this, (int) (drive.getRandomBuffsPowerLevel(drive.counter) * 2)), (int) (drive.getRandomBuffsPowerLevel(drive.counter) * 2)));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SpikierStripPower(this)));
                break;
            }
            case (byte) 3: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[5], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[6], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 50));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WhiteFlagPower(this)));
                break;
            }
            case (byte) 4: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[7], 2.5F, 2.5F));
                int emptySpace = BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size();
                if (emptySpace > 6) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new DeimosDown(), 6));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new DeimosDown(), emptySpace));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new DeimosDown(), 6 - emptySpace, false, true));
                }
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[8], 1.0F, 2.5F));
                break;
            }
            case (byte) 5: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[9], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[10], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[11], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 50));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 1), 1));
                break;
            }
            case (byte) 6: {
                if (Loader.isModLoaded("hubris")) {
                    for (int i = 0; i < 9; i++) {
                        AbstractDungeon.actionManager.addToBottom(new SpawnRelicAction("hubris:Spice"));
                    }
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[13], 1.0F, 2.5F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[12], 2.5F, 2.5F));
                }
                break;
            }
            case (byte) 7: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[14], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[15], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 50));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
                break;
            }
            case (byte) 8: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[16], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[17], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[18], 2.5F, 2.5F));
                ArrayList<AbstractCard> cursesToAdd = new ArrayList<>();
                for (int i = 0; i < 50 && cursesToAdd.size() < 4; i++) {
                    AbstractCard c = AbstractDungeon.returnRandomCurse();
                    // LOVE THIS HARDCODE
                    if (!(c instanceof Clumsy || c instanceof Decay || c instanceof Doubt || c instanceof Injury || c instanceof Parasite || c instanceof Regret || c instanceof Shame || c instanceof Writhe)) {
                        cursesToAdd.add(c);
                    }
                }
                for (AbstractCard c : cursesToAdd) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, true));
                }
                break;
            }
            case (byte) 9: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[19], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[20], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[21], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 50));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 1), 1));
                break;
            }
            case (byte) 10: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[22], 2.5F, 2.5F));
                if (Loader.isModLoaded("hubris")) {
                    AbstractDungeon.actionManager.addToBottom(new SpawnRelicAction("hubris:CrackedHourglass"));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[24], 1.5F, 1.5F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[23], 2.5F, 2.5F));
                }
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[25], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[26], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, ObnoxiousPower.POWER_ID));
                break;
            }
            case (byte) 11: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[27], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[28], 1.0F, 1.0F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("JEVIL-BYEBYE"));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(1.0F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("CHOO-CHOO"));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(1.0F));
                for (int i = 0; i < 666; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, 666, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                break;
            }
            case (byte) 12: {
                if (AbstractDungeon.player.hasRelic("hubris:CrackedHourglass")) {
                    AbstractRelic hourglass = AbstractDungeon.player.getRelic("hubris:CrackedHourglass");
                    hourglass.atBattleStart();
                }
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[29], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[30], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[31], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(2.0F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[32], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[33], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[34], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[35], 1.0F, 1.0F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(66666.6F));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void getMove(int num) {
        if (lastMove((byte) 1)) {
            this.setMove((byte) 2, Intent.BUFF);
        } else if (lastMove((byte) 2)) {
            this.setMove((byte) 3, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 3)) {
            this.setMove((byte) 4, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 4)) {
            this.setMove((byte) 5, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 5)) {
            this.setMove((byte) 6, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 6)) {
            this.setMove((byte) 7, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 7)) {
            this.setMove((byte) 8, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 8)) {
            this.setMove((byte) 9, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 9)) {
            this.setMove((byte) 10, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 10)) {
            this.setMove((byte) 11, Intent.ATTACK, 666, 666, true);
        } else if (lastMove((byte) 11)) {
            this.setMove((byte) 12, Intent.MAGIC);
        } else {
            this.setMove((byte) 1, Intent.UNKNOWN);
        }
    }

    @Override
    public void die() {
        if (this.hasPower(ObnoxiousPower.POWER_ID)) {
            this.currentHealth = 0;
            this.heal(1);
            return;
        }
        if (Loader.isModLoaded("hubris")) {
            if (!AbstractDungeon.player.hasRelic("hubris:Potato")) {
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F, RelicLibrary.getRelic("hubris:Potato").makeCopy());
            }
        }
        this.useFastShakeAnimation(5.0f);
        CardCrawlGame.screenShake.rumble(4.0f);
        super.die();
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
    }
}
