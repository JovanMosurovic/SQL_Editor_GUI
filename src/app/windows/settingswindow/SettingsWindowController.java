package app.windows.settingswindow;

import app.ControllerBase;
import app.Window;
import app.windows.mainwindow.MainWindowController;
import app.util.FontHelper;
import app.util.FontConfig;
import javafx.animation.RotateTransition;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for the Settings Window in the application.
 * <p>This class handles the user interactions within the Settings Window,
 * such as changing appearance settings, font settings, and applying the changes.</p>
 *
 * Note: Appearance settings are not yet implemented in this version of the application.
 *
 * @see app.ControllerBase
 * @see app.Window
 */
public class SettingsWindowController extends ControllerBase {

    /**
     * FXML elements for the Settings Window UI.
     */
    @FXML
    private VBox fontOptionsVBox;
    @FXML
    private VBox appearanceOverviewVBox, fontOverviewVBox;
    @FXML
    private ImageView fontArrow;
    @FXML
    private HBox appearanceHBox, fontHBox;
    @FXML
    private VBox editorFontOptionsVBox, consoleFontOptionsVBox;
    @FXML
    private HBox editorHBox, consoleHBox;
    @FXML
    private ComboBox<String> themeComboBox, editorFontFamilyComboBox, consoleFontFamilyComboBox;
    @FXML
    private Spinner<Double> editorFontSizeSpinner, consoleFontSizeSpinner;
    @FXML
    private TextArea editorFontPreviewTextArea, consoleFontPreviewTextArea;

    /**
     * Flag to track the initial theme of the application.
     * <p>{@code true} if the initial theme is dark, {@code false} otherwise.</p>
     */
    private boolean initialTheme;
    /**
     * Flag to track the visibility of the font options.
     */
    private boolean fontOptionsVisible = false;
    /**
     * Flag to track if changes have been applied.
     */
    private boolean changesApplied = false;

    /**
     * Reference to the Main Window Controller.
     */
    private MainWindowController mainWindowController;

    /**
     * Temporary font size and font family settings for the editor and console.
     * This allows users to preview the changes before applying them.
     */
    private double tempEditorFontSize = FontConfig.getEditorFontSize();
    private double tempConsoleFontSize = FontConfig.getConsoleFontSize();
    private String tempEditorFontFamily = FontConfig.getEditorFontFamily();
    private String tempConsoleFontFamily = FontConfig.getConsoleFontFamily();

    /**
     * Initializes the controller and associates it with the Settings Window.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Window.getWindowAt(Window.SETTINGS_WINDOW).setController(this);
        mainWindowController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
        initialTheme = Window.isDarkTheme;
        setupThemeOptions();
        initAreas();
        setupFontOptions();
    }

    /**
     * Resets the state of the Settings Window.
     */
    public void resetState() {
        initialTheme = Window.isDarkTheme;
        changesApplied = false;
        updateThemeComboBox();
    }

    /**
     * Sets up the theme options for the user to customize the appearance.
     * Note: This feature is not yet implemented in this version of the application.
     */
    private void setupThemeOptions() {

        themeComboBox.getItems().addAll("Light", "Dark");
        themeComboBox.setValue(Window.isDarkTheme ? "Dark" : "Light");

        themeComboBox.setCellFactory(listView -> new ListCell<String>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitHeight(16);
                imageView.setFitWidth(16);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    if ("Light".equals(item)) {
                        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/light_theme_icon.png"))));
                    } else {
                        imageView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/dark_theme_icon.png"))));
                    }
                    setGraphic(imageView);
                }
            }
        });

        themeComboBox.setButtonCell(themeComboBox.getCellFactory().call(null));

        themeComboBox.setOnAction(event -> {
            String selectedTheme = themeComboBox.getValue();
            if ((selectedTheme.equals("Dark") && !Window.isDarkTheme) ||
                    (selectedTheme.equals("Light") && Window.isDarkTheme)) {
                Window.changeTheme();
                changesApplied = false;
            }
        });
    }

    /**
     * Initializes the font areas with the default font settings.
     */
    private void initAreas() {
        applyEditorSettings();
        applyConsoleSettings();
    }

    /**
     * Sets up the font options for the editor and console.
     * <p>This method populates the font family and font size options for the user to customize the appearance.</p>
     */
    private void setupFontOptions() {
        setupFontFamilyComboBox(editorFontFamilyComboBox, FontConfig.EDITOR_FONT_FAMILIES, FontConfig.getEditorFontFamily(), editorFontPreviewTextArea);
        setupFontFamilyComboBox(consoleFontFamilyComboBox, FontConfig.CONSOLE_FONT_FAMILIES, FontConfig.getConsoleFontFamily(), consoleFontPreviewTextArea);
        setupFontSizeSpinner(editorFontSizeSpinner, FontConfig.EDITOR_MIN_FONT_SIZE, FontConfig.EDITOR_MAX_FONT_SIZE, FontConfig.getEditorFontSize(), editorFontPreviewTextArea);
        setupFontSizeSpinner(consoleFontSizeSpinner, FontConfig.CONSOLE_MIN_FONT_SIZE, FontConfig.CONSOLE_MAX_FONT_SIZE, FontConfig.getConsoleFontSize(), consoleFontPreviewTextArea);
        setupPreviewTextAreas();
    }

    /**
     * Sets up the font family combo box with the specified font families and default family.
     *
     * @param comboBox     The combo box to set up.
     * @param fontFamilies The font families to populate the combo box.
     * @param defaultFamily The default font family to select.
     * @param previewArea  The preview area to update the font family.
     */
    private void setupFontFamilyComboBox(ComboBox<String> comboBox, String[] fontFamilies, String defaultFamily, TextArea previewArea) {
        comboBox.getItems().addAll(fontFamilies);
        comboBox.setValue(defaultFamily);
        comboBox.valueProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            updateTempFontFamily(comboBox, newValue);
            updatePreviewAreaFontFamily(previewArea, newValue);
        });
    }

    /**
     * Updates the temporary font family setting based on the selected value in the combo box.
     *
     * @param comboBox The combo box that triggered the event.
     * @param newValue The new value selected in the combo box.
     */
    private void updateTempFontFamily(ComboBox<String> comboBox, String newValue) {
        if (comboBox == editorFontFamilyComboBox) {
            tempEditorFontFamily = newValue;
        } else if (comboBox == consoleFontFamilyComboBox) {
            tempConsoleFontFamily = newValue;
        }
    }

    /**
     * Updates the preview area with the specified font family.
     *
     * @param previewArea The preview area to update.
     * @param fontFamily   The font family to apply.
     */
    private void updatePreviewAreaFontFamily(TextArea previewArea, String fontFamily) {
        previewArea.getStyleClass().removeAll("calibri-font", "monospaced-font", "consolas-font", "firacode-font", "jetbrainsmono-font");
        previewArea.getStyleClass().add(fontFamily.toLowerCase() + "-font");
    }

    /**
     * Sets up the font size spinner with the specified range and default value.
     *
     * @param spinner      The spinner to set up.
     * @param min          The minimum font size.
     * @param max          The maximum font size.
     * @param initial      The initial font size.
     * @param previewArea  The preview area to update the font size.
     */
    private void setupFontSizeSpinner(Spinner<Double> spinner, double min, double max, double initial, TextArea previewArea) {
        SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, FontConfig.FONT_STEP);
        spinner.setValueFactory(factory);
        spinner.valueProperty().addListener((ObservableValue<? extends Double> observable, Double oldValue, Double newValue) -> {
            previewArea.setStyle("-fx-font-size: " + newValue + "px;");
            updateTempFontSize(spinner, newValue);
        });
    }

    /**
     * Updates the temporary font size setting based on the selected value in the spinner.
     *
     * @param spinner  The spinner that triggered the event.
     * @param newValue The new value selected in the spinner.
     */
    private void updateTempFontSize(Spinner<Double> spinner, double newValue) {
        if (spinner == editorFontSizeSpinner) {
            tempEditorFontSize = newValue;
        } else if (spinner == consoleFontSizeSpinner) {
            tempConsoleFontSize = newValue;
        }
    }

    /**
     * Sets up the preview text areas with the default font settings.
     */
    private void setupPreviewTextAreas() {
        editorFontPreviewTextArea.getStyleClass().add(tempEditorFontFamily.toLowerCase() + "-font");
        consoleFontPreviewTextArea.getStyleClass().add(tempConsoleFontFamily.toLowerCase() + "-font");
        FontHelper.setFontSize((int) tempEditorFontSize, editorFontPreviewTextArea);
        FontHelper.setFontSize((int) tempConsoleFontSize, consoleFontPreviewTextArea);

        editorFontPreviewTextArea.setEditable(false);
        consoleFontPreviewTextArea.setEditable(false);
    }

    /**
     * Handles the action of applying the settings.
     * <p>This method applies the font settings to the editor and console areas and closes the Settings Window.</p>
     */
    @FXML
    private void OKSettings() {
        if(!changesApplied) {
            applySettings();
        }
        Window.hideWindow(Window.SETTINGS_WINDOW);
    }

    /**
     * Handles the action of applying the settings.
     * <p>This method applies the font settings to the editor and console areas.</p>
     */
    @FXML
    private void applySettings() {
        if (editorFontOptionsVBox.isVisible()) {
            applyEditorSettings();
        } else if (consoleFontOptionsVBox.isVisible()) {
            applyConsoleSettings();
        }
        changesApplied = true;
        initialTheme = Window.isDarkTheme;
    }

    /**
     * Applies the editor font settings to the editor area.
     */
    private void applyEditorSettings() {
        mainWindowController.editorArea.setId(tempEditorFontFamily.toLowerCase());
        FontHelper.setFontSize((int) tempEditorFontSize, mainWindowController.editorArea);
        FontConfig.updateFontSizeConfig(tempEditorFontSize, mainWindowController.editorArea);
        FontConfig.updateFontFamilyConfig(tempEditorFontFamily, mainWindowController.editorArea);
    }

    /**
     * Applies the console font settings to the console area.
     */
    private void applyConsoleSettings() {
        mainWindowController.consoleTextFlow.setId(tempConsoleFontFamily.toLowerCase());
        FontHelper.setFontSize((int) tempConsoleFontSize, mainWindowController.consoleTextFlow);
        FontConfig.updateFontSizeConfig(tempConsoleFontSize, mainWindowController.consoleTextFlow);
        FontConfig.updateFontFamilyConfig(tempConsoleFontFamily, mainWindowController.consoleTextFlow);

        // Resize all existing icons in consoleTextFlow
        for (Node node : mainWindowController.consoleTextFlow.getChildren()) {
            if (node instanceof TextFlow) {
                TextFlow textFlow = (TextFlow) node;
                for (Node child : textFlow.getChildren()) {
                    if (child instanceof SVGPath) {
                        SVGPath svgPath = (SVGPath) child;
                        double scale = tempConsoleFontSize / Math.max(svgPath.getBoundsInLocal().getWidth(), svgPath.getBoundsInLocal().getHeight());
                        svgPath.setScaleX(scale);
                        svgPath.setScaleY(scale);
                    }
                }
            }
        }
    }

    /**
     * Handles the action of canceling the settings.
     * <p>This method closes the Settings Window without applying the changes.</p>
     */
    @FXML
    private void cancelSettings() {
        if(!changesApplied && Window.isDarkTheme != initialTheme) {
            Window.changeTheme();
            updateThemeComboBox();
        }
        Window.hideWindow(Window.SETTINGS_WINDOW);
    }

    private void updateThemeComboBox() {
        themeComboBox.setValue(Window.isDarkTheme ? "Dark" : "Light");
    }

    /**
     * Toggles the visibility of the font options.
     */
    @FXML
    private void toggleFontOptions() {
        fontOptionsVisible = !fontOptionsVisible;
        updateFontOptionsVisibility();
        if(fontOptionsVisible) {
            hideAllOptionPanes();
            fontOverviewVBox.setVisible(true);
        }
    }

    /**
     * Updates the visibility of the font options based on the current state.
     */
    private void updateFontOptionsVisibility() {
        animateFontArrow();
        fontOptionsVBox.setVisible(fontOptionsVisible);
        fontOptionsVBox.setManaged(fontOptionsVisible);
        if (fontOptionsVisible) {
            selectFontOptions();
        } else {
            clearSelection();
            hideAllOptionPanes();
        }
    }

    /**
     * Animates the font arrow based on the visibility of the font options.
     */
    private void animateFontArrow() {
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(200), fontArrow);
        rotateTransition.setByAngle(fontOptionsVisible ? 90 : -90);
        rotateTransition.play();
        fontArrow.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/app/resources/icons/arrow_right_icon.png"))));
    }

    /**
     * Selects the appearance settings.
     * Note: This is not implemented yet.
     */
    @FXML
    private void selectAppearance() {
        setSelectedOption(appearanceHBox);
        hideAllOptionPanes();
        appearanceOverviewVBox.setVisible(true);
    }

    /**
     * Selects the font settings.
     */
    @FXML
    private void selectFontOptions() {
        setSelectedOption(fontHBox);
        hideAllOptionPanes();
        fontOverviewVBox.setVisible(true);
    }

    /**
     * Selects the editor font settings.
     */
    @FXML
    private void selectEditor() {
        setSelectedOption(editorHBox);
        hideAllOptionPanes();
        editorFontOptionsVBox.setVisible(true);
    }

    /**
     * Selects the console font settings.
     */
    @FXML
    private void selectConsole() {
        setSelectedOption(consoleHBox);
        hideAllOptionPanes();
        consoleFontOptionsVBox.setVisible(true);
    }

    /**
     * Sets the selected option based on the specified HBox.
     *
     * @param selectedBox The HBox representing the selected option.
     */
    private void setSelectedOption(HBox selectedBox) {
        clearSelection();
        selectedBox.getStyleClass().add("selected");
    }

    /**
     * Clears the selection style from all HBox elements.
     */
    private void clearSelection() {
        appearanceHBox.getStyleClass().remove("selected");
        consoleHBox.getStyleClass().remove("selected");
        editorHBox.getStyleClass().remove("selected");
        fontHBox.getStyleClass().remove("selected");
    }

    /**
     * Hides all option panes.
     */
    private void hideAllOptionPanes() {
        appearanceOverviewVBox.setVisible(false);
        fontOverviewVBox.setVisible(false);
        editorFontOptionsVBox.setVisible(false);
        consoleFontOptionsVBox.setVisible(false);
    }

}
