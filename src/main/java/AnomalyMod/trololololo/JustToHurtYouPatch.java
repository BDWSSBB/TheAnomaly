package AnomalyMod.trololololo;

import AnomalyMod.AnomalyMod;
import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.helpers.loadedMods.LoadedModsHelper;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

// You know who you are.
@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfCombatLogic"
)
public class JustToHurtYouPatch {

    public static void Postfix(AbstractPlayer __instance) {
        checkForAbsoluteUnit();
    }

    private static void checkForAbsoluteUnit() {
        if (LoadedModsHelper.jungleModLoaded && (AbstractDungeon.player instanceof AnomalyCharacter || CardCrawlGame.playerName.equals("Aragolt"))) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m.id.equals("theJungle:GiantWrat") && ((CardCrawlGame.playerName.equals("Rhapsody") && MathUtils.randomBoolean()) || CardCrawlGame.playerName.equals("BDWSSBB") || CardCrawlGame.playerName.equals("Aragolt"))) {
                    // You know you love this hardcode. And you know who I'm telling this to.
                    AbstractDungeon.actionManager.addToBottom(new TalkAction(true, "In awe at the size of this lad. Absolute unit.", 0.0F, 3.0F));
                    switch (MathUtils.random(0, 3)) {
                        case 0: {
                            m.name = "Megachonker";
                            break;
                        }
                        case 1: {
                            m.name = "R.O.U.S.";
                            break;
                        }
                        case 2: {
                            m.name = "A Fine Lad";
                            break;
                        }
                        case 3: {
                            m.name = "Absolute Unit";
                            break;
                        }
                        default: {
                            // WTF
                        }
                        AnomalyMod.logger.info("You know I had to do it to them.");
                    }
                    break;
                }
            }
        }
    }
}
