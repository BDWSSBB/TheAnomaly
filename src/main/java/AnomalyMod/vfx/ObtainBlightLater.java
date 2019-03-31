package AnomalyMod.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ObtainBlightLater extends AbstractGameEffect {

    private AbstractBlight blight;
    private boolean noDuplicateCheck;

    public ObtainBlightLater(AbstractBlight blight, boolean noDuplicateCheck) {
        this.blight = blight;
        this.noDuplicateCheck = noDuplicateCheck;
    }

    public ObtainBlightLater(AbstractBlight blight) {
        this(blight, false);
    }

    @Override
    public void update() {
        if (!(this.noDuplicateCheck && AbstractDungeon.player.hasBlight(blight.blightID))) {
            AbstractDungeon.getCurrRoom().spawnBlightAndObtain(Settings.scale * 64.0F, Settings.HEIGHT - Settings.scale * 176.0F, this.blight);
        }
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
