package AnomalyMod.blights;

import AnomalyMod.AnomalyMod;
import AnomalyMod.actions.unique.PortableTerminalAction;
import AnomalyMod.blights.improbabilityDriveInfo.*;
import AnomalyMod.cards.status.BadSector;
import AnomalyMod.helpers.WaitForIntentsToLoad;
import AnomalyMod.helpers.improbabilityDriveBuffs.AbstractImprobabilityDriveBuffModule;
import AnomalyMod.helpers.improbabilityDriveBuffs.ImprobabilityDriveBuffHelper;
import AnomalyMod.helpers.improbabilityDriveBuffs.enemy.*;
import AnomalyMod.helpers.improbabilityDriveBuffs.player.*;
import AnomalyMod.patches.improbabilityDriveEffects.ImprobabilityDriveFutureInvadersPatch;
import AnomalyMod.powers.*;
import AnomalyMod.relics.AbstractAnomalyRelic;
import AnomalyMod.relics.DeimosCap;
import AnomalyMod.trololololo.TheEvilDevModels.AbstractEvilDev;
import AnomalyMod.vfx.ObtainBlightLater;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.BlightStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Transient;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class ImprobabilityDrive extends AbstractAnomalyBlight implements WaitForIntentsToLoad {

    public final static String ID = "anomalyMod:ImprobabilityDrive";
    private static final BlightStrings BLIGHT_STRINGS = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = BLIGHT_STRINGS.NAME;
    public static final String[] DESCRIPTION = BLIGHT_STRINGS.DESCRIPTION;
    public static final String IMAGE_PATH = "AnomalyModResources/relics/placeholder.png";
    public static final String IMAGE_OUTLINE_PATH = "AnomalyModResources/relics/outline/placeholderOutline.png";
    private static final int CONVERT_GOLD_IMPROBABILITY_MINIMUM = 12;
    public int cardRewardStrength = 0;
    public boolean convertingGoldToCard = false;
    private static final int SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM = 20;
    public static final int SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM = 40;
    public static final int SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM = 50;
    public static final int SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM = 70;
    public static final int SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM = 80;
    private boolean invadingRoomWithFuture = false;
    private boolean shouldBuffNonFutureInvaders = false;

    public ImprobabilityDrive() {
        super(ID, NAME, getDescription(), IMAGE_PATH, IMAGE_OUTLINE_PATH, true);
        this.counter = 0;
        checkForSpawnInfoBlights();
        changeDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        changeDescription();
    }

    private void changeDescription() {
        this.description = getDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private static String getDescription() {
        String toEdit = "";
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveTryNewThingsInfo.ID, CONVERT_GOLD_IMPROBABILITY_MINIMUM);
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveSpecialtyInfo.ID, SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM);
        toEdit = upcomingInfoBlightCheck(toEdit, ImprobabilityDriveSurpriseElitesInfo.ID, SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM);
        return DESCRIPTION[0] + toEdit;
    }

    private static String upcomingInfoBlightCheck(String toEdit, String blightID, int effectMinimum) {
        if (toEdit.equals("") && !AbstractDungeon.player.hasBlight(blightID)) {
            return DESCRIPTION[1] + effectMinimum + DESCRIPTION[2] + BlightHelper.getBlight(blightID).name;
        }
        return toEdit;
    }

    public void changeImprobability(int improbabilityNumber) {
        int calculatedImprobabilityNumber = (int) doOnGainImprobabilityStageOne((float) improbabilityNumber);
        if (calculatedImprobabilityNumber != 0) {
            int finalImprobabilityNumber = doAfterFinalImprobabilityCalculations(calculatedImprobabilityNumber);
            if (finalImprobabilityNumber != 0) {
                this.counter += finalImprobabilityNumber;
                flash();
                doOnActuallyGainImprobability(finalImprobabilityNumber);
            }
        }
        if (this.counter < 0) {
            this.counter = 0;
        }
        checkForSpawnInfoBlights();
        changeDescription();
    }

    private float doOnGainImprobabilityStageOne(float calculatingImprobabilityNumber) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                calculatingImprobabilityNumber = ((AbstractAnomalyRelic) r).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                calculatingImprobabilityNumber = ((AbstractAnomalyPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                calculatingImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        calculatingImprobabilityNumber = ((AbstractAnomalyPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        calculatingImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                calculatingImprobabilityNumber = ((AbstractAnomalyBlight) b).onGainImprobabilityStageOne(calculatingImprobabilityNumber);
            }
        }
        return calculatingImprobabilityNumber;
    }

    private int doAfterFinalImprobabilityCalculations (int finalImprobabilityNumber) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                finalImprobabilityNumber = ((AbstractAnomalyRelic) r).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                finalImprobabilityNumber = ((AbstractAnomalyPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                finalImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        finalImprobabilityNumber = ((AbstractAnomalyPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        finalImprobabilityNumber = ((AbstractAnomalyTwoAmountPower) p).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                finalImprobabilityNumber = ((AbstractAnomalyBlight) b).afterFinalImprobabilityCalculations(finalImprobabilityNumber);
            }
        }
        return finalImprobabilityNumber;
    }

    private void doOnActuallyGainImprobability(int improbabilityGained) {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AbstractAnomalyRelic) {
                ((AbstractAnomalyRelic) r).onActuallyGainImprobability(improbabilityGained);
            }
        }
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof AbstractAnomalyPower) {
                ((AbstractAnomalyPower) p).onActuallyGainImprobability(improbabilityGained);
            }
            if (p instanceof AbstractAnomalyTwoAmountPower) {
                ((AbstractAnomalyTwoAmountPower) p).onActuallyGainImprobability(improbabilityGained);
            }
        }
        if (AbstractDungeon.getMonsters() != null && AbstractDungeon.getMonsters().monsters != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                for (AbstractPower p : m.powers) {
                    if (p instanceof AbstractAnomalyPower) {
                        ((AbstractAnomalyPower) p).onActuallyGainImprobability(improbabilityGained);
                    }
                    if (p instanceof AbstractAnomalyTwoAmountPower) {
                        ((AbstractAnomalyTwoAmountPower) p).onActuallyGainImprobability(improbabilityGained);
                    }
                }
            }
        }
        for (AbstractBlight b : AbstractDungeon.player.blights) {
            if (b instanceof AbstractAnomalyBlight) {
                ((AbstractAnomalyBlight) b).onActuallyGainImprobability(improbabilityGained);
            }
        }
    }

    public void checkForSpawnInfoBlights() {
        spawnInfoBlightCheck(ImprobabilityDrivePortableTerminalInfo.ID, 0, true);
        spawnInfoBlightCheck(ImprobabilityDriveMalfunctionInfo.ID, 0, !Settings.isEndless);
        spawnInfoBlightCheck(ImprobabilityDriveRandomBuffsInfo.ID, 0, true);
        spawnInfoBlightCheck(ImprobabilityDriveTryNewThingsInfo.ID, CONVERT_GOLD_IMPROBABILITY_MINIMUM, true);
        spawnInfoBlightCheck(ImprobabilityDriveSpecialtyInfo.ID, SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM, true);
        spawnInfoBlightCheck(ImprobabilityDriveSurpriseElitesInfo.ID, SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM, true);
        if (Settings.isEndless && AbstractDungeon.player.hasBlight(ImprobabilityDriveMalfunctionInfo.ID)) { // BLAAAAAANK
            AbstractDungeon.player.blights.remove(AbstractDungeon.player.getBlight(ImprobabilityDrive.ID));
        }
    }

    private void spawnInfoBlightCheck(String blightID, int effectMinimum, boolean extraRequirements) {
        if (!AbstractDungeon.player.hasBlight(blightID) && this.counter >= effectMinimum && extraRequirements) {
            AbstractDungeon.effectsQueue.add(new ObtainBlightLater(BlightHelper.getBlight(blightID), true));
        }
    }

    @Override
    public void atBattleStart() {
        int initialCounter = this.counter;
        float powerLevel = this.getRandomBuffsPowerLevel(initialCounter);
        buffPlayer(powerLevel);
        buffMonsters(powerLevel);
        changeImprobability(getRandomBuffsImprobabilityChange(initialCounter));
        if (this.shouldBuffNonFutureInvaders) {
            this.shouldBuffNonFutureInvaders = false;
        }
        if (this.invadingRoomWithFuture) {
            this.invadingRoomWithFuture = false;
            ImprobabilityDriveFutureInvadersPatch.FutureInvadersField.anomalyModInvadedByFuture.set(AbstractDungeon.getCurrRoom(), true);
        }
    }

    @Override
    public void atStartOfTurnPostPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new PortableTerminalAction(ImprobabilityDrivePortableTerminalInfo.IMPROBABILITY_LOSS));
    }

    private void buffPlayer(float powerLevel) {
        ArrayList<AbstractImprobabilityDriveBuffModule> buffsToUse = new ArrayList<>();
        buffsToUse.add(new ViscocityBuffModule(AbstractDungeon.player, 6, 1.0F, 0.0F, 2, 0.5F));
        if (powerLevel >= 2.0F && AbstractDungeon.miscRng.randomBoolean()) {
            buffsToUse.add(new BonfireSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
            buffsToUse.add(new ImitatorSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
        }
        else {
            buffsToUse.add(new AggressorSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
            buffsToUse.add(new DefensorSyntaxBuffModule(AbstractDungeon.player, 2, 1.0F, 1.0F));
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
        }
        else if (powerLevel >= 3.0F) {
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

    private void buffMonsters(float powerLevel) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m instanceof AbstractEvilDev) {
                continue;
            }
            ArrayList<AbstractImprobabilityDriveBuffModule> buffsToUse = new ArrayList<>();
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && m.type == AbstractMonster.EnemyType.BOSS && AbstractDungeon.id.equals(TheBeyond.ID) && !AbstractDungeon.player.hasRelic(DeimosCap.ID)) {
                buffsToUse.add(new AuditorBuffModule(m, 1.0F / 3.0F));
            }
            if (m instanceof CorruptHeart) {
                buffsToUse.add(new SpikierStripBuffModule(m, 1.0F / 3.0F));
            }
            if (powerLevel >= 2.0F && ((AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss && m.type == AbstractMonster.EnemyType.BOSS) || AbstractDungeon.miscRng.randomBoolean(0.6F))) {
                if (AbstractDungeon.miscRng.randomBoolean()) {
                    buffsToUse.add(new RedFlagBuffModule(m, 1.0F / 3.0F));
                }
                else {
                    buffsToUse.add(new WhiteFlagBuffModule(m, 1.0F / 3.0F));
                }
            }
            if (m instanceof Transient) {
                buffsToUse.add(new IncreasedMaxHPBuffModule(m, 1.0F / 3.0F, 1.0F, 0.0F, 0, 1.0F / 3.0F));
            }
            else {
                buffsToUse.add(new IncreasedMaxHPBuffModule(m, 1.0F / 3.0F, 1.0F, 1.0F, 0, 1.0F / 3.0F));
            }
            buffsToUse.add(new ImproblembleBuffModule(m, 1, 1.0F, 1.0F));
            if (powerLevel >= 3.0F) {
                if (m instanceof CorruptHeart || AbstractDungeon.miscRng.randomBoolean()) {
                    buffsToUse.add(new SpikeStripBuffModule(m, 3, 1.0F, 1.0F));
                }
                else {
                    buffsToUse.add(new ProjectorBuffModule(m, 2, 1.0F, 1.0F));
                }
            }
            ImprobabilityDriveBuffHelper.doBuffs(buffsToUse, powerLevel);
        }
    }

    @Override
    public void onBossDefeat() {
        if (!Settings.isEndless) {
            this.flash();
            AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new BadSector().makeCopy(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        }
    }

    public void iLoveYouKio() { // I feel like this is the rng at fault
        int initialCounter = this.counter;
        if (ImprobabilityDriveFutureInvadersPatch.FutureInvadersField.anomalyModInvadedByFuture.get(AbstractDungeon.getCurrRoom())) {
            AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
        }
        if (initialCounter >= CONVERT_GOLD_IMPROBABILITY_MINIMUM && AnomalyMod.anomalyRNG.randomBoolean(getConvertGoldChance(initialCounter))) {
            this.cardRewardStrength = 0;
            ArrayList<RewardItem> rewardsToRemove = new ArrayList<>();
            for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
                if (r.type == RewardItem.RewardType.GOLD || r.type == RewardItem.RewardType.STOLEN_GOLD) {
                    rewardsToRemove.add(r);
                }
            }
            for (RewardItem r : rewardsToRemove) {
                AbstractDungeon.combatRewardScreen.rewards.remove(r);
                this.cardRewardStrength += r.goldAmt;
            }
            if (this.cardRewardStrength > 0) {
                this.convertingGoldToCard = true;
                AbstractDungeon.combatRewardScreen.rewards.add(new RewardItem());
                this.convertingGoldToCard = false;
            }
        }
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.CARD && r.cards.size() > 1 && initialCounter >= SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM && AnomalyMod.anomalyRNG.randomBoolean(getSingleCardChoiceChance(initialCounter))) {
                while (r.cards.size() > 1) {
                    r.cards.remove(r.cards.size() - 1);
                }
                r.cards.get(0).upgrade();
            }
        }
        ArrayList<RewardItem> emptyRewards = new ArrayList<>(); // Sometimes there are empty card rewards (e.g. Busted Crown), so this should do another check.
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.CARD && r.cards.size() == 0) {
                emptyRewards.add(r);
            }
        }
        for (RewardItem r : emptyRewards) {
            AbstractDungeon.combatRewardScreen.rewards.remove(r);
        }
        AbstractDungeon.combatRewardScreen.positionRewards();
    }

    public void rollSurpriseElite() {
        int initialCounter = this.counter;
        if (isValidRoomForSurpriseElite(initialCounter) && AnomalyMod.anomalyRNG.randomBoolean(getSurpriseElitesChance(initialCounter))) {
            changeImprobability(getSurpriseElitesImprobabilityChange(initialCounter));
            AbstractDungeon.nextRoom.room = new MonsterRoomElite();
        }
    }

    private boolean isValidRoomForSurpriseElite(int initialCounter) {
        // The following format is just for readability's sake.
        boolean validRoom = false;
        if (AbstractDungeon.nextRoom == null) {
            AnomalyMod.logger.info("WTF? Why is AbstractDungeon.nextRoom null?");
            return false;
        }
        if (AbstractDungeon.nextRoom.room instanceof EventRoom && initialCounter >= SURPRISE_ELITES_UNKNOWN_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room.getClass().equals(MonsterRoom.class) && initialCounter >= SURPRISE_ELITES_MONSTER_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room instanceof ShopRoom && initialCounter >= SURPRISE_ELITES_SHOP_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.nextRoom.room instanceof RestRoom && initialCounter >= SURPRISE_ELITES_REST_ROOM_IMPROBABILITY_MINIMUM) {
            validRoom = true;
        }
        if (AbstractDungeon.id == null || AbstractDungeon.id.equals(TheEnding.ID)) {
            validRoom = false;
        }
        if (AbstractDungeon.currMapNode == null || AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            validRoom = false;
        }
        return validRoom;
    }

    // DEPRECATED
    public MonsterGroup rollFutureInvader(MonsterGroup oldGroup) {
//        int initialCounter = this.counter;
//        if (initialCounter >= FUTURE_INVADERS_IMPROBABILITY_MINIMUM && AnomalyMod.anomalyRNG.randomBoolean(getFutureInvadersChance(initialCounter))) {
//            String oldMonsterEncounter = AbstractDungeon.monsterList.get(0);
//            FutureInvadersEncounterCategory category = FutureInvadersEncounterCategory.NORMAL;
//
//            // Do you know how much I hate the following hardcode.
//            // TODO: Tidy this up
//            ArrayList<String> exordiumWeakEncounters = new ArrayList<>();
//            List<MonsterInfo> customExordiumWeakEncounters = BaseMod.getMonsterEncounters(Exordium.ID);
//            ArrayList<String> exordiumNormalEncounters = new ArrayList<>();
//            List<MonsterInfo> customExordiumNormalEncounters = BaseMod.getMonsterEncounters(Exordium.ID);
//            ArrayList<String> cityWeakEncounters = new ArrayList<>();
//            List<MonsterInfo> customCityWeakEncounters = BaseMod.getMonsterEncounters(TheCity.ID);
//            ArrayList<String> cityNormalEncounters = new ArrayList<>();
//            List<MonsterInfo> customCityNormalEncounters = BaseMod.getMonsterEncounters(TheCity.ID);
//            ArrayList<String> beyondWeakEncounters = new ArrayList<>();
//            List<MonsterInfo> customBeyondWeakEncounters = BaseMod.getMonsterEncounters(TheBeyond.ID);
//            ArrayList<String> beyondNormalEncounters = new ArrayList<>();
//            List<MonsterInfo> customBeyondNormalEncounters = BaseMod.getMonsterEncounters(TheBeyond.ID);
//
//            exordiumWeakEncounters.add(MonsterHelper.CULTIST_ENC);
//            exordiumWeakEncounters.add(MonsterHelper.JAW_WORM_ENC);
//            exordiumWeakEncounters.add(MonsterHelper.TWO_LOUSE_ENC);
//            exordiumWeakEncounters.add(MonsterHelper.SMALL_SLIMES_ENC);
//            for (MonsterInfo m : customExordiumWeakEncounters) {
//                exordiumWeakEncounters.add(m.name);
//            }
//            exordiumNormalEncounters.add(MonsterHelper.GREMLIN_GANG_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.LARGE_SLIME_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.LOTS_OF_SLIMES_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.BLUE_SLAVER_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.RED_SLAVER_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.THREE_LOUSE_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.TWO_FUNGI_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.LVL_1_THUGS_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.LVL_1_WILDLIFE_ENC);
//            exordiumNormalEncounters.add(MonsterHelper.LOOTER_ENC);
//            for (MonsterInfo m : customExordiumNormalEncounters) {
//                exordiumNormalEncounters.add(m.name);
//            }
//
//            cityWeakEncounters.add(MonsterHelper.SPHERE_GUARDIAN_ENC);
//            cityWeakEncounters.add(MonsterHelper.CHOSEN_ENC);
//            cityWeakEncounters.add(MonsterHelper.SHELL_PARASITE_ENC);
//            cityWeakEncounters.add(MonsterHelper.THREE_BYRDS_ENC);
//            cityWeakEncounters.add(MonsterHelper.TWO_THIEVES_ENC);
//            for (MonsterInfo m : customCityWeakEncounters) {
//                cityWeakEncounters.add(m.name);
//            }
//            cityNormalEncounters.add(MonsterHelper.CHOSEN_FLOCK_ENC);
//            cityNormalEncounters.add(MonsterHelper.CULTIST_CHOSEN_ENC);
//            cityNormalEncounters.add(MonsterHelper.SENTRY_SPHERE_ENC);
//            cityNormalEncounters.add(MonsterHelper.SNAKE_PLANT_ENC);
//            cityNormalEncounters.add(MonsterHelper.SNECKO_ENC);
//            cityNormalEncounters.add(MonsterHelper.TANK_HEALER_ENC);
//            cityNormalEncounters.add(MonsterHelper.THREE_CULTISTS_ENC);
//            cityNormalEncounters.add(MonsterHelper.PARASITE_AND_FUNGUS);
//            for (MonsterInfo m : customCityNormalEncounters) {
//                cityNormalEncounters.add(m.name);
//            }
//
//            beyondWeakEncounters.add(MonsterHelper.THREE_DARKLINGS_ENC);
//            beyondWeakEncounters.add(MonsterHelper.ORB_WALKER_ENC);
//            beyondWeakEncounters.add(MonsterHelper.THREE_SHAPES_ENC);
//            for (MonsterInfo m : customBeyondWeakEncounters) {
//                beyondWeakEncounters.add(m.name);
//            }
//            beyondNormalEncounters.add(MonsterHelper.FOUR_SHAPES_ENC);
//            beyondNormalEncounters.add(MonsterHelper.MAW_ENC);
//            beyondNormalEncounters.add(MonsterHelper.SPHERE_TWO_SHAPES_ENC);
//            beyondNormalEncounters.add(MonsterHelper.THREE_DARKLINGS_ENC);
//            beyondNormalEncounters.add(MonsterHelper.SPIRE_GROWTH_ENC);
//            beyondNormalEncounters.add(MonsterHelper.TRANSIENT_ENC);
//            beyondNormalEncounters.add(MonsterHelper.JAW_WORMS_HORDE);
//            beyondNormalEncounters.add(MonsterHelper.WRITHING_MASS_ENC);
//            for (MonsterInfo m : customBeyondNormalEncounters) {
//                beyondNormalEncounters.add(m.name);
//            }
//
//            for (String s : exordiumWeakEncounters) {
//                if (oldMonsterEncounter.equals(s)) {
//                    category = FutureInvadersEncounterCategory.WEAK;
//                }
//            }
//            for (String s : cityWeakEncounters) {
//                if (oldMonsterEncounter.equals(s)) {
//                    category = FutureInvadersEncounterCategory.WEAK;
//                }
//            }
//            for (String s : exordiumNormalEncounters) {
//                if (oldMonsterEncounter.equals(s)) {
//                    category = FutureInvadersEncounterCategory.NORMAL;
//                }
//            }
//            for (String s : cityNormalEncounters) {
//                if (oldMonsterEncounter.equals(s)) {
//                    category = FutureInvadersEncounterCategory.NORMAL;
//                }
//            }
//            this.invadingRoomWithFuture = true;
//            if (AbstractDungeon.id.equals(Exordium.ID)) {
//                changeImprobability(FUTURE_INVADERS_ACTUALLY_INVADING_IMPROBABILITY_CHANGE);
//                String newEncounterName;
//                if (category == FutureInvadersEncounterCategory.WEAK) {
//                    newEncounterName = cityWeakEncounters.get(AnomalyMod.anomalyRNG.random(0, cityWeakEncounters.size() - 1));
//                }
//                else {
//                    newEncounterName = cityNormalEncounters.get(AnomalyMod.anomalyRNG.random(0, cityWeakEncounters.size() - 1));
//                }
//                MonsterGroup newEncounter = MonsterHelper.getEncounter(newEncounterName);
//                AnomalyMod.logger.info("Just kidding, your encounter should be: " + newEncounterName);
//                return newEncounter;
//            }
//            else if (AbstractDungeon.id.equals(TheCity.ID)) {
//                changeImprobability(FUTURE_INVADERS_ACTUALLY_INVADING_IMPROBABILITY_CHANGE);
//                String newEncounterName;
//                if (category == FutureInvadersEncounterCategory.WEAK) {
//                    newEncounterName = beyondWeakEncounters.get(AnomalyMod.anomalyRNG.random(0, beyondWeakEncounters.size() - 1));
//                }
//                else {
//                    newEncounterName = beyondNormalEncounters.get(AnomalyMod.anomalyRNG.random(0, beyondNormalEncounters.size() - 1));
//                }
//                MonsterGroup newEncounter = MonsterHelper.getEncounter(newEncounterName);
//                AnomalyMod.logger.info("Just kidding, your encounter should be: " + newEncounterName);
//                return newEncounter;
//            }
//            else {
//                this.shouldBuffNonFutureInvaders = true;
//                //AnomalyMod.logger.info("The enemies get buffs instead");
//                return oldGroup;
//            }
//        }
//        else {
//            return oldGroup;
//        }
        return oldGroup;
    }

    private enum FutureInvadersEncounterCategory {
        WEAK, NORMAL
    }

    public float getRandomBuffsPowerLevel(int initialCounter) {
        return (float) Math.pow((float) initialCounter / 40, 2) + (float) initialCounter / 20 + 0.5F;
    }

    private int getRandomBuffsImprobabilityChange(int initialCounter) {
        return -initialCounter / 10;
    }

    public float getConvertGoldChance(int initialCounter) {
        if (this.counter < CONVERT_GOLD_IMPROBABILITY_MINIMUM) {
            return 0.0F;
        }
        else {
            int translatedInitialCounter = initialCounter + 3;
            return (-7.0F / 10.0F) + ((3.0F * translatedInitialCounter + 30.0F) / (2.0F * translatedInitialCounter + 70.0F));
        }
    }

    public float getSingleCardChoiceChance(int initialCounter) {
        if (this.counter < SINGLE_CARD_CHOICE_IMPROBABILITY_MINIMUM) {
            return 0.0F;
        }
        else {
            return (-6.0F / 5.0F) + ((2.0F * initialCounter) / ((float) initialCounter + 10.0F));
        }
    }

    public float getSurpriseElitesChance(int initialCounter) {
        if (!isValidRoomForSurpriseElite(initialCounter)) {
            return 0.0F;
        }
        else {
            return Math.min(-0.3F + (float) initialCounter * 0.01F, 0.5F);
        }
    }

    private int getSurpriseElitesImprobabilityChange(int initialCounter) {
        return -initialCounter / 10;
    }
}
