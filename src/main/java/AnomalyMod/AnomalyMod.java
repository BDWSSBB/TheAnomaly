package AnomalyMod;

import AnomalyMod.cards.AbstractAnomalyCard;
import AnomalyMod.cards.colorless.DummyCard;
import AnomalyMod.cards.status.BadSector;
import AnomalyMod.cards.wistful.*;
import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.events.exordium.Dedmos;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.patches.enums.CardColorEnum;
import AnomalyMod.patches.enums.PlayerClassEnum;
import AnomalyMod.relics.*;
import AnomalyMod.trololololo.TheEvilDevModels.TheOriginalEvilDev;
import basemod.*;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
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
        CustomSavable<Random> {

    public static final Logger logger = LogManager.getLogger(AnomalyMod.class.getSimpleName());

    private static final String ATTACK_PORTRAIT = "AnomalyModResources/512/bgAttackWistful.png";
    private static final String SKILL_PORTRAIT = "AnomalyModResources/512/bgSkillWistful.png";
    private static final String POWER_PORTRAIT = "AnomalyModResources/512/bgPowerWistful.png";
    private static final String ENERGY_ORB = "AnomalyModResources/character/placeholder/card_mystic_orb.png";
    private static final String BIG_ATTACK_PORTRAIT= "AnomalyModResources/1024/bigAttackWistful.png";
    private static final String BIG_SKILL_PORTRAIT = "AnomalyModResources/1024/bigSkillWistful.png";
    private static final String BIG_POWER_PORTRAIT = "AnomalyModResources/1024/bigPowerWistful.png";
    private static final String ENERGY_ORB_PORTRAIT = "AnomalyModResources/character/placeholder/big_card_mystic_orb.png";
    private static final String CHARACTER_BUTTON = "AnomalyModResources/character/placeholder/crowbotButton.png";
    private static final String CHARACTER_PORTRAIT = "AnomalyModResources/character/placeholder/simpleAnomalyPortrait.jpg";
    private static final String SMALL_ENERGY_SYMBOL = "AnomalyModResources/character/placeholder/manaSymbol.png";

    public static Random anomalyRNG;

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
        //BaseMod.addCard(new Blackout());
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
        BaseMod.addCard(new DualAction());
        BaseMod.addCard(new DummyCard());
        BaseMod.addCard(new DumpsterDive());
        BaseMod.addCard(new EmergencyExit());
        BaseMod.addCard(new EscapeAttempt());
        BaseMod.addCard(new Excavate());
        BaseMod.addCard(new ExtraPockets());
        BaseMod.addCard(new Fak3());
        BaseMod.addCard(new Fault());
        BaseMod.addCard(new Featherweight());
        BaseMod.addCard(new Filter());
        BaseMod.addCard(new FluxCapacitor());
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
        BaseMod.addCard(new Minimalist());
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
        BaseMod.addCard(new Stutter());
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
        final Keyword[] keywords = (Keyword[])gson.fromJson(json, (Class)Keyword[].class);
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

        BaseMod.addMonster(TheOriginalEvilDev.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new TheOriginalEvilDev(0.0F, 0.0F)
        }));
        HashMap<String, Sfx> map = (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put("JEVIL-ANYTHING", new Sfx("AnomalyModResources/trololololo/snd_joker_anything.wav", false));
        map.put("JEVIL-BYEBYE", new Sfx("AnomalyModResources/trololololo/snd_joker_byebye.wav", false));
        map.put("CHOO-CHOO", new Sfx("AnomalyModResources/trololololo/ILikeTrains.ogg", false));

        BaseMod.addSaveField("anomalyRNG", this);
    }

    @Override
    public Random onSave() {
        logger.info("Saved integer: " + anomalyRNG.counter);
        return anomalyRNG;
    }

    @Override
    public void onLoad(Random loaded) {
        generateSeeds();
        if (loaded != null) {
            logger.info("Loaded integer: " + loaded.counter);
            anomalyRNG.counter = loaded.counter;
        }
        else {
            logger.info("loaded = null");
        }
    }

    public static void generateSeeds() {
        logger.info("generateSeeds was called");
        if (Settings.seed != null) {
            anomalyRNG = new Random(Settings.seed);
        }
        else {
            logger.info("generateSeeds called with null Settings.seed");
            anomalyRNG = new Random();
        }
    }
}