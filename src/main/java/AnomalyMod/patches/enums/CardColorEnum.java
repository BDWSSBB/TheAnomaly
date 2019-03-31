package AnomalyMod.patches.enums;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardHelper;

public class CardColorEnum {

    public static final Color ANOMALY_WISTFUL_COLOR = CardHelper.getColor(158.0f, 169.0f, 208.0f); // Don't question the name.

    @SpireEnum
    public static AbstractCard.CardColor ANOMALY_WISTFUL;
}
