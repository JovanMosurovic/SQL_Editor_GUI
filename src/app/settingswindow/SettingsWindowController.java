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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SETTINGS_WINDOW).setController(this);
        mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        setupFontSizeSpinners();
        setupPreviewTextAreas();
    }

    private void setupFontSizeSpinners() {
        setupEditorFontSizeSpinner();
        setupConsoleFontSizeSpinner();
    }

    private void setupEditorFontSizeSpinner() {
        SpinnerValueFactory<Double> editorFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                FontConfig.getEditorMinFontSize(),
                FontConfig.getEditorMaxFontSize(),
                FontConfig.getEditorFontSize(),
                FontConfig.getFontStep()
        );

        editorFontSizeSpinner.setValueFactory(editorFactory);

        editorFontSizeSpinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            editorFontPreviewTextArea.setStyle("-fx-font-size: " + newValue + "pt;");
            tempEditorFontSize = newValue;
        });
    }

    private void setupConsoleFontSizeSpinner() {
        SpinnerValueFactory<Double> consoleFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                FontConfig.getConsoleMinFontSize(),
                FontConfig.getConsoleMaxFontSize(),
                FontConfig.getConsoleFontSize(),
                FontConfig.getFontStep()
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

    private void applyEditorSettings() {
        FontHelper.setFontSize(tempEditorFontSize, mainWindowController.codeArea);
        FontConfig.setEditorFontSize(tempEditorFontSize);
    }

    private void applyConsoleSettings() {
        FontHelper.setFontSize(tempConsoleFontSize, mainWindowController.consoleTextFlow);
        FontConfig.setConsoleFontSize(tempConsoleFontSize);
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
