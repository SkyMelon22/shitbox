package com.example.textanalyzer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-тесты для {@link TextAnalyzer}.
 * Проверяем, что анализатор корректно определяет тематику текста.
 */
public class TextAnalyzerTest {

    @Test
    void programmingTextIsDetectedAsProgramming() {
        TextAnalyzer analyzer = new TextAnalyzer();

        // Текст с явной программной лексикой
        String text = "Этот текст посвящён программированию на Java. " +
                "Класс, объект, алгоритм, код, компилятор, " +
                "функция, метод, переменная и цикл используются в примерах.";

        AnalysisResult result = analyzer.analyze(text);

        // 1) Проверяем, что тема определена
        assertNotNull(result.getDetectedTopic(), "Тема не должна быть null");

        // 2) Проверяем, что определена именно тематика PROGRAMMING
        assertEquals(Topic.PROGRAMMING, result.getDetectedTopic(),
                "Ожидалась тематика PROGRAMMING для программного текста");

        // 3) У программирования должно быть хотя бы одно совпадение
        Integer programmingCount = result.getTopicCounts().get(Topic.PROGRAMMING);
        assertNotNull(programmingCount, "Для PROGRAMMING должен быть счётчик");
        assertTrue(programmingCount > 0, "Для PROGRAMMING должно быть > 0 совпадений ключевых слов");
    }
}
