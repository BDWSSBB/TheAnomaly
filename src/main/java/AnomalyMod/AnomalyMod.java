package AnomalyMod;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.cards.colorless.DummyCard;
import AnomalyMod.cards.status.BadSector;
import AnomalyMod.cards.wistful.*;
import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.dungeons.AnomalyTheEnding;
import AnomalyMod.events.exordium.Dedmos;
import AnomalyMod.events.theEnd.*;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.modSaveData.AnomalyModDungeonData;
import AnomalyMod.helpers.modSaveData.AnomalyModSaveFile;
import AnomalyMod.helpers.monsterEncounters.AnomalyMonsterHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import AnomalyMod.patches.enums.PlayerClassEnum;
import AnomalyMod.relics.*;
import AnomalyMod.trololololo.TheEvilDevModels.AprilFoolsEvilDev;
import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.custom.CustomMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@SpireInitializer
public class AnomalyMod implements
        PostInitializeSubscriber,
        AddCustomModeModsSubscriber,
        EditCardsSubscriber,
        EditCharactersSubscriber,
        EditKeywordsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        CustomSavable<AnomalyModSaveFile> {

    public static final Logger logger = LogManager.getLogger(AnomalyMod.class.getSimpleName());

    private static final String ATTACK_PORTRAIT = "AnomalyModResources/512/bgAttackWistful.png";
    private static final String SKILL_PORTRAIT = "AnomalyModResources/512/bgSkillWistful.png";
    private static final String POWER_PORTRAIT = "AnomalyModResources/512/bgPowerWistful.png";
    private static final String ENERGY_ORB = "AnomalyModResources/512/wistfulOrb.png";
    private static final String BIG_ATTACK_PORTRAIT = "AnomalyModResources/1024/bigAttackWistful.png";
    private static final String BIG_SKILL_PORTRAIT = "AnomalyModResources/1024/bigSkillWistful.png";
    private static final String BIG_POWER_PORTRAIT = "AnomalyModResources/1024/bigPowerWistful.png";
    private static final String ENERGY_ORB_PORTRAIT = "AnomalyModResources/1024/bigWistfulOrb.png";
    private static final String CHARACTER_BUTTON = "AnomalyModResources/character/placeholder/characterButton.png";
    private static final String CHARACTER_PORTRAIT = "AnomalyModResources/character/placeholder/simpleAnomalyPortrait.jpg";
    private static final String SMALL_ENERGY_SYMBOL = "AnomalyModResources/character/placeholder/manaSymbol.png";

    public AnomalyMod() {
        BaseMod.subscribe(this);

        BaseMod.addColor(
                CardColorEnum.ANOMALY_WISTFUL,
                CardColorEnum.ANOMALY_WISTFUL_COLOR,
                ATTACK_PORTRAIT,
                SKILL_PORTRAIT,
                POWER_PORTRAIT,
                ENERGY_ORB,
                BIG_ATTACK_PORTRAIT,
                BIG_SKILL_PORTRAIT,
                BIG_POWER_PORTRAIT,
                ENERGY_ORB_PORTRAIT,
                SMALL_ENERGY_SYMBOL
        );

        ConfigHelper.setupConfig();
    }

    // Used by @SpireInitializer
    public static void initialize() {
        new AnomalyMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new AbstractAnomalyCard.ImprobabilityNumber());
        BaseMod.addDynamicVariable(new AbstractAnomalyCard.SecondMagicNumber());

        BaseMod.addCard(new Apex());
        BaseMod.addCard(new Backdoor());
        BaseMod.addCard(new BadSector());
        BaseMod.addCard(new BlastPipe());
        BaseMod.addCard(new Blender());
        BaseMod.addCard(new Blink());
        BaseMod.addCard(new BudgetPack());
        BaseMod.addCard(new ClockworkCrawl());
        BaseMod.addCard(new ClusterCrunch());
        BaseMod.addCard(new Collision());
        BaseMod.addCard(new ConicSands());
        BaseMod.addCard(new Discontinuity());
        BaseMod.addCard(new DoOver());
        BaseMod.addCard(new Crank());
        BaseMod.addCard(new Decoy());
        BaseMod.addCard(new DeepDitch());
        BaseMod.addCard(new DefendAnomaly());
        BaseMod.addCard(new DeimosDown());
        BaseMod.addCard(new DelayedFlight());
        BaseMod.addCard(new Derail());
        BaseMod.addCard(new DialMForMurder());
        BaseMod.addCard(new Disable());
        BaseMod.addCard(new Driller());
        BaseMod.addCard(new DualAction());
        BaseMod.addCard(new DummyCard());
        BaseMod.addCard(new DumpsterDive());
        BaseMod.addCard(new EmergencyExit());
        BaseMod.addCard(new EscapeAttempt());
        BaseMod.addCard(new Excavate());
        BaseMod.addCard(new ExpirationDate());
        BaseMod.addCard(new ExtraPockets());
        BaseMod.addCard(new Fak3());
        BaseMod.addCard(new Fault());
        BaseMod.addCard(new Featherweight());
        BaseMod.addCard(new Filter());
        BaseMod.addCard(new FluxCapacitor());
        BaseMod.addCard(new FourEyes());
        BaseMod.addCard(new FreeForm());
        BaseMod.addCard(new Gravekeep());
        BaseMod.addCard(new Gape());
        BaseMod.addCard(new Hook());
        BaseMod.addCard(new Hurdle());
        BaseMod.addCard(new Hyperdrift());
        BaseMod.addCard(new Invad3r());
        BaseMod.addCard(new Invalidate());
        BaseMod.addCard(new Invert());
        BaseMod.addCard(new Jailbreak());
        BaseMod.addCard(new Krieg());
        BaseMod.addCard(new Maladaptation());
        BaseMod.addCard(new Multithreading());
        BaseMod.addCard(new NegativeReception());
        BaseMod.addCard(new Overdrive());
        BaseMod.addCard(new Pincushion());
        BaseMod.addCard(new PopcornFunction());
        BaseMod.addCard(new Preloader());
        BaseMod.addCard(new Procrastinate());
        BaseMod.addCard(new RedWorld());
        BaseMod.addCard(new Riftwalk());
        BaseMod.addCard(new SawBones());
        BaseMod.addCard(new Seeker());
        BaseMod.addCard(new SixthRevision());
        BaseMod.addCard(new SmoothSailing());
        BaseMod.addCard(new SocketSlice());
        BaseMod.addCard(new StrikeAnomaly());
        BaseMod.addCard(new SystemHack());
        BaseMod.addCard(new TelomereFlake());
        BaseMod.addCard(new Timeslip());
        BaseMod.addCard(new Torment());
        BaseMod.addCard(new Trailmixing());
        BaseMod.addCard(new TrashChute());
        BaseMod.addCard(new TrenchWarfare());
        BaseMod.addCard(new WarpDrive());
    }

    @Override
    public void receiveCustomModeMods(List<CustomMod> list) {
        list.add(new CustomMod("anomalyMod:Everyman", "b", false));
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new AnomalyCharacter(CardCrawlGame.playerName), CHARACTER_BUTTON, CHARACTER_PORTRAIT, PlayerClassEnum.ANOMALY_CLASS);
    }

    @Override
    public void receiveEditKeywords() { // I believe this is kio's black magic that I'm afraid of.
        final Gson gson = new Gson();
        final String json = Gdx.files.internal("AnomalyModResources/localization/AnomalyMod-Keyword.json").readString(String.valueOf(StandardCharsets.UTF_8));
        final Keyword[] keywords = (Keyword[]) gson.fromJson(json, (Class) Keyword[].class);
        if (keywords != null) {
            for (final Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new AuditingHijack(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new ControlHijack(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new DeimosCap(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new DummyFile(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new GreenRock(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new Megachette(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new MilkTea(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new MowsetrapCar(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new Nightshirt(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new Overrider(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new Ratchet(), CardColorEnum.ANOMALY_WISTFUL);
        BaseMod.addRelicToCustomPool(new WheeRemote(), CardColorEnum.ANOMALY_WISTFUL);
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(BlightStrings.class, "AnomalyModResources/localization/AnomalyMod-BlightStrings.json");
        BaseMod.loadCustomStringsFile(RunModStrings.class, "AnomalyModResources/localization/AnomalyMod-CustomModifierStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "AnomalyModResources/localization/AnomalyMod-CardStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "AnomalyModResources/localization/AnomalyMod-CharacterStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, "AnomalyModResources/localization/AnomalyMod-EventStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "AnomalyModResources/localization/AnomalyMod-PowerStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "AnomalyModResources/localization/AnomalyMod-RelicStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "AnomalyModResources/localization/AnomalyMod-UIStrings.json");
    }

    @Override
    public void receivePostInitialize() {
        ConfigHelper.initializeConfig();

        BaseMod.addEvent(Dedmos.ID, Dedmos.class, Exordium.ID);

        // These don't naturally spawn, I use a manual way to spawn these.
        BaseMod.addEvent(LaunchTerminal.ID, LaunchTerminal.class, AnomalyTheEnding.ID);
        BaseMod.addEvent(PhaseControllerChipQuest.ID, PhaseControllerChipQuest.class, AnomalyTheEnding.ID);
        BaseMod.addEvent(NeuralCablesQuest.ID, NeuralCablesQuest.class, AnomalyTheEnding.ID);
        BaseMod.addEvent(DataDecoderQuest.ID, DataDecoderQuest.class, AnomalyTheEnding.ID);
        BaseMod.addEvent(CoolantFanQuest.ID, CoolantFanQuest.class, AnomalyTheEnding.ID);

        // Act 4 Easy Pool
        BaseMod.addMonster(AnomalyMonsterHelper.SLIME_HORDE_ENC, "Slime Horde", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.SLIME_HORDE_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.THUG_HORDE_ENC, "Thug Horde", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.THUG_HORDE_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.SNECKO_CULT_ENC, "Snecko Cult", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.SNECKO_CULT_ENC));

        // Act 4 Normal Pool
        BaseMod.addMonster(AnomalyMonsterHelper.CONSTRUCT_HORDE_ENC, "Construct Horde", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.CONSTRUCT_HORDE_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.WILDLIFE_HORDE_ENC, "Wildlife Horde", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.WILDLIFE_HORDE_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.MAW_AND_FRIENDS_ENC, "Maw and Friends", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.MAW_AND_FRIENDS_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.THE_REMINDER_ENC, "The Reminder", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.THE_REMINDER_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.BLUE_CREW_ENC, "Blue Crew", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.BLUE_CREW_ENC));

        // Act 4 Elite Pool
        BaseMod.addMonster(AnomalyMonsterHelper.ACT_1_ELITE_MIX_ENC, "Exordium Elites", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.ACT_1_ELITE_MIX_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.ACT_2_ELITE_MIX_ENC, "City Elites", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.ACT_2_ELITE_MIX_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.ACT_3_ELITE_MIX_ENC, "Beyond Elites", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.ACT_3_ELITE_MIX_ENC));

        // The Last Stand
        BaseMod.addMonster(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC, "The Last Stand", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC));
        BaseMod.addMonster(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC2, "The Last Stand", () -> AnomalyMonsterHelper.getMainMonsters(AnomalyMonsterHelper.LAUNCH_TERMINAL_HORDE_ENC2));

        BaseMod.addMonster(AprilFoolsEvilDev.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new AprilFoolsEvilDev(0.0F, 0.0F)
        }));
        HashMap<String, Sfx> map = (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put("JEVIL-ANYTHING", new Sfx("AnomalyModResources/trololololo/snd_joker_anything.wav", false));
        map.put("JEVIL-BYEBYE", new Sfx("AnomalyModResources/trololololo/snd_joker_byebye.wav", false));
        map.put("CHOO-CHOO", new Sfx("AnomalyModResources/trololololo/ILikeTrains.ogg", false));

        BaseMod.addSaveField("anomalySaveData", this);
    }

    @Override
    public AnomalyModSaveFile onSave() {
        return AnomalyModDungeonData.createSaveData();
    }

    @Override
    public void onLoad(AnomalyModSaveFile loaded) {
        AnomalyModDungeonData.loadSaveData(loaded);
    }
}