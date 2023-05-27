package net.highskiesmc.fishing.util;

import java.util.HashMap;
import java.util.Map;

public class CustomLevelSystem {
    public static final int MAX_LEVEL = 100; // Maximum level
    private static final Map<Integer, Double> EXPERIENCE_TABLE = new HashMap<>(); // Map of experience requirements

    static {
        // Populate the experience table with level and experience requirements
        for (int level = 1; level <= MAX_LEVEL; level++) {
            double experienceRequired = calculateExperienceRequired(level);
            EXPERIENCE_TABLE.put(level, experienceRequired);
        }
    }

    public static double calculateExperienceRequired(int level) {
        // Custom formula to calculate the experience required for a specific level
        // Adjust the formula based on your desired progression curve
        // For example, linear progression: experienceRequired = level * 100;
        // Or exponential progression: experienceRequired = Math.pow(level, 2) * 100;
        return Math.pow(level, 2) * 100;
    }

    public static double getExperienceRequiredForLevel(int level) {
        // Retrieve the experience required for a specific level from the experience table
        if (level <= 0 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid level specified.");
        }
        return EXPERIENCE_TABLE.get(level);
    }

    public static int getNextLevel(int currentLevel, double currentExperience) {
        // Find the next level based on the current level and experience
        double experienceRequired = getExperienceRequiredForLevel(currentLevel + 1);
        while (currentExperience >= experienceRequired && currentLevel < MAX_LEVEL) {
            currentExperience -= experienceRequired;
            currentLevel++;
            experienceRequired = getExperienceRequiredForLevel(currentLevel);
        }
        return currentLevel;
    }
}

