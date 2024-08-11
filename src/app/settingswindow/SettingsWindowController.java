package app.settingswindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.mainwindow.MainWindowController;
import app.util.CodeAreaHelper;
import app.util.FontHelper;
import app.util.FontSizeConfig;
import app.util.TextFlowHelper;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private HBox editorHBox, consoleHBox;
    @FXML
    private ComboBox<String> fontFamilyComboBox;
    @FXML
    private Spinner<Double> fontSizeSpinner;
    @FXML
    private TextArea fontPreviewTextArea;

    private boolean fontOptionsVisible = false;

    private MainWindowController mainWindowController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Window.getWindowAt(Window.SETTINGS_WINDOW).setController(this);
        mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        setupFontSizeSpinner();
    }

    private void setupFontSizeSpinner() {
        fontSizeSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                FontSizeConfig.getEditorMinFontSize(),
                FontSizeConfig.getEditorMaxFontSize(),
                FontSizeConfig.getEditorFontSize(),
                FontSizeConfig.getFontStep()));

        fontSizeSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            fontPreviewTextArea.setStyle("-fx-font-size: " + newValue + "pt;");
            FontSizeConfig.setEditorFontSize(newValue);
        });
    }

    //region Font configuration

    public void increaseConsoleFontSize() {
        if (FontSizeConfig.getConsoleFontSize() < FontSizeConfig.getConsoleMaxFontSize()) {
            FontHelper.increaseFontSize(FontSizeConfig.getFontStep(), mainWindowController.consoleTextFlow);
            FontSizeConfig.setConsoleFontSize(FontSizeConfig.getConsoleFontSize() + FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[FONT SIZE]: Maximum font size for console reached", Color.RED, true);
        }
    }

    public void decreaseConsoleFontSize() {
        if (FontSizeConfig.getConsoleFontSize() > FontSizeConfig.getConsoleMinFontSize()) {
            FontHelper.decreaseFontSize(FontSizeConfig.getFontStep(), mainWindowController.consoleTextFlow);
            FontSizeConfig.setConsoleFontSize(FontSizeConfig.getConsoleFontSize() - FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.consoleTextFlow, "\n[FONT SIZE]: Minimum font size for console reached", Color.RED, true);
        }
    }

    public void increaseEditorFontSize() {
        CodeAreaHelper.increaseCodeAreaFontSize(mainWindowController.consoleTextFlow, mainWindowController.codeArea);
    }

    public void decreaseEditorFontSize() {
        CodeAreaHelper.decreaseCodeAreaFontSize(mainWindowController.consoleTextFlow, mainWindowController.codeArea);
    }

    //endregion

    @FXML
    private void OKSettings() {
        applySettings();
        WindowHelper.hideWindow(Window.SETTINGS_WINDOW);
    }

    @FXML
    private void applySettings() {
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
    private void selectFontOptions() {
        clearSelection();
        fontHBox.getStyleClass().add("selected");
    }

    @FXML
    private void selectAppearance() {
        clearSelection();
        appearanceHBox.getStyleClass().add("selected");
    }

    @FXML
    private void selectConsole() {
        clearSelection();
        consoleHBox.getStyleClass().add("selected");
    }

    @FXML
    private void selectCodeArea() {
        clearSelection();
        editorHBox.getStyleClass().add("selected");
    }

    private void clearSelection() {
        appearanceHBox.getStyleClass().remove("selected");
        consoleHBox.getStyleClass().remove("selected");
        editorHBox.getStyleClass().remove("selected");
        fontHBox.getStyleClass().remove("selected");
    }
}
