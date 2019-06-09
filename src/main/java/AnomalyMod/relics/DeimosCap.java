package AnomalyMod.relics;

import AnomalyMod.blights.ImprobabilityDrive;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class DeimosCap extends AbstractAnomalyRelic {

    public static final String ID = "anomalyMod:DeimosCap";
    public static final Texture IMAGE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/placeholder.png");
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage("AnomalyModResources/relics/outline/placeholderOutline.png");
    private static final int DAMAGE_PER_IMPROBABILITY = 3;

    public DeimosCap() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        ImprobabilityDrive drive;
        if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID)) {
            drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        } else {
            drive = new ImprobabilityDrive();
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, drive);
        }
        drive.counter = 2 * AbstractDungeon.floorNum;
        ImprobabilityDrive.checkForSpawnInfoBlights();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_PER_IMPROBABILITY + DESCRIPTIONS[1];
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        ImprobabilityDrive drive;
        if (AbstractDungeon.player.hasBlight(ImprobabilityDrive.ID)) {
            drive = (ImprobabilityDrive) AbstractDungeon.player.getBlight(ImprobabilityDrive.ID);
        } else {
            drive = new ImprobabilityDrive();
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, drive);
        }
        drive.counter = 2 * AbstractDungeon.floorNum;
        ImprobabilityDrive.checkForSpawnInfoBlights();
    }

    @Override
    public int afterFinalImprobabilityCalculations(int finalImprobabilityNumber) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && finalImprobabilityNumber > 0) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, DAMAGE_PER_IMPROBABILITY * finalImprobabilityNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
        return 0;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DeimosCap();
    }
}
