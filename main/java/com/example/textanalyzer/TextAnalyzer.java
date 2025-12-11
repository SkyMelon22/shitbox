package com.example.textanalyzer;

import java.util.*;

/**
 * Performs thematic analysis of plain text. The analyser uses a predefined set
 * of keyword roots for each {@link Topic} to determine how many words in the
 * input relate to a given subject area. The topic with the highest number
 * of matches is reported as the detected topic.
 */
public class TextAnalyzer {
    /** Map of topics to lists of keyword roots used for matching. */
    private final Map<Topic, List<String>> keywordMap;

    /**
     * Constructs a new analyser and populates the internal dictionary of
     * keyword roots for each topic. The roots should be lower‑case and
     * represent common stems of words associated with their topic.
     */
    public TextAnalyzer() {
        keywordMap = new EnumMap<>(Topic.class);
        initializeKeywords();
    }

    /**
     * Populates the keyword map with simple Russian and English stems for each
     * supported topic. These lists can be extended by the developer to
     * improve accuracy. Keywords should be lower‑case to match normalized
     * input.
     */
    private void initializeKeywords() {
        keywordMap.put(Topic.MEDICAL, Arrays.asList(
                "медиц", "здоров", "болезн", "лечен", "пациент",
                "врач", "симптом", "анализ", "диагноз", "терап",
                "операц", "биолог", "орган", "клетк", "ткан", "сердц", "кров",
                "сосуд", "кислород", "дыхан", "легк", "газообмен", "анатом", "физиолог", "иммун", "инфекц", "вирус",
                "бактери", "ген", "геном", "диагност", "процедур",
                "реабилит", "профилак", "патолог", "хирург", "терапевт",
                "педиатр", "стоматолог", "невролог", "кардиолог",
                "онколог", "эпидем", "вакцин", "инъекц", "таблет",
                "препарат", "фармак", "рецепт", "больнич", "клиник",
                "амбулатор", "стационар", "скорая", "давлен", "пульс",
                "температур", "воспален", "перелом", "раствор", "шов"));
        keywordMap.put(Topic.HISTORICAL, Arrays.asList(
                "истор", "прошл", "древн", "эпох", "дата",
                "событ", "историк", "археолог", "импер", "революц",
                "войн", "культ", "средневеков", "античн", "новый времен", "современ",
                "первобытн", "феодал", "монар", "династ", "цар",
                "корол", "император", "полковод", "завоеван",
                "сражен", "битв", "договор", "пакт", "союз",
                "цивилиз", "государств", "общество", "класс",
                "сослов", "крепост", "реформа", "восстан", "бунт",
                "хронолог", "летопис", "архив", "документ", "артефакт",
                "памятник", "музей", "историограф", "первоисточник",
                "исследован", "традиц", "обыча", "обряд"));
        keywordMap.put(Topic.PROGRAMMING, Arrays.asList(
                "программ", "разработ", "код", "компьютер", "язык",
                "алгоритм", "система", "данн", "приложен", "java",
                "python", "c++", "code", "software", "hardware", "информац", "технолог", "it", "разработчик", "программист",
                "функц", "перемен", "класс", "объект", "метод",
                "интерфейс", "модул", "библиотек", "фреймворк",
                "база данн", "sql", "nosql", "веб", "backend",
                "frontend", "fullstack", "дебаг", "отладк", "компиляц",
                "интерпрет", "синтакс", "логик", "цикл", "услов",
                "рекурс", "массив", "список", "стек", "очеред",
                "дерев", "граф", "сорт", "поиск", "оптимиз",
                "гит", "github", "gitlab", "репозитор", "commit",
                "контрол верс", "ide", "сред", "развертыван", "deploy",
                "тест", "юнит-тест", "интеграц", "агл", "машин обучен",
                "нейросет", "искуствен интеллект", "кибербезопас"));
        keywordMap.put(Topic.NETWORKS, Arrays.asList(
                "сеть", "network", "tcp", "udp", "ip",
                "internet", "интернет", "маршрут", "протокол", "канал",
                "соединен", "трафик", "сервер", "клиент", "сетев", "lan", "wan", "vpn", "dns", "dhcp",
                "http", "https", "ftp", "ssh", "ssl",
                "tls", "web", "сайт", "браузер", "роутер",
                "маршрутизатор", "коммутатор", "switch", "хаб", "мост",
                "firewall", "брандмауэр", "порт", "сокет", "пакет",
                "кадр", "шифрован", "аутентиф", "авториз", "доступ",
                "пропускная способ", "задержк", "ping", "latency",
                "тополог", "звезд", "кольцо", "шин", "mesh",
                "беспровод", "wi-fi", "wifi", "bluetooth", "ethernet",
                "оптоволок", "коаксиал", "витая пара", "isp",
                "провайдер", "ddos", "атак", "сканирован", "порт"));
        keywordMap.put(Topic.CRYPTOGRAPHY, Arrays.asList(
                "крипт", "шифр", "шифров", "кодиров", "rsa",
                "aes", "des", "hash", "хэш", "шифран",
                "public key", "private key", "key", "криптограф", "криптоанал", "стойкост", "взлом",
                "дешифр", "расшифр", "криптосистем", "ключ",
                "открытый ключ", "закрытый ключ", "секретный ключ",
                "эллипт", "ecdh", "ecdsa", "blowfish", "twofish",
                "тройной des", "3des", "sha", "md5", "md4",
                "соле", "salt", "iv", "вектор инициализ", "подп",
                "электронная подп", "цифровая подп", "сертификат",
                "pki", "infosec", "кибербезопас", "confidential",
                "целост", "integrity", "доступ", "availability",
                "аутентич", "non-repud", "отказоустойчив",
                "протокол согласован", "key exchange", "diffie-hellman",
                "квант крипт", "post-quantum", "блокчейн", "blockchain",
                "майнинг", "консенсус", "proof of work", "proof of stake",
                "асимметрич", "симметрич", "stream cipher", "block cipher",
                "криптовалют", "биткоин", "ethereum", "monero", "privacy coin"));
        keywordMap.put(Topic.FINANCE, Arrays.asList(
                "финанс", "эконом", "банк", "валют", "цен",
                "рынок", "инвести", "кредит", "акци", "бирж",
                "налог", "бюджет", "рубл", "доллар", "евро",
                "crypto", "деньг", "капитал", "прибыл", "убыток", "доход",
                "расход", "счет", "вклад", "депоз", "ипотек",
                "заем", "долг", "обязательств", "актив", "пассив",
                "баланс", "отчет", "аудит", "бухгалтер", "трейд",
                "трейдер", "инвестор", "спекуля", "дивиденд",
                "облигац", "фьючерс", "опцион", "дериватив", "фонд",
                "etf", "взаимный фонд", "хедж-фонд", "ликвид",
                "волатиль", "риск", "доход", "ставк", "процент",
                "кредитная став", "рефинанс", "инфляц", "дефляц",
                "рецесс", "кризис", "санкц", "эмбарго", "тариф",
                "пошлин", "тамож", "транш", "платеж", "перевод",
                "swift", "пластиковая карт", "кэш", "налич",
                "безнал", "финтех", "fintech", "страхов", "страхован",
                "пенс", "пенсионный фонд", "мсфо", "gaap", "рсбу"));
    }

    /**
     * Analyses the supplied text and returns a new {@link AnalysisResult}
     * containing statistics about keyword occurrences per topic and per word.
     *
     * @param text text to analyse; may be {@code null} or empty
     * @return analysis result containing counts and the detected topic
     */
    public AnalysisResult analyze(String text) {
        if (text == null || text.isEmpty()) {
            return new AnalysisResult(Collections.emptyMap(), Collections.emptyMap(), 0, null);
        }
        String normalized = normalizeText(text);
        String[] words = normalized.split("\\s+");
        Map<Topic, Integer> topicCounts = new EnumMap<>(Topic.class);
        Map<String, Integer> keywordCounts = new HashMap<>();
        for (Topic topic : Topic.values()) {
            topicCounts.put(topic, 0);
        }
        int totalWords = 0;
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            totalWords++;
            Topic matchedTopic = null;
            String matchedKeyword = null;
            // iterate through all topics and their keywords until a match is found
            for (Map.Entry<Topic, List<String>> entry : keywordMap.entrySet()) {
                for (String keyword : entry.getValue()) {
                    if (word.startsWith(keyword)) {
                        topicCounts.put(entry.getKey(), topicCounts.get(entry.getKey()) + 1);
                        matchedTopic = entry.getKey();
                        matchedKeyword = keyword;
                        break;
                    }
                }
                if (matchedTopic != null) {
                    break;
                }
            }
            if (matchedKeyword != null) {
                keywordCounts.put(matchedKeyword, keywordCounts.getOrDefault(matchedKeyword, 0) + 1);
            }
        }
        // Determine the topic with the highest count
        Topic detected = null;
        int maxCount = 0;
        for (Topic topic : Topic.values()) {
            int count = topicCounts.getOrDefault(topic, 0);
            if (count > maxCount) {
                maxCount = count;
                detected = topic;
            }
        }
        return new AnalysisResult(topicCounts, keywordCounts, totalWords, detected);
    }

    /**
     * Normalizes text by converting it to lower case and replacing all
     * non‑alphanumeric characters with spaces. This allows keywords to be
     * matched using simple substring checks. Unicode letters and digits are
     * preserved.
     *
     * @param text input text
     * @return normalised text consisting of lower‑case words separated by spaces
     */
    public String normalizeText(String text) {
        // Convert to lower case and replace punctuation with spaces
        String normalized = text.toLowerCase();
        // Replace everything that is not a letter or digit with a space
        normalized = normalized.replaceAll("[^\\p{L}\\p{Nd}]+", " ");
        return normalized;
    }

    /**
     * Returns an unmodifiable view of the keyword map for inspection or
     * debugging. Modifying the returned map will result in an
     * {@link UnsupportedOperationException}.
     *
     * @return read‑only map of topics to keyword lists
     */
    public Map<Topic, List<String>> getKeywordMap() {
        return Collections.unmodifiableMap(keywordMap);
    }
}