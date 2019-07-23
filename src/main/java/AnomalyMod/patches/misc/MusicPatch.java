package AnomalyMod.patches.misc;

import AnomalyMod.character.AnomalyCharacter;
import AnomalyMod.helpers.config.ConfigHelper;
import AnomalyMod.helpers.loadedMods.LoadedModsHelper;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.audio.MainMusic;
import com.megacrit.cardcrawl.audio.TempMusic;
import com.megacrit.cardcrawl.dungeons.*;

public class MusicPatch {

    @SpirePatch(
            clz = MainMusic.class,
            method = "getSong"
    )
    public static class CustomMusic {

        public static Music Postfix(Music __result, MainMusic __instance, String key) {
            if (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ON ||
                    (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)
            ) {
                if (key.equals(Exordium.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_SirenSong.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_TheRiseOfTheCheshyre.ogg");
                    }
                } else if (key.equals(TheCity.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_DeathToBigHead.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_StormComing.ogg");
                    }
                } else if (key.equals(TheBeyond.ID)) {
                    if (MathUtils.randomBoolean()) {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_TheBanshee.ogg");
                    } else {
                        __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Utopia.ogg");
                    }
                } else if (key.equals(TheEnding.ID)) {
                    __result = MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Shadowman.ogg");
                }
            }
            if (LoadedModsHelper.jungleModLoaded &&
                    (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ON ||
                            (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)
                    )
            ) {
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

        public static SpireReturn<Music> Prefix(TempMusic __instance, String key) {
            if (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ON ||
                    (ConfigHelper.useCustomMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)
            ) {
                if (key.equals("STS_BossVictoryStinger_1_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_2_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_3_v3_MUSIC.ogg")
                        || key.equals("STS_BossVictoryStinger_4_v3_MUSIC.ogg")) {
                    return SpireReturn.Return(MainMusic.newMusic("AnomalyModResources/audio/music/SoulEye_PathComplete.ogg"));
                } else if (key.equals("BOSS_BEYOND")) {
                    return SpireReturn.Return(MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_Autolysis.ogg"));
                } else if (key.equals("BOSS_ENDING")) {
                    return SpireReturn.Return(MainMusic.newMusic("AnomalyModResources/audio/music/OneArcDegree+Cybernetika_GhostComet.ogg"));
                }
            }
            if (LoadedModsHelper.jungleModLoaded &&
                    (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ON ||
                            (ConfigHelper.overrideJungleMusic == ConfigHelper.CustomMusicConfig.ANOMALY && AbstractDungeon.player instanceof AnomalyCharacter)
                    )
            ) {
                if (key.equals("JUNGLEELITE")) {
                    return SpireReturn.Return(MainMusic.newMusic("AnomalyModResources/audio/music/Cheshyre_RoadToTheApocalypse.ogg"));
                }
            }

            if (key.equals("ANOMALY_ENDING")) {
                return SpireReturn.Return(MainMusic.newMusic("AnomalyModResources/audio/music/Note!_535am.ogg"));
            }

            return SpireReturn.Continue();
        }
    }
}
