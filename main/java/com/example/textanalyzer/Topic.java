package com.example.textanalyzer;

/**
 * Enumeration of supported text topics. Each topic represents a thematic
 * category that the {@link TextAnalyzer} can recognize based on keyword
 * occurrences in a document. The {@link #getDisplayName()} method provides
 * a human‑readable Russian name for displaying results in the user interface.
 */
public enum Topic {
    /**
     * Texts related to medicine and health.
     */
    MEDICAL,
    /**
     * Texts describing historical events, periods or figures.
     */
    HISTORICAL,
    /**
     * Texts about programming, software development and computer science.
     */
    PROGRAMMING,
    /**
     * Texts discussing computer networks and communication protocols.
     */
    NETWORKS,
    /**
     * Texts focusing on cryptography, encryption and security.
     */
    CRYPTOGRAPHY,
    /**
     * Texts concerned with finance, economics and banking.
     */
    FINANCE;

    /**
     * Returns a human‑friendly Russian name for the topic. This is used in
     * dialogs and reports instead of the raw enum constant name.
     *
     * @return displayable topic name in Russian
     */
    public String getDisplayName() {
        switch (this) {
            case MEDICAL:
                return "Медицинская";
            case HISTORICAL:
                return "Историческая";
            case PROGRAMMING:
                return "Программирование";
            case NETWORKS:
                return "Сети";
            case CRYPTOGRAPHY:
                return "Криптография";
            case FINANCE:
                return "Финансы";
            default:
                return name();
        }
    }
}