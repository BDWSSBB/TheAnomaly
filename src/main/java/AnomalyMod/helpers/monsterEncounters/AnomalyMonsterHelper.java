package AnomalyMod.helpers.monsterEncounters;

import AnomalyMod.AnomalyMod;
import AnomalyMod.patches.theEnd.AlterMonsterPatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.exordium.*;

import java.util.ArrayList;

public class AnomalyMonsterHelper {

    // Easy Pool
    public static final String SLIME_HORDE_ENC = "anomalyMod:SlimeHorde";
    public static final String THUG_HORDE_ENC = "anomalyMod:ThugHorde";
    public static final String SNECKO_CULT_ENC = "anomalyMod:SneckoCult";

    // Normal Pool
    public static final String CONSTRUCT_HORDE_ENC = "anomalyMod:ConstructHorde";
    public static final String WILDLIFE_HORDE_ENC = "anomalyMod:WildlifeHorde";
    public static final String MAW_AND_FRIENDS_ENC = "anomalyMod:MawAndFriends";
    public static final String THE_REMINDER_ENC = "anomalyMod:TheReminder";
    public static final String BLUE_CREW_ENC = "anomalyMod:BlueCrew";

    // Elite Pool
    public static final String ACT_1_ELITE_MIX_ENC = "anomalyMod:Act1EliteMix";
    public static final String ACT_2_ELITE_MIX_ENC = "anomalyMod:Act2EliteMix";
    public static final String ACT_3_ELITE_MIX_ENC = "anomalyMod:Act3EliteMix";

    // Last Stand
    public static final String LAUNCH_TERMINAL_HORDE_ENC = "anomalyMod:TheyAreBillions";
    public static final String LAUNCH_TERMINAL_HORDE_ENC2 = "anomalyMod:TheyAreBillions2";

    public static MonsterGroup getMainMonsters(String key) {
        switch (key) {
            case SLIME_HORDE_ENC: {
                AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                MonsterGroup monsterGroup = new MonsterGroup(new AbstractMonster[]{
                        new SpikeSlime_L(-385.0F, 20.0F),
                        new AcidSlime_L(120.0F, -8.0F)
                });
                AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                return monsterGroup;
            }
            case THUG_HORDE_ENC: {
                AbstractMonster slime;
                if (AbstractDungeon.miscRng.randomBoolean()) {
                    slime = new AcidSlime_M(-470.0F, MathUtils.random(-6.0F, 10.0F));
                } else {
                    slime = new SpikeSlime_M(-470.0F, MathUtils.random(-6.0F, 10.0F));
                }
                return new MonsterGroup(new AbstractMonster[]{
                        slime,
                        new Mugger(-190.0F, MathUtils.random(-6.0F, 10.0F)),
                        new SlaverRed(90.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case SNECKO_CULT_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new Snecko(-240f, MathUtils.random(0.0F, 20.0F)),
                        new Healer(80f, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case CONSTRUCT_HORDE_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new OrbWalker(-485.0F, MathUtils.random(-6.0F, 10.0F)),
                        MonsterHelper.getAncientShape(-135.0F, MathUtils.random(-6.0F, 10.0F)),
                        new Sentry(215.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case WILDLIFE_HORDE_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new SnakePlant(-400.0F, -11.0F),
                        new FungiBeast(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case MAW_AND_FRIENDS_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new Maw(-340.0F, MathUtils.random(0.0F, 10.0F)),
                        new JawWorm(80.0F, MathUtils.random(-6.0F, 10.0F), true)
                });
            }
            case THE_REMINDER_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new ShelledParasite(-260f, 15f),
                        new SlaverRed(120f, 0f)
                });
            }
            case BLUE_CREW_ENC: {
                AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                MonsterGroup monsterGroup = new MonsterGroup(new AbstractMonster[]{
                        new FungiBeast(-435.0F, MathUtils.random(-6.0F, 10.0F)),
                        new Cultist(-115.0F, MathUtils.random(-6.0F, 10.0F), true),
                        new SpikeSlime_L(175.0F, MathUtils.random(-6.0F, 10.0F))
                });
                AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                return monsterGroup;
            }
            case ACT_1_ELITE_MIX_ENC: {
                Lagavulin lagavulin = new Lagavulin(false);
                setMonsterPosition(lagavulin, -450.0F, MathUtils.random(-6.0F, 10.0F));
                return new MonsterGroup(new AbstractMonster[]{
                        lagavulin,
                        new GremlinNob(-900.0F, MathUtils.random(-6.0F, 10.0F)),
                        new Sentry(200.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case ACT_2_ELITE_MIX_ENC: {
                BookOfStabbing bookOfStabbing = new BookOfStabbing();
                setMonsterPosition(bookOfStabbing, 135.0F, MathUtils.random(-6.0F, 10.0F));
                return new MonsterGroup(new AbstractMonster[]{
                        new Taskmaster(-425.0F, MathUtils.random(-6.0F, 10.0F)),
                        getRandomGremlin(-170.0F, MathUtils.random(-6.0F, 10.0F)),
                        bookOfStabbing
                });
            }
            case ACT_3_ELITE_MIX_ENC: {
                GiantHead giantHead = new GiantHead();
                setMonsterPosition(giantHead, -25.0F, MathUtils.random(-6.0F, 10.0F));
                return new MonsterGroup(new AbstractMonster[]{
                        new SnakeDagger(-235.0F, MathUtils.random(325.0F, 360.0F)),
                        giantHead,
                        new SnakeDagger(170.0F, MathUtils.random(325.0F, 360.0F))
                });
            }
            case LAUNCH_TERMINAL_HORDE_ENC: {
                return generateLastStandFirstEncounter();
            }
            case LAUNCH_TERMINAL_HORDE_ENC2: {
                return generateLastStandSecondEncounter();
            }
            default: {
                return MonsterHelper.getEncounter(MonsterHelper.APOLOGY_SLIME_ENC);
            }
        }
    }

    public static MonsterGroup getBackupMonsters(String key) {
        switch (key) {
            case SLIME_HORDE_ENC: {
                if (AbstractDungeon.miscRng.randomBoolean()) {
                    AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                    MonsterGroup monsterGroup = new MonsterGroup(new AbstractMonster[]{
                            new SpikeSlime_L(0.0F, MathUtils.random(16.0F, 24.0F)),
                            new AcidSlime_M(0.0F, MathUtils.random(-6.0F, 10.0F))
                    });
                    AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                    return monsterGroup;
                } else {
                    AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                    MonsterGroup monsterGroup = new MonsterGroup(new AbstractMonster[]{
                            new AcidSlime_L(0.0F, MathUtils.random(-12.0F, -4.0F)),
                            new SpikeSlime_M(0.0F, MathUtils.random(-6.0F, 10.0F))
                    });
                    AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                    return monsterGroup;
                }
            }
            case THUG_HORDE_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new Looter(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        new SlaverBlue(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case SNECKO_CULT_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new Centurion(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case CONSTRUCT_HORDE_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new SphericGuardian(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        MonsterHelper.getAncientShape(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case WILDLIFE_HORDE_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new ShelledParasite(),
                        new FungiBeast(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case MAW_AND_FRIENDS_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new JawWorm(0.0F, MathUtils.random(-6.0F, 10.0F), true),
                        new JawWorm(0.0F, MathUtils.random(-6.0F, 10.0F), true)
                });
            }
            case THE_REMINDER_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new GremlinNob(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        new Chosen()
                });
            }
            case BLUE_CREW_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new SlaverBlue(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        new JawWorm(0.0F, MathUtils.random(-6.0F, 10.0F), true)
                });
            }
            case ACT_1_ELITE_MIX_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new Sentry(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        new Sentry(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case ACT_2_ELITE_MIX_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        getRandomGremlin(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        getRandomGremlin(0.0F, MathUtils.random(-6.0F, 10.0F)),
                        getRandomGremlin(0.0F, MathUtils.random(-6.0F, 10.0F))
                });
            }
            case ACT_3_ELITE_MIX_ENC: {
                return new MonsterGroup(new AbstractMonster[]{
                        new SnakeDagger(0.0F, MathUtils.random(325.0F, 360.0F)),
                        new SnakeDagger(0.0F, MathUtils.random(325.0F, 360.0F)),
                        new SnakeDagger(0.0F, MathUtils.random(325.0F, 360.0F))
                });
            }
            case LAUNCH_TERMINAL_HORDE_ENC: {
                return generateLastStandFirstBackup();
            }
            case LAUNCH_TERMINAL_HORDE_ENC2: {
                return generateLastStandSecondBackup();
            }
            default: {
                return null;
            }
        }
    }

    private static void setMonsterPosition(AbstractMonster monster, float x, float y) {
        monster.drawX = Settings.WIDTH * 0.75f + x * Settings.scale;
        monster.drawY = AbstractDungeon.floorY + y * Settings.scale;
    }

    private static AbstractMonster getRandomGremlin(float x, float y) {
        ArrayList<String> gremlins = new ArrayList<>();
        gremlins.add(GremlinFat.ID);
        gremlins.add(GremlinThief.ID);
        gremlins.add(GremlinWarrior.ID);
        gremlins.add(GremlinTsundere.ID);
        gremlins.add(GremlinWizard.ID);
        return MonsterHelper.getGremlin(gremlins.get(AbstractDungeon.miscRng.random(0, gremlins.size() - 1)), x, y);
    }

    private static MonsterGroup generateLastStandFirstEncounter() {
        AnomalyMod.logger.info("Oh my god! A huge swarm is approaching! They are billions!");
        AbstractMonster[] firstMonsters = new AbstractMonster[3];
        firstMonsters[0] = generateMonster(3);
        setMonsterPosition(firstMonsters[0], -485.0F, MathUtils.random(-6.0F, 10.0F));
        firstMonsters[1] = generateMonster(1);
        setMonsterPosition(firstMonsters[1], -150.0F, MathUtils.random(-6.0F, 10.0F));
        firstMonsters[2] = new Lagavulin(false);
        setMonsterPosition(firstMonsters[2], 200.0F, MathUtils.random(-6.0F, 10.0F));
        return new MonsterGroup(firstMonsters);
    }

    private static MonsterGroup generateLastStandFirstBackup() {
        return new MonsterGroup(new AbstractMonster[]{
                generateMonster(2),
                generateMonster(-1),
        });
    }

    private static MonsterGroup generateLastStandSecondEncounter() {
        AbstractMonster[] firstMonsters = new AbstractMonster[2];
        firstMonsters[0] = generateMonster(5);
        setMonsterPosition(firstMonsters[0], -420.0F, MathUtils.random(-6.0F, 10.0F));
        firstMonsters[1] = generateMonster(2);
        setMonsterPosition(firstMonsters[1], 80.0F, MathUtils.random(-6.0F, 10.0F));
        return new MonsterGroup(firstMonsters);
    }

    private static MonsterGroup generateLastStandSecondBackup() {
        return new MonsterGroup(new AbstractMonster[]{
                generateMonster(3),
                generateMonster(4),
                generateMonster(4),
                new WrithingMass(),
        });
    }

    private static AbstractMonster generateMonster(int intensity) {
        switch (intensity) {
            case -1: {
                switch (AbstractDungeon.miscRng.random(0, 1)) {
                    case 0: {
                        return new Snecko();
                    }
                    case 1: {
                        return new Chosen();
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            case 1: {
                switch (AbstractDungeon.miscRng.random(0, 6)) {
                    case 0: {
                    }
                    case 1: {
                    }
                    case 2: {
                        return MonsterHelper.getAncientShape(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 3: {
                        return new FungiBeast(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 4: {
                        return new AcidSlime_M(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 5: {
                        return new SpikeSlime_M(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 6: {
                        return new Sentry(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            case 2: {
                switch (AbstractDungeon.miscRng.random(0, 2)) {
                    case 0: {
                        return new SlaverBlue(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 1: {
                        return new SlaverRed(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 2: {
                        return new JawWorm(0.0F, MathUtils.random(-6.0F, 10.0F), true);
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            case 3: {
                switch (AbstractDungeon.miscRng.random(0, 3)) {
                    case 0: {
                        AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                        AcidSlime_L slime = new AcidSlime_L(0.0F, MathUtils.random(-8.0F));
                        AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                        return slime;
                    }
                    case 1: {
                        AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = false;
                        SpikeSlime_L slime = new SpikeSlime_L(0.0F, MathUtils.random(-6.0F, 10.0F));
                        AlterMonsterPatch.LargeSlimeSplitOption.slimesShouldSplitSwitch = true;
                        return slime;
                    }
                    case 2: {
                        return new ShelledParasite();
                    }
                    case 3: {
                        return new SphericGuardian(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            case 4: {
                switch (AbstractDungeon.miscRng.random(0, 2)) {
                    case 0: {
                        return new OrbWalker(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    case 1: {
                        return new SnakePlant(0.0F, -11.0F);
                    }
                    case 2: {
                        return new GremlinNob(0.0F, MathUtils.random(-6.0F, 10.0F));
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            case 5: {
                switch (AbstractDungeon.miscRng.random(0, 1)) {
                    case 0: {
                        return new Maw(0.0F, MathUtils.random(0.0F, 10.0F));
                    }
                    case 1: {
                        return new BookOfStabbing();
                    }
                    default: {
                        return new ApologySlime();
                    }
                }
            }
            default: {
                return new ApologySlime();
            }
        }
    }
}