package net.highskiesmc.fishing.util;

import java.util.HashMap;
import java.util.Map;

public class CustomLevelSystem {
    private static final int MAX_LEVEL = 100; // Maximum level
    private static final Map<Integer, Integer> EXPERIENCE_TABLE = new HashMap<>(); // Map to store experience requirements

    static {
        // Populate the experience table with level and experience requirements
        for (int level = 1; level <= MAX_LEVEL; level++) {
            int experienceRequired = calculateExperienceRequired(level);
            EXPERIENCE_TABLE.put(level, experienceRequired);
        }
    }

    public static int calculateExperienceRequired(int level) {
        // Custom formula to calculate the experience required for a specific level
        // Adjust the formula based on your desired progression curve
        // For example, linear progression: experienceRequired = level * 100;
        // Or exponential progression: experienceRequired = (int) (Math.pow(level, 2) * 100);
        return (int) (Math.pow(level, 2) * 100);
    }

    public static int getExperienceRequiredForLevel(int level) {
        // Retrieve the experience required for a specific level from the experience table
        if (level <= 0 || level > MAX_LEVEL) {
            throw new IllegalArgumentException("Invalid level specified.");
        }
        return EXPERIENCE_TABLE.get(level);
    }

    public static int getNextLevel(int currentLevel, double currentExperience) {
        // Find the next level based on the current level and experience
        int experienceRequired = getExperienceRequiredForLevel(currentLevel);
        while (currentExperience >= experienceRequired && currentLevel < MAX_LEVEL) {
            currentExperience -= experienceRequired;
            currentLevel++;
            experienceRequired = getExperienceRequiredForLevel(currentLevel);
        }
        return currentLevel;
    }
}

