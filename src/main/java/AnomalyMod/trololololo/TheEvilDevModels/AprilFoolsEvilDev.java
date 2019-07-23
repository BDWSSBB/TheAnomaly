package AnomalyMod.trololololo.TheEvilDevModels;

import AnomalyMod.actions.utility.NoFastModeTalkAction;
import AnomalyMod.actions.utility.NoFastModeWaitAction;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.SpikeStripPower;
import AnomalyMod.powers.ImprobabilityDriveExclusive.enemy.WhiteFlagPower;
import AnomalyMod.trololololo.*;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Pain;
import com.megacrit.cardcrawl.cards.red.Anger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.powers.TimeMazePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.io.IOException;

public class AprilFoolsEvilDev extends AbstractEvilDev {

    /*
    Notes for fight:
    - Innate abilities include:
     - Time Maze
     - Immune to stun
     - Can't die until turn 11 (the kill move)
    - 666x666 move has a train sound with it
    - The actual insta-kill move should do what cracked hourglass does
    - Drop Hubris Potato on death (to make heart fight easier)
     */
    public static final String ID = "anomalyMod:AprilFoolsEvilDev";
    public static final String NAME = "BD, The Evil Dev";
    public static final String[] DIALOG = {
            "Happy April Fools!", // Starting dialog
            "I decided to kick the original boss out of here.", // Turn 1: ??? (nothing) (1)
            "So now you get to fight me today!",
            "Don't worry, I won't barge in anymore after this fight.",
            "Now don't think I'm an easy boss!", // Turn 2: buff (Spike Strip 9) (4)
            "I've got ways of defending myself.",
            "Spike Strip deployed!",
            "I don't like it when you hit me!", // Turn 3, block buff (Block 66, White Flag) (7)
            "White flag, white flag! I'm out!",
            "I guess I should actually attack you.", // Turn 4, attack (12x3) (9)
            "Alright, I'll attack you. Prepare for a triple slap!",
            "I do plan to kill you eventually, but it's a secret.", // Turn 5, block buff (Block 66, Thorns 2) (11)
            "You'll see how on turn 11.",
            "Life is pain. Pain is life.", // Turn 6, mega debuff (Give 12 Spice or 3 Pain into draw) (13)
            "~THE~ ~SPICE~ ~MUST~ ~FLOW.~",
            "Only 4 turns left until I get to kill you!", // Turn 7, block buff (Block 66, Artifact 2) (15)
            "It's gonna be fun! You'll love what I have prepared!",
            "Ehh, I guess I should do some attack every once in a while.", // Turn 8, attack (9x5) (17)
            "More slaps coming!",
            "By the way, I'm not really BD. I'm just a representation.", // Turn 9, block buff (Block 66, Thorns 1) (19)
            "The real BD makes clones of us actually.",
            "You'll probably fight another me after April Fools.",
            "Why don't you have Hubris installed?! I HAVE SO MUCH ANGERY.", // Turn 10, mega debuff (Give Cracked Hourglass or 5 Anger into draw, also loses Obnoxious here) (22)
            "I must give you my ANGERY.",
            "I'm sure ~EVERYONE~ loves this relic.",
            "Oh, it's turn 11. Uh...",
            "Just look at my intent.",
            "Are you ready?", // Turn 11, 666x666 (Queue wait action for cracked hourglass or just run player's die command) (27)
            "!train", // Train sound here
            "Wait... how are you still alive?",
            "I don't know how you are, but you should be punished.", // Runs only if you have cracked hourglass, otherwise, just insta-kill (30)
            "So here goes nothing...",
            "...",
            "Yeah. Nothing. Just like what Sans does.",
            "I can play the waiting game. You can't.",
            "Wait... why am I doing this move?", // Lost move in case Disciple cheese happens, basically the same thing as Turn 11 (35)
            "You didn't pattern shift me or something now, did you?",
            "Well, I can't just give you a free win, so...",
            "#rDIE."
    };
    private static final float HB_X = -8.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 200.0F;
    private static final float HB_H = 260.0F;
    private static final int HP_MAX = 666;
    public static boolean shouldUseSpecialMessage = false;

    public AprilFoolsEvilDev(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, "AnomalyModResources/character/placeholder/simpleAnomalyModelFlipped.png", x, y);
        this.type = EnemyType.BOSS;
        setHp(HP_MAX);
        this.damage.add(new DamageInfo(this, 12));
        this.damage.add(new DamageInfo(this, 9));
        this.damage.add(new DamageInfo(this, 666));
    }

    @Override
    public void usePreBattleAction() {
        shouldUseSpecialMessage = true;
        ConfigHelper.foughtAprilFoolsMe = true;
        ConfigHelper.modConfig.setBool(ConfigHelper.APRIL_FOOLS_KEY, true);
        try {
            ConfigHelper.modConfig.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        ConfigHelper.CustomMusicConfig temp = ConfigHelper.useCustomMusic;
        ConfigHelper.useCustomMusic = ConfigHelper.CustomMusicConfig.ON;
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_BEYOND");
        ConfigHelper.useCustomMusic = temp;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeMazePower(this, 15)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 66)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ImmuneToStunPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ObnoxiousPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GoingEasyPower(this)));
        AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[0], 0.0F, 4.0F));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case (byte) 1: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[1], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[2], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[3], 2.5F, 2.5F));
                break;
            }
            case (byte) 2: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[4], 2.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[5], 2.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[6], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SpikeStripPower(this, 9), 9));
                break;
            }
            case (byte) 3: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[7], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[8], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 66));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WhiteFlagPower(this)));
                break;
            }
            case (byte) 4: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[9], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[10], 2.5F, 2.5F));
                for (int i = 0; i < 3; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case (byte) 5: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[11], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[12], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 66));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 2), 2));
                break;
            }
            case (byte) 6: {
                if (Loader.isModLoaded("hubris")) {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[14], 1.5F, 1.5F));
                    for (int i = 0; i < 12; i++) {
                        AbstractDungeon.actionManager.addToBottom(new SpawnRelicAction("hubris:Spice"));
                    }
                } else {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[13], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Pain(), 3, true, true));
                }
                break;
            }
            case (byte) 7: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[15], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[16], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 66));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 2), 2));
                break;
            }
            case (byte) 8: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[17], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[18], 1.5F, 1.5F));
                for (int i = 0; i < 5; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            }
            case (byte) 9: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[19], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[20], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[21], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 66));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 1), 1));
                break;
            }
            case (byte) 10: {
                if (Loader.isModLoaded("hubris")) {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[24], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new SpawnRelicAction("hubris:CrackedHourglass"));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[22], 2.5F, 2.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[23], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Anger(), 5, true, true));
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
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                }
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[29], 2.5F, 2.5F));
                if (AbstractDungeon.player.hasRelic("hubris:CrackedHourglass")) {
                    AbstractRelic hourglass = AbstractDungeon.player.getRelic("hubris:CrackedHourglass");
                    hourglass.atBattleStart();
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[30], 2.5F, 2.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[31], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(2.0F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[32], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[33], 2.5F, 2.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[34], 2.5F, 2.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeWaitAction(66666.6F));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[37], 2.5F, 2.5F));
                    AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[38], 1.5F, 1.5F));
                    AbstractDungeon.actionManager.addToBottom(new DieDotPeeEnGeeAction());
                }
                break;
            }
            case (byte) 12: {
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[35], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[36], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[37], 2.5F, 2.5F));
                AbstractDungeon.actionManager.addToBottom(new NoFastModeTalkAction(this, DIALOG[38], 1.5F, 1.5F));
                AbstractDungeon.actionManager.addToBottom(new DieDotPeeEnGeeAction());
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
            this.setMove((byte) 4, Intent.ATTACK, this.damage.get(0).base, 3, true);
        } else if (lastMove((byte) 4)) {
            this.setMove((byte) 5, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 5)) {
            this.setMove((byte) 6, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 6)) {
            this.setMove((byte) 7, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 7)) {
            this.setMove((byte) 8, Intent.ATTACK, this.damage.get(1).base, 5, true);
        } else if (lastMove((byte) 8)) {
            this.setMove((byte) 9, Intent.DEFEND_BUFF);
        } else if (lastMove((byte) 9)) {
            this.setMove((byte) 10, Intent.STRONG_DEBUFF);
        } else if (lastMove((byte) 10)) {
            this.setMove((byte) 11, Intent.ATTACK, this.damage.get(2).base, 666, true);
        } else if (lastMove((byte) 11)) {
            this.setMove((byte) 12, Intent.MAGIC);
        } else if (lastMove((byte) 12)) {
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
        shouldUseSpecialMessage = false;
        this.useFastShakeAnimation(5.0f);
        CardCrawlGame.screenShake.rumble(4.0f);
        super.die();
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
    }
}
