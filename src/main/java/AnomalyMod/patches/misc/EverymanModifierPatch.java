package AnomalyMod.patches.misc;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.trials.CustomTrial;

import java.util.ArrayList;

@SpirePatch(
        clz = CustomModeScreen.class,
        method = "addNonDailyMods"
)
public class EverymanModifierPatch {

    @SpireInsertPatch(
            rloc = 1,
            localvars = {"modId"}
    )
    public static void Insert(CustomModeScreen __instance, CustomTrial trial, ArrayList<String> modIds, String modId) {
        if (modId.equals("anomalyMod:Everyman")) {
            for (AbstractRelic r : RelicLibrary.starterList) {
                boolean containsAlready = false;
                for (AbstractRelic re : trial.relics) {
                    if (re.relicId.equals(r.relicId)) {
                        containsAlready = true;
                    }
                }
                if (!containsAlready) {
                    trial.addStarterRelic(r.relicId);
                }
            }
            trial.setShouldKeepStarterRelic(false);
        }
    }
}
