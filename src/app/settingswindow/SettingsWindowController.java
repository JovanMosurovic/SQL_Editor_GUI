package app.settingswindow;

import app.ControllerBase;
import app.Window;
import app.WindowHelper;
import app.mainwindow.MainWindowController;
import app.util.CodeAreaHelper;
import app.util.FontHelper;
import app.util.FontSizeConfig;
import app.util.TextFlowHelper;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsWindowController extends ControllerBase {

    private final MainWindowController mainWindowController;

    public ComboBox<String> fontFamilyComboBox;
    public TextField fontSizeTextField;
    public TextArea fontPreviewTextArea;

    public SettingsWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SETTINGS_WINDOW).setController(this);
    }

    //region Font configuration

    public void increaseEditorFontSize() {
        if(FontSizeConfig.getEditorFontSize() < FontSizeConfig.getEditorMaxFontSize()) {
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
        if(FontSizeConfig.getEditorFontSize() > FontSizeConfig.getEditorMinFontSize()) {
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
        if(FontSizeConfig.getConsoleFontSize() < FontSizeConfig.getConsoleMaxFontSize()) {
            FontHelper.increaseFontSize(FontSizeConfig.getFontStep(), mainWindowController.resultTextFlow);
            FontSizeConfig.setConsoleFontSize(FontSizeConfig.getConsoleFontSize() + FontSizeConfig.getFontStep());
        } else {
            TextFlowHelper.updateResultTextFlow(mainWindowController.resultTextFlow, "\n[FONT SIZE]: Maximum font size for console reached", Color.RED, true);
        }
    }

    public void decreaseConsoleFontSize() {
        if(FontSizeConfig.getConsoleFontSize() > FontSizeConfig.getConsoleMinFontSize()) {
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

    public void applySettings() {
    }

    public void cancelSettings() {
        WindowHelper.hideWindow(Window.SETTINGS_WINDOW);
    }

    public void decreaseFontSize() {
    }

    public void increaseFontSize() {
    }

}
