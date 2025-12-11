package com.example.textanalyzer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Minimal JavaFX application providing a user interface to analyse the
 * thematic content of text documents. The interface allows selecting a file,
 * performing the analysis and displaying the resulting statistics and
 * detected topic. A bar chart visualises the count of matched keywords per
 * topic.
 */
public class MainApp extends Application {
    /** Input field for the file path. */
    private TextField filePathField;
    /** Area for displaying detailed statistics. */
    private TextArea statisticsArea;
    /** Label for showing the detected topic. */
    private Label resultLabel;
    /** Bar chart to visualise topic match counts. */
    private BarChart<String, Number> barChart;
    /** Parser for reading documents. */
    private final DocumentParser parser = new DocumentParser();
    /** Analyser for computing statistics. */
    private final TextAnalyzer analyzer = new TextAnalyzer();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Анализатор текста");

        // File path input and browse button
        filePathField = new TextField();
        filePathField.setPrefWidth(400);

        Button browseButton = new Button("Выбрать...");
        browseButton.setOnAction(e -> browseFile(primaryStage));

        Button analyzeButton = new Button("Анализировать");
        analyzeButton.setOnAction(e -> analyzeFile());


        HBox fileBox = new HBox(10, new Label("Файл:"), filePathField, browseButton, analyzeButton);
        fileBox.setPadding(new Insets(10));

        resultLabel = new Label("Результат: ");
        statisticsArea = new TextArea();
        statisticsArea.setEditable(false);
        statisticsArea.setPrefRowCount(15);

        // Setup bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Тематика");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Количество");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Частота тематики");
        barChart.setAnimated(false);

        VBox root = new VBox(10, fileBox, resultLabel, statisticsArea, barChart);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Opens a file chooser allowing the user to select a text or Word document.
     * The chosen file path is placed into the text field for analysis.
     *
     * @param stage the owner window for the dialog
     */
    private void browseFile(Stage stage) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Выберите файл");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Текстовые и Word файлы", "*.txt", "*.docx", "*.doc"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );
        File selected = chooser.showOpenDialog(stage);
        if (selected != null) {
            filePathField.setText(selected.getAbsolutePath());
        }
    }

    /**
     * Reads the selected file, performs the analysis and updates the UI with
     * the results. Errors are reported via a dialog.
     */
    private void analyzeFile() {
        String path = filePathField.getText();
        if (path == null || path.isBlank()) {
            showAlert("Пожалуйста, выберите файл для анализа.");
            return;
        }
        try {
            String text = parser.parse(path);
            AnalysisResult result = analyzer.analyze(text);
            displayResult(result);
        } catch (IOException ex) {
            showAlert("Ошибка чтения файла: " + ex.getMessage());
        }
    }

    /**
     * Updates the user interface with the analysis result by setting the
     * detected topic label, populating the statistics text area and drawing
     * the bar chart.
     *
     * @param result analysis result to display
     */
    private void displayResult(AnalysisResult result) {
        if (result.getDetectedTopic() != null) {
            resultLabel.setText("Результат: " + result.getDetectedTopic().getDisplayName());
        } else {
            resultLabel.setText("Результат: не удалось определить");
        }
        statisticsArea.setText(result.toString());
        updateChart(result);
    }

    /**
     * Populates the bar chart with topic counts from the result. Each bar
     * represents the number of words associated with a specific topic.
     *
     * @param result analysis result containing the counts
     */
    private void updateChart(AnalysisResult result) {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<Topic, Integer> entry : result.getTopicCounts().entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().getDisplayName(), entry.getValue()));
        }
        series.setName("Вхождения");
        barChart.getData().add(series);
    }

    /**
     * Shows an error alert with the specified message.
     *
     * @param message text to display in the dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Entry point for launching the JavaFX application.
     *
     * @param args command line arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}