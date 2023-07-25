package net.highskiesmc.fishing.util.enums;

import org.checkerframework.common.value.qual.ArrayLen;
import org.checkerframework.common.value.qual.IntRange;

import java.util.Arrays;
import java.util.LinkedList;

public enum Perk {
    XP_GAIN("Xp Gain", new LinkedList<>(Arrays.asList(1.15, 1.25, 1.50, 1.80D, 2.50))),
    FISHING_SPEED("Fishing Speed", new LinkedList<>(Arrays.asList(10.00, 20.00, 30.00, 40.00, 50.00))),
    ITEM_FIND("Item Find", new LinkedList<>(Arrays.asList(25.00, 50.00, 125.00, 175.00, 250.00))),
    DOUBLE_DROPS("Double Drops", new LinkedList<>(Arrays.asList(2.50, 5.00, 7.50, 10.00, 15.00))),
    DOUBLE_XP("Double Xp", new LinkedList<>(Arrays.asList(2.50, 5.00, 7.50, 10.00, 15.00)));
    private final String VALUE;
    // Length = amount of skills...Should also edit skill point gain to account for new perks.
    public static final int MAX_PERK_LEVEL = 5;
    private final @ArrayLen(MAX_PERK_LEVEL) LinkedList<Double> LEVELS; // Double = perk amount. Index + 1 = level

    Perk(String value, LinkedList<Double> levels) {
        this.VALUE = value;
        this.LEVELS = levels;
    }

    /**
     * @param level Level of the perk
     * @return Amount of the bonus that should be provided for that level
     */
    public double getAmount(@IntRange(from = 1, to = MAX_PERK_LEVEL) int level) {
        return this.LEVELS.get(level - 1);
    }

    public Integer getLevel(Double amount) {
        return this.LEVELS.indexOf(amount) + 1;
    }

    /**
     * @return Amount of skill points required to purchase the next level, or -1 if at max level
     */
    public static int getNextLevelCost(int currentLevel) {
        switch (currentLevel) {
            case 0 -> {
                return 1;
            }
            case 1 -> {
                return 2;
            }
            case 2 -> {
                return 4;
            }
            case 3 -> {
                return 8;
            }
            case 4 -> {
                return 10;
            }
            default -> {
                return -1;
            }
        }
    }

    public String getValue() {
        return this.VALUE;
    }
}
