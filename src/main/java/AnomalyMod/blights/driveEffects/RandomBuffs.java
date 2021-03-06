package AnomalyMod.blights.driveEffects;

import AnomalyMod.AnomalyMod;
import AnomalyMod.blights.AbstractAnomalyBlight;
import AnomalyMod.blights.ImprobabilityDrive;
import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.helpers.improbabilityDriveBuffs.ImprobabilityDriveBuffHelper;
import AnomalyMod.helpers.improbabilityDriveBuffs.enemy.*;
import AnomalyMod.helpers.improbabilityDriveBuffs.player.*;
import AnomalyMod.relics.DeimosCap;
import AnomalyMod.trololololo.TheEvilDevModels.AbstractEvilDev;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RandomBuffs extends AbstractAnomalyBlight {

    public final static String ID = "anomalyMod:RandomBuffs";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/blights/randomBuffs.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/character/placeholder/empty128x128.png";

    public RandomBuffs() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
    }

    @Override
    public void onEquip() {
        changeDescription();
    }

    @Override
    public void updateDescriptionFromImprobabilityChange() {
        changeDescription();
    }

    private void changeDescription() {
        this.counter = (int) (getPowerLevel() * 10.0F);
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        return DESCRIPTION[0] + DESCRIPTION[1] + new DecimalFormat("#.##").format(getPowerLevel()) + DESCRIPTION[2];
    }

    private static float getPowerLevel() { // Never over 9000 unless you're some maniac.
        return getRandomBuffsPowerLevel(ImprobabilityDrive.getImprobability());
    }

    @Override
    public void atBattleStart() {
        int initialCounter = ImprobabilityDrive.getImprobability();
        float powerLevel = getRandomBuffsPowerLevel(initialCounter);
        buffPlayer(powerLevel);
        buffMonsters(powerLevel);
        ImprobabilityDrive.changeImprobability(getRandomBuffsImprobabilityChange(initialCounter));
    }

    private static void buffPlayer(float powerLevel) {
        ArrayList<AbstractImprobabilityDriveBuffModule> buffsToUse = new ArrayList<>();
        buffsToUse.add(new ViscosityBuffModule(AbstractDungeon.player, 4, 1.0F, 0.0F, 2, 0.75F));
        if (powerLevel >= 2.0F && AbstractDungeon.miscRng.randomBoolean()) {
            if (AbstractDungeon.miscRng.randomBoolean()) {
                buffsToUse.add(new BonfireSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
            } else {
                buffsToUse.add(new GravitronSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
            }
        } else {
            buffsToUse.add(new AggressorSyntaxBuffModule(AbstractDungeon.player, 2, 2.0F / 3.0F, 1.5F));
            buffsToUse.add(new DefensorSyntaxBuffModule(AbstractDungeon.player, 2, 2.0F / 3.0F, 1.5F));
        }
        if (powerLevel >= 4.0F) {
            switch (AbstractDungeon.miscRng.random(0, 2)) {
                case 0: {
                    buffsToUse.add(new StarchedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    buffsToUse.add(new SpringLoadedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    break;
                }
                case 1: {
                    buffsToUse.add(new StitchedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    buffsToUse.add(new ShellLoadedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    break;
                }
                case 2: {
                    buffsToUse.add(new PolyrhythmBuffModule(AbstractDungeon.player, 0.5F));
                    break;
                }
                default: {
                    AnomalyMod.logger.info("WARNING: Something wrong happened when applying the unique powers to the player.");
                    break;
                }
            }
        } else if (powerLevel >= 3.0F) {
            switch (AbstractDungeon.miscRng.random(0, 1)) {
                case 0: {
                    buffsToUse.add(new StitchedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    break;
                }
                case 1: {
                    buffsToUse.add(new StarchedBuffModule(AbstractDungeon.player, 1.0F - 1.0F / (float) Math.sqrt(2.0F)));
                    break;
                }
                default: {
                    AnomalyMod.logger.info("WARNING: Something wrong happened when applying the unique powers to the player.");
                    break;
                }
            }
        }
        ImprobabilityDriveBuffHelper.doBuffs(buffsToUse, powerLevel);
    }

    private static void buffMonsters(float powerLevel) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            buffMonster(m, powerLevel);
        }
    }

    public static void buffMonster(AbstractMonster m, float powerLevel) {
        if (m instanceof AbstractEvilDev) {
            return;
        }
        ArrayList<AbstractImprobabilityDriveBuffModule> buffsToUse = new ArrayList<>();
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && m.type == AbstractMonster.EnemyType.BOSS && AbstractDungeon.id.equals(TheBeyond.ID) && !AbstractDungeon.player.hasRelic(DeimosCap.ID)) {
            buffsToUse.add(new AuditorBuffModule(m, 1.0F / 3.0F));
        }
        if (m instanceof CorruptHeart && !AbstractDungeon.player.hasRelic(DeimosCap.ID)) {
            buffsToUse.add(new SpikierStripBuffModule(m, 1.0F / 3.0F));
        }
        if (powerLevel >= 2.0F && ((AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && m.type == AbstractMonster.EnemyType.BOSS) || AbstractDungeon.miscRng.randomBoolean(0.6F))) {
            if (AbstractDungeon.miscRng.randomBoolean()) {
                buffsToUse.add(new RedFlagBuffModule(m, 1.0F / 3.0F));
            } else {
                buffsToUse.add(new WhiteFlagBuffModule(m, 1.0F / 3.0F));
            }
        }
        if (m instanceof Transient) {
            buffsToUse.add(new IncreasedMaxHPBuffModule(m, 0.3F, 1.0F, 0.0F, 0, 0.25F));
        } else {
            buffsToUse.add(new IncreasedMaxHPBuffModule(m, 0.3F, 1.0F, 1.0F, 0, 0.25F));
        }
        buffsToUse.add(new ImproblembleBuffModule(m, 1, 1.0F, 1.0F));
        if (powerLevel >= 3.0F) {
            if (m instanceof CorruptHeart || AbstractDungeon.miscRng.randomBoolean()) {
                buffsToUse.add(new SpikeStripBuffModule(m, 3, 1.0F, 1.0F));
            } else {
                buffsToUse.add(new ProjectorBuffModule(m, 2, 1.0F, 1.0F));
            }
        }
        ImprobabilityDriveBuffHelper.doBuffs(buffsToUse, powerLevel);
    }

    public static float getRandomBuffsPowerLevel(int initialCounter) {
        return (float) Math.pow((float) initialCounter / 40, 2) + (float) initialCounter / 20 + 1.0F;
    }

    private static int getRandomBuffsImprobabilityChange(int initialCounter) {
        return -initialCounter / 10;
    }
}
