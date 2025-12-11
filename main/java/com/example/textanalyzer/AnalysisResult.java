package com.example.textanalyzer;

import java.util.Map;

/**
 * Immutable container for the results of a text analysis. An instance of this
 * class is returned by {@link TextAnalyzer#analyze(String)} and holds
 * statics about word occurrences per topic as well as keyword counts.
 */
public class AnalysisResult {
    /** Counts of matched words for each topic. */
    private final Map<Topic, Integer> topicCounts;
    /** Counts of each individual keyword that was matched. */
    private final Map<String, Integer> keywordCounts;
    /** Total number of words processed in the analysed text. */
    private final int totalWords;
    /** Topic with the highest number of matches or {@code null} if none. */
    private final Topic detectedTopic;

    /**
     * Constructs a new result.
     *
     * @param topicCounts   map of topics to their respective match counts
     * @param keywordCounts map of keywords to occurrence counts
     * @param totalWords    total number of words analysed
     * @param detectedTopic topic with the highest frequency or {@code null}
     */
    public AnalysisResult(Map<Topic, Integer> topicCounts,
                          Map<String, Integer> keywordCounts,
                          int totalWords,
                          Topic detectedTopic) {
        this.topicCounts = topicCounts;
        this.keywordCounts = keywordCounts;
        this.totalWords = totalWords;
        this.detectedTopic = detectedTopic;
    }

    /**
     * Returns the map containing the count of matched words per topic.
     *
     * @return a map from topic to count
     */
    public Map<Topic, Integer> getTopicCounts() {
        return topicCounts;
    }

    /**
     * Returns the map of keywords to their respective occurrence counts.
     *
     * @return a map from keyword root to count
     */
    public Map<String, Integer> getKeywordCounts() {
        return keywordCounts;
    }

    /**
     * Returns the total number of words processed in the analysed text.
     *
     * @return total words analysed
     */
    public int getTotalWords() {
        return totalWords;
    }

    /**
     * Returns the topic that has the highest number of keyword matches.
     * If no matches were found for any topic, this method returns {@code null}.
     *
     * @return detected topic or {@code null}
     */
    public Topic getDetectedTopic() {
        return detectedTopic;
    }

    /**
     * Computes the percentage of words matched for the specified topic relative
     * to the total number of words processed. If no words were processed the
     * percentage is zero.
     *
     * @param topic the topic for which to compute the percentage
     * @return fraction of words matching the topic in the range [0.0, 1.0]
     */
    public double getTopicPercentage(Topic topic) {
        int count = topicCounts.getOrDefault(topic, 0);
        if (totalWords == 0) {
            return 0.0;
        }
        return (double) count / totalWords;
    }

    /**
     * Returns a multiline string summarizing the analysis statistics. The
     * summary lists the total word count, per‑topic counts with percentages
     * and individual keyword occurrence counts. This is primarily intended
     * for displaying in the user interface.
     *
     * @return human readable summary of this result
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total words: ").append(totalWords).append(System.lineSeparator());
        for (Map.Entry<Topic, Integer> entry : topicCounts.entrySet()) {
            double percent = getTopicPercentage(entry.getKey()) * 100.0;
            sb.append(entry.getKey().getDisplayName())
              .append(": ")
              .append(entry.getValue())
              .append(" (")
              .append(String.format("%.2f", percent))
              .append("%")
              .append(")")
              .append(System.lineSeparator());
        }
        sb.append("Detected topic: ")
          .append(detectedTopic != null ? detectedTopic.getDisplayName() : "Не удалось определить")
          .append(System.lineSeparator());
        if (!keywordCounts.isEmpty()) {
            sb.append("Keyword counts:").append(System.lineSeparator());
            keywordCounts.forEach((k, v) ->
                    sb.append("  ").append(k).append(": ").append(v).append(System.lineSeparator()));
        }
        return sb.toString();
    }
}