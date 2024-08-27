package app.util;

import app.Window;
import app.mainwindow.MainWindowController;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.CodeArea;

import java.util.Objects;

/**
 * Utility class for creating and managing context menus in this JavaFX application.
 * <p>Provides methods to create context menus for different components such as a console area ({@link TextFlow}),
 * a list view of tables ({@link ListView}), and a code editor area ({@link CodeArea}.)</p>
 */
public class ContextMenuHelper {

    /**
     * Creates a context menu for a console area with an option to clear the console.
     *
     * @param resultTextFlow the TextFlow component representing the console area
     * @return a {@link ContextMenu} with a "Clear console" menu item
     */
    private static ContextMenu createConsoleContextMenu(TextFlow resultTextFlow) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem clearConsoleItem = new MenuItem("Clear console");
        clearConsoleItem.setOnAction(event -> TextFlowHelper.clearResultTextFlow(resultTextFlow));
        contextMenu.getItems().add(clearConsoleItem);
        return contextMenu;
    }

    /**
     * Creates a context menu for a list view of tables with options to refresh or drop a table.
     *
     * @return a {@link ContextMenu} with "Show tables" and "Drop table" menu items
     */
    private static ContextMenu createTableListViewContextMenu(ListView<String> tablesListView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem showTableItem = new MenuItem("Show table");
        MenuItem dropTableItem = new MenuItem("Drop table");

        showTableItem.setOnAction(event -> {
            String selectedTable = tablesListView.getSelectionModel().getSelectedItem();
            if (selectedTable != null) {
                MainWindowController controller = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
                controller.executeQueryFromContextMenu("SELECT * FROM " + selectedTable);
            }
        });

        dropTableItem.setOnAction(event -> {
            String selectedTable = tablesListView.getSelectionModel().getSelectedItem();
            if (selectedTable != null) {
                MainWindowController controller = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
                controller.executeQueryFromContextMenu("DROP TABLE " + selectedTable);
            }
        });

        contextMenu.getItems().addAll(showTableItem, dropTableItem);
        return contextMenu;
    }

    /**
     * Creates a context menu for a code editor area with various editing options
     * such as undo, redo, cut, copy, paste, select all, font size adjustments, and clearing the editor.
     *
     * @param editorArea      the {@link CodeArea} component representing the code editor
     * @param consoleTextFlow the {@link TextFlow} component for displaying messages in the console
     * @return a {@link ContextMenu} with various editing and formatting options
     */
    private static ContextMenu createEditorAreaContextMenu(CodeArea editorArea, TextFlow consoleTextFlow) {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::undo));

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::redo));

        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::cut));

        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::copy));

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::paste));

        MenuItem selectAllItem = new MenuItem("Select All");
        selectAllItem.setOnAction(event -> EditorHelper.handleEditAction(editorArea, CodeArea::selectAll));

        MenuItem increaseFontItem = new MenuItem("Increase Font Size");
        increaseFontItem.setOnAction(event -> EditorHelper.increaseEditorFontSize(consoleTextFlow, editorArea));

        MenuItem decreaseFontItem = new MenuItem("Decrease Font Size");
        decreaseFontItem.setOnAction(event -> EditorHelper.decreaseEditorFontSize(consoleTextFlow, editorArea));

        MenuItem clearEditorAreaItem = new MenuItem("Clear Editor Area");
        clearEditorAreaItem.setOnAction(event -> editorArea.clear());

        contextMenu.getItems().addAll(
                undoItem, redoItem,
                new SeparatorMenuItem(),
                cutItem, copyItem, pasteItem,
                new SeparatorMenuItem(),
                selectAllItem,
                new SeparatorMenuItem(),
                increaseFontItem, decreaseFontItem,
                new SeparatorMenuItem(),
                clearEditorAreaItem
        );

        return contextMenu;
    }

    /**
     * Creates a context menu for a tab in a tab pane with
     * options to close the tab or to close all tabs except the console.
     *
     * @param tabPane the {@link TabPane} component containing the tabs
     * @param tab     the {@link Tab} component for which the context menu is created
     * @return a {@link ContextMenu} with "Close Tab" and "Close All Tabs" menu items
     */
    public static ContextMenu createTabContextMenu(TabPane tabPane, Tab tab) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem closeTab = new MenuItem("Close Tab");
        MenuItem closeAllTabs = new MenuItem("Close All Tabs");

        closeTab.setOnAction(event -> closeTabIfNotConsole(tabPane, tab));
        closeAllTabs.setOnAction(event -> closeAllTabsExceptConsole(tabPane));

        if(tabPane.getTabs().indexOf(tab) == 0) {
            closeTab.setDisable(true);
        }

        contextMenu.getItems().addAll(closeTab, closeAllTabs);
        return contextMenu;
    }

    /**
     * Creates a context menu for the history table view with an option to copy the selected query to the editor.
     *
     * @param historyTableView the TableView component representing the history of queries
     * @return a {@link ContextMenu} with a "Copy to Editor" menu item
     */
    private static ContextMenu createHistoryContextMenu(TableView<HistoryEntry> historyTableView) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyToEditorItem = new MenuItem("Copy to Editor");
        copyToEditorItem.setOnAction(event -> copySelectedQueryToEditor(historyTableView));

        // Add icon
        ImageView copyIcon = new ImageView(new Image(Objects.requireNonNull(ContextMenuHelper.class.getResourceAsStream("/app/resources/icons/copy_icon.png"))));
        copyIcon.setFitWidth(16);
        copyIcon.setFitHeight(16);
        copyToEditorItem.setGraphic(copyIcon);

        contextMenu.getItems().add(copyToEditorItem);
        return contextMenu;
    }


    /**
     * Sets up a context menu for the console area, displaying the menu on right-click.
     *
     * @param consoleTextFlow the {@link TextFlow} component representing the console area
     */
    public static void setupConsoleContextMenu(TextFlow consoleTextFlow) {
        ContextMenu consoleContextMenu = ContextMenuHelper.createConsoleContextMenu(consoleTextFlow);

        consoleTextFlow.setOnContextMenuRequested(event ->
                showContextMenu(consoleContextMenu, consoleTextFlow, event));

        consoleTextFlow.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                consoleContextMenu.hide();
            }
        });
    }

    /**
     * Sets up a context menu for a list view of tables, displaying the menu on right-click if an item is selected.
     *
     * @param tablesListView the {@link ListView} component containing the list of tables
     */
    public static void setupTablesContextMenu(ListView<String> tablesListView) {
        ContextMenu tablesContextMenu = ContextMenuHelper.createTableListViewContextMenu(tablesListView);

        tablesListView.setOnContextMenuRequested(event -> {
            if (!tablesListView.getSelectionModel().isEmpty()) {
                showContextMenu(tablesContextMenu, tablesListView, event);
            }
        });

        tablesListView.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                tablesContextMenu.hide();
            }
        });
    }

    /**
     * Sets up a context menu for the code editor area, providing various editing and formatting options
     * through a context menu that appears on right-click.
     *
     * @param editorArea      the {@link CodeArea} component representing the code editor
     * @param consoleTextFlow the {@link TextFlow} component for displaying messages in the console
     */
    public static void setupEditorAreaContextMenu(CodeArea editorArea, TextFlow consoleTextFlow) {
        ContextMenu editorAreaContextMenu = ContextMenuHelper.createEditorAreaContextMenu(editorArea, consoleTextFlow);

        editorArea.setOnContextMenuRequested(event ->
                showContextMenu(editorAreaContextMenu, editorArea, event));

        editorArea.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                editorAreaContextMenu.hide();
            }
        });
    }

    /**
     * Sets up context menus for all tabs in a tab pane, providing options to close the tab or all tabs except the console.
     *
     * @param resultTabPane the {@link TabPane} component containing the tabs
     */
    public static void setupTabContextMenus(TabPane resultTabPane) {
        for (Tab tab : resultTabPane.getTabs()) {
            ContextMenu tabContextMenu = createTabContextMenu(resultTabPane, tab);
            tab.setContextMenu(tabContextMenu);

            if (tab.getGraphic() instanceof javafx.scene.layout.HBox) {
                javafx.scene.layout.HBox tabBox = (javafx.scene.layout.HBox) tab.getGraphic();
                tabBox.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.MIDDLE) {
                        closeTabIfNotConsole(resultTabPane, tab);
                    }
                });
            }
        }
    }

    /**
     * Sets up a context menu for the history table view, displaying the menu on right-click if an item is selected.
     *
     * @param historyTableView the {@link TableView} component containing the history of queries
     */
    public static void setupHistoryContextMenu(TableView<HistoryEntry> historyTableView) {
        ContextMenu historyContextMenu = createHistoryContextMenu(historyTableView);

        historyTableView.setContextMenu(historyContextMenu);

        historyTableView.setOnContextMenuRequested(event -> {
            if (historyTableView.getSelectionModel().getSelectedItem() != null) {
                showContextMenu(historyContextMenu, historyTableView, event);
            }
        });

        historyTableView.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                historyContextMenu.hide();
            }
        });
    }

    /**
     * Closes the given tab if it is not the console tab.
     *
     * @param tabPane the {@link TabPane} component containing the tabs
     * @param tab     the {@link Tab} component to close
     */
    public static void closeTabIfNotConsole(TabPane tabPane, Tab tab) {
        if (tabPane.getTabs().indexOf(tab) != 0) {
            tabPane.getTabs().remove(tab);
        }
    }

    /**
     * Closes all tabs in the tab pane except the console tab.
     *
     * @param tabPane the {@link TabPane} component containing the tabs
     */
    private static void closeAllTabsExceptConsole(TabPane tabPane) {
        tabPane.getTabs().removeIf(t -> tabPane.getTabs().indexOf(t) != 0);
    }

    /**
     * Displays the given context menu at the specified position.
     *
     * @param contextMenu the {@link ContextMenu} to show
     * @param anchor      the anchor {@link Node} for the context menu
     * @param event       the {@link ContextMenuEvent} that triggered the context menu
     */
    private static void showContextMenu(ContextMenu contextMenu, Node anchor, ContextMenuEvent event) {
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        Platform.runLater(() -> contextMenu.show(anchor, event.getScreenX(), event.getScreenY()));
        event.consume();
    }

    /**
     * Copies the selected query from the history table view to the editor.
     *
     * @param historyTableView the {@link TableView} component representing the history of queries
     */
    private static void copySelectedQueryToEditor(TableView<HistoryEntry> historyTableView) {
        HistoryEntry selectedEntry = historyTableView.getSelectionModel().getSelectedItem();
        if (selectedEntry != null) {
            MainWindowController mainController = (MainWindowController) Window.getWindowAt(Window.MAIN_WINDOW).getController();
            mainController.setEditorText(selectedEntry.getQuery());
        }
    }
}
