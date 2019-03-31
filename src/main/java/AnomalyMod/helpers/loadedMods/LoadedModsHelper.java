package AnomalyMod.helpers.loadedMods;

import AnomalyMod.AnomalyMod;
import com.evacipated.cardcrawl.modthespire.Loader;

public class LoadedModsHelper {

    public static boolean jungleModLoaded;
    public static boolean strawberrySpireModLoaded;

    static {
        if (Loader.isModLoaded("TheJungle")) {
            AnomalyMod.logger.info("Detected The Jungle mod.");
            jungleModLoaded = true;
        }
        if (Loader.isModLoaded("StrawberrySpire")) {
            AnomalyMod.logger.info("Detected Strawberry Spire mod.");
            strawberrySpireModLoaded = true;
        }
    }
}
