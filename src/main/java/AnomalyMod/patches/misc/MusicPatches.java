package AnomalyMod.patches.misc;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.loadedMods.LoadedModsHelper;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;

public class MusicPatches {

    @SpirePatch(
            clz = MainMusic.class,
            method = "getSong"
    )
    public static class CustomMusic {

        public static Music Postfix(Music __result, MainMusic __instance, String key) {
            if (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ON || (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)) {
                if (key.equals(Exordium.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_SirenSong.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_TheRiseOfTheCheshyre.ogg");
                    }
                }
                if (key.equals(TheCity.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_DeathToBigHead.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_StormComing.ogg");
                    }
                }
                if (key.equals(TheBeyond.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_TheBanshee.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Utopia.ogg");
                    }
                }
            }
            if (LoadedModsHelper.jungleModLoaded && (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ON || (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter))) {
                if (key.equals("JUNGLEMAIN")) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Frost.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Samhain.ogg");
                    }
                }
            }
            return __result;
        }
    }

    @SpirePatch(
            clz = TempMusic.class,
            method = "getSong"
    )
    public static class CustomTempMusic {

        public static Music Postfix(Music __result, TempMusic __instance, String key) {
            if (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ON || (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)) {
                if (key.equals("STS_BossVictoryStinger_1_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_2_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_3_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_4_v3_MUSIC.ogg")) {
                    __result = MainMusic.newMusic("AnomalyModResources/audio/music/SoulEye_PathComplete.ogg");
                }
                if (key.equals("BOSS_BEYOND")) {
                    __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Autolysis.ogg");
                }
            }
            if (LoadedModsHelper.jungleModLoaded && (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ON || (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter))) {
                if (key.equals("JUNGLEELITE")) {
                    __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_RoadToTheApocalypse.ogg");
                }
            }
            return __result;
        }
    }
}
