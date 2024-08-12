package app.settingswindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.mainwindow.MainWindowController;
import app.util.FontHelper;
import app.util.FontConfig;
import javafx.animation.RotateTransition;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class SettingsWindowController extends ControllerBase {
    @FXML
    private VBox fontOptionsVBox;
    @FXML
    private ImageView fontArrow;
    @FXML
    private HBox appearanceHBox, fontHBox;
    @FXML
    private VBox editorFontOptionsVBox, consoleFontOptionsVBox;
    @FXML
    private HBox editorHBox, consoleHBox;
    @FXML
    private ComboBox<String> editorFontFamilyComboBox, consoleFontFamilyComboBox;
    @FXML
    private Spinner<Double> editorFontSizeSpinner, consoleFontSizeSpinner;
    @FXML
    private TextArea editorFontPreviewTextArea, consoleFontPreviewTextArea;

    private boolean fontOptionsVisible = false;
    private MainWindowController mainWindowController;

    private double tempEditorFontSize, tempConsoleFontSize;
    private String tempEditorFontFamily = FontConfig.getEditorFontFamily(), tempConsoleFontFamily = FontConfig.getConsoleFontFamily();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SETTINGS_WINDOW).setController(this);
        mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        setupFontFamilyComboBoxes();
        setupFontSizeSpinners();
        setupPreviewTextAreas();
    }

    private void setupFontFamilyComboBoxes() {
        setupEditorFontFamilyComboBox();
        setupConsoleFontFamilyComboBox();
    }

    private void setupEditorFontFamilyComboBox() {
        editorFontFamilyComboBox.getItems().addAll(FontConfig.EDITOR_FONT_FAMILIES);
        editorFontFamilyComboBox.setValue(FontConfig.getEditorFontFamily());
        editorFontFamilyComboBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tempEditorFontFamily = newValue;
            double currentFontSize = tempEditorFontSize != 0 ? tempEditorFontSize : FontConfig.getEditorFontSize();

            editorFontPreviewTextArea.setStyle("-fx-font-family: " + newValue + "; -fx-font-size: " + currentFontSize + "pt;");
        });
    }

    private void setupConsoleFontFamilyComboBox() {
        consoleFontFamilyComboBox.getItems().addAll(FontConfig.CONSOLE_FONT_FAMILIES);
        consoleFontFamilyComboBox.setValue(FontConfig.getConsoleFontFamily());
        consoleFontFamilyComboBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            tempConsoleFontFamily = newValue;
            double currentFontSize = tempConsoleFontSize != 0 ? tempConsoleFontSize : FontConfig.getConsoleFontSize();

            consoleFontPreviewTextArea.setStyle("-fx-font-family: " + newValue + "; -fx-font-size: " + currentFontSize + "pt;");
        });
    }

    private void setupFontSizeSpinners() {
        setupEditorFontSizeSpinner();
        setupConsoleFontSizeSpinner();
    }

    private void setupEditorFontSizeSpinner() {
        SpinnerValueFactory<Double> editorFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                FontConfig.EDITOR_MIN_FONT_SIZE,
                FontConfig.EDITOR_MAX_FONT_SIZE,
                FontConfig.getEditorFontSize(),
                FontConfig.FONT_STEP
        );

        editorFontSizeSpinner.setValueFactory(editorFactory);

        editorFontSizeSpinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            editorFontPreviewTextArea.setStyle("-fx-font-size: " + newValue + "pt;");
            tempEditorFontSize = newValue;
        });
    }

    private void setupConsoleFontSizeSpinner() {
        SpinnerValueFactory<Double> consoleFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                FontConfig.CONSOLE_MIN_FONT_SIZE,
                FontConfig.CONSOLE_MAX_FONT_SIZE,
                FontConfig.getConsoleFontSize(),
                FontConfig.FONT_STEP
        );

        consoleFontSizeSpinner.setValueFactory(consoleFactory);

        consoleFontSizeSpinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            consoleFontPreviewTextArea.setStyle("-fx-font-size: " + newValue + "pt;");
            tempConsoleFontSize = newValue;
        });
    }

    private void setupPreviewTextAreas() {
        editorFontPreviewTextArea.setStyle("-fx-font-size: " + FontConfig.getEditorFontSize() + "pt;");
        consoleFontPreviewTextArea.setStyle("-fx-font-size: " + FontConfig.getConsoleFontSize() + "pt;");
    }

    @FXML
    private void OKSettings() {
        applySettings();
        WindowHelper.hideWindow(Window.SETTINGS_WINDOW);
    }

    @FXML
    private void applySettings() {
        if (editorFontOptionsVBox.isVisible()) {
            applyEditorSettings();
        } else if (consoleFontOptionsVBox.isVisible()) {
            applyConsoleSettings();
        }
    }

    @FXML
    private void cancelSettings() {
        WindowHelper.hideWindow(Window.SETTINGS_WINDOW);
    }

    @FXML
    private void toggleFontOptions() {
        fontOptionsVisible = !fontOptionsVisible;

        RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), fontArrow);
        if (fontOptionsVisible) {
            fontOptionsVBox.setVisible(true);
            fontOptionsVBox.setManaged(true);
            rotateTransition.setByAngle(90);
            fontArrow.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/arrow_right_icon.png"))));
        } else {
            fontOptionsVBox.setVisible(false);
            fontOptionsVBox.setManaged(false);
            rotateTransition.setByAngle(-90);
            fontArrow.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/icons/arrow_right_icon.png"))));
        }
        rotateTransition.play();

        selectFontOptions();
    }

    @FXML
    private void selectAppearance() {
        clearSelection();
        appearanceHBox.getStyleClass().add("selected");
    }

    @FXML
    private void selectFontOptions() {
        clearSelection();
        fontHBox.getStyleClass().add("selected");
    }

    @FXML
    private void selectEditor() {
        clearSelection();
        editorHBox.getStyleClass().add("selected");
        showEditorFontOptions();
    }

    @FXML
    private void selectConsole() {
        clearSelection();
        consoleHBox.getStyleClass().add("selected");
        showConsoleFontOptions();
    }

    private void applyEditorSettings() {
        if (tempEditorFontFamily != null) {
            FontHelper.setFontFamily(tempEditorFontFamily, mainWindowController.editorArea);
            FontConfig.setEditorFontFamily(tempEditorFontFamily);
        }
        FontHelper.setFontSize(tempEditorFontSize, mainWindowController.editorArea);
        FontConfig.setEditorFontSize(tempEditorFontSize);
    }

    private void applyConsoleSettings() {
        if (tempConsoleFontFamily != null) {
            FontHelper.setFontFamily(tempConsoleFontFamily, mainWindowController.consoleTextFlow);
            FontConfig.setConsoleFontFamily(tempConsoleFontFamily);
        }
        FontHelper.setFontSize(tempConsoleFontSize, mainWindowController.consoleTextFlow);
        FontConfig.setConsoleFontSize(tempConsoleFontSize);
    }

    private void showEditorFontOptions() {
        editorFontOptionsVBox.setVisible(true);
        consoleFontOptionsVBox.setVisible(false);
    }

    private void showConsoleFontOptions() {
        consoleFontOptionsVBox.setVisible(true);
        editorFontOptionsVBox.setVisible(false);
    }

    private void clearSelection() {
        appearanceHBox.getStyleClass().remove("selected");
        consoleHBox.getStyleClass().remove("selected");
        editorHBox.getStyleClass().remove("selected");
        fontHBox.getStyleClass().remove("selected");
    }
}
