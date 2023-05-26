package net.highskiesmc.fishing.util.enums;

import java.text.DecimalFormat;
import java.util.Random;

public enum Perk {
    ITEM_FIND(0.25, 1.0),
    EXPERIENCE_MULTIPLIER(0.05, 0.2);

    private final Double MIN_INCREMENT;
    private final Double MAX_INCREMENT;

    Perk(Double minIncrement, Double maxIncrement) {
        this.MIN_INCREMENT = minIncrement;
        this.MAX_INCREMENT = maxIncrement;
    }

    public double getMaxIncrement() {
        return MAX_INCREMENT;
    }

    public double getMinIncrement() {
        return MIN_INCREMENT;
    }

    public double getRandomIncrement() {
        return Double.parseDouble(new DecimalFormat("#.##").format(this.MIN_INCREMENT + (new Random().nextDouble() * (this.MAX_INCREMENT - this.MIN_INCREMENT))));
    }
}
