package AnomalyMod.helpers.blacklistedContent;

import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.potions.PoisonPotion;
import com.megacrit.cardcrawl.relics.DeadBranch;

import java.util.ArrayList;

public class BlacklistedContentHelper {

    public static ArrayList<String> bannedCards = new ArrayList<>();
    public static ArrayList<String> bannedRelics = new ArrayList<>();
    public static ArrayList<String> bannedPotions = new ArrayList<>();
    // Events handled somewhere else.

    static {
        // Banned cards

        // Basically a free win.
        bannedCards.add(Apotheosis.ID);

        // Banned relics

        // Braindead relic, braindead win.
        bannedRelics.add(DeadBranch.ID);

        // Banned potions

        // Absolute trash on him. Why does this exist outside Silent's pool?
        bannedPotions.add(PoisonPotion.POTION_ID);
    }
}
