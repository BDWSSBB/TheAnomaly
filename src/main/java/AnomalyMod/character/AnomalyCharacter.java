package AnomalyMod.character;

import AnomalyMod.cards.status.BadSector;
import AnomalyMod.cards.wistful.DefendAnomaly;
import AnomalyMod.cards.wistful.Derail;
import AnomalyMod.cards.wistful.DualAction;
import AnomalyMod.cards.wistful.StrikeAnomaly;
import AnomalyMod.patches.enums.CardColorEnum;
import AnomalyMod.patches.enums.PlayerClassEnum;
import AnomalyMod.relics.ControlHijack;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class AnomalyCharacter extends CustomPlayer {

    public static final String ID = "anomalyMod:AnomalyCharacter";
    private static final CharacterStrings CHARACTER_STRINGS = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String[] ORB_TEXTURES = {
            "AnomalyModResources/character/orbs/layer1.png",
            "AnomalyModResources/character/orbs/layer2.png",
            "AnomalyModResources/character/orbs/layer3.png",
            "AnomalyModResources/character/orbs/layer4.png",
            "AnomalyModResources/character/orbs/layer5.png",
            "AnomalyModResources/character/orbs/layer6.png",
            "AnomalyModResources/character/orbs/layer1D.png",
            "AnomalyModResources/character/orbs/layer2D.png",
            "AnomalyModResources/character/orbs/layer3D.png",
            "AnomalyModResources/character/orbs/layer4D.png",
            "AnomalyModResources/character/orbs/layer5D.png"
    };
    public static final String ORB_VFX_PATH = "AnomalyModResources/character/orbs/layer5.png";
    public static final float[] LAYER_SPEEDS = {
            0.0F, 0.0F, 0.0F, 0.0F, 0.0F
    };
    public static final String ANOMALY_SHOULDER_FIRE = "AnomalyModResources/character/placeholder/simpleAnomalyShoulder.png";
    public static final String ANOMALY_SHOULDER_NORMAL = "AnomalyModResources/character/placeholder/simpleAnomalyShoulder.png";
    public static final String ANOMALY_CORPSE = "AnomalyModResources/character/orbs/layer5.png";
    public static final int ENERGY_PER_TURN = 4;
    public static final int STARTING_HP = 45;
    public static final int MAX_HP = 45;
    public static final int ORB_SLOTS = 0;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 6;

    public AnomalyCharacter(String name) {
        super(name, PlayerClassEnum.ANOMALY_CLASS, ORB_TEXTURES, ORB_VFX_PATH, LAYER_SPEEDS, null, null);

        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 220.0F * Settings.scale);

        initializeClass("AnomalyModResources/character/placeholder/simpleAnomalyModel.png",
                ANOMALY_SHOULDER_FIRE,
                ANOMALY_SHOULDER_NORMAL,
                ANOMALY_CORPSE,
                getLoadout(),
                8.0F,
                0.0F,
                200.0F,
                260.0F,
                new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return CHARACTER_STRINGS.NAMES[0];
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return CardColorEnum.ANOMALY_WISTFUL;
    }

    @Override
    public Color getCardRenderColor() {
        return CardColorEnum.ANOMALY_WISTFUL_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new DualAction();
    }

    @Override
    public Color getCardTrailColor() {
        return CardColorEnum.ANOMALY_WISTFUL_COLOR;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 3;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.sound.playA("ATTACK_FAST", MathUtils.random(-0.2f, 0.2f));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public String getLocalizedCharacterName() {
        return CHARACTER_STRINGS.NAMES[0];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new AnomalyCharacter(this.name);
    }

    @Override
    public String getSpireHeartText() {
        return CHARACTER_STRINGS.TEXT[2];
    }

    @Override
    public Color getSlashAttackColor() {
        return CardColorEnum.ANOMALY_WISTFUL_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.SLASH_HEAVY
        };
    }

    @Override
    public String getVampireText() {
        return CHARACTER_STRINGS.TEXT[1];
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(BadSector.ID);
        for (int i = 0; i < 5; i++) {
            retVal.add(StrikeAnomaly.ID);
        }
        for (int i = 0; i < 5; i++) {
            retVal.add(DefendAnomaly.ID);
        }
        retVal.add(DualAction.ID);
        retVal.add(Derail.ID);
        UnlockTracker.markCardAsSeen(StrikeAnomaly.ID);
        UnlockTracker.markCardAsSeen(DefendAnomaly.ID);
        UnlockTracker.markCardAsSeen(DualAction.ID);
        UnlockTracker.markCardAsSeen(Derail.ID);
        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(ControlHijack.ID);
        UnlockTracker.markRelicAsSeen(ControlHijack.ID);
        return retVal;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                CHARACTER_STRINGS.NAMES[0],
                CHARACTER_STRINGS.TEXT[0],
                STARTING_HP,
                MAX_HP,
                ORB_SLOTS,
                STARTING_GOLD,
                HAND_SIZE,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false);
    }
}
