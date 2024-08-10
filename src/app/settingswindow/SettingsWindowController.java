package app.settingswindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.mainwindow.MainWindowController;
import app.util.CodeAreaHelper;
import app.util.FontHelper;
import app.util.FontSizeConfig;
import app.util.TextFlowHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsWindowController extends ControllerBase {
    @FXML
    private Spinner<Double> fontSizeSpinner;
    @FXML
    private ComboBox<String> fontFamilyComboBox;
    @FXML
    private TextArea fontPreviewTextArea;

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

    public void increaseEditorFontSize() {
        if (FontSizeConfig.getEditorFontSize() < FontSizeConfig.getEditorMaxFontSize()) {
            FontHelper.increaseFontSize(FontSizeConfig.getFontStep(),
                    mainWindowController.tablesLabel, mainWindowController.tablesListView,
                    mainWindowController.codeArea, mainWindowController.resultScrollPane,
                    mainWindowController.resultTextFlow, mainWindowController.resultTableView);
            FontSizeConfig.setEditorFontSize(FontSizeConfig.getEditorFontSize() + FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.resultTextFlow, "\n[FONT SIZE]: Maximum font size for editor reached", Color.RED, true);
        }
    }

    public void decreaseEditorFontSize() {
        if (FontSizeConfig.getEditorFontSize() > FontSizeConfig.getEditorMinFontSize()) {
            FontHelper.decreaseFontSize(FontSizeConfig.getFontStep(),
                    mainWindowController.tablesLabel, mainWindowController.tablesListView,
                    mainWindowController.codeArea, mainWindowController.resultScrollPane,
                    mainWindowController.resultTextFlow, mainWindowController.resultTableView);
            FontSizeConfig.setEditorFontSize(FontSizeConfig.getEditorFontSize() - FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.resultTextFlow, "\n[FONT SIZE]: Minimum font size for editor reached", Color.RED, true);
        }
    }

    public void increaseConsoleFontSize() {
        if (FontSizeConfig.getConsoleFontSize() < FontSizeConfig.getConsoleMaxFontSize()) {
            FontHelper.increaseFontSize(FontSizeConfig.getFontStep(), mainWindowController.resultTextFlow);
            FontSizeConfig.setConsoleFontSize(FontSizeConfig.getConsoleFontSize() + FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.resultTextFlow, "\n[FONT SIZE]: Maximum font size for console reached", Color.RED, true);
        }
    }

    public void decreaseConsoleFontSize() {
        if (FontSizeConfig.getConsoleFontSize() > FontSizeConfig.getConsoleMinFontSize()) {
            FontHelper.decreaseFontSize(FontSizeConfig.getFontStep(), mainWindowController.resultTextFlow);
            FontSizeConfig.setConsoleFontSize(FontSizeConfig.getConsoleFontSize() - FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.resultTextFlow, "\n[FONT SIZE]: Minimum font size for console reached", Color.RED, true);
        }
    }

    public void increaseCodeAreaFontSize() {
        CodeAreaHelper.increaseCodeAreaFontSize(mainWindowController.resultTextFlow, mainWindowController.codeArea);
    }

    public void decreaseCodeAreaFontSize() {
        CodeAreaHelper.decreaseCodeAreaFontSize(mainWindowController.resultTextFlow, mainWindowController.codeArea);
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
}
