package app.util;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.CodeArea;

/**
 * Utility class for creating and managing context menus in this JavaFX application.
 * Provides methods to create context menus for different components such as a console area,
 * a list view of tables, and a code editor area.
 */
public class ContextMenuHelper {

    /**
     * Creates a context menu for a console area with an option to clear the console.
     *
     * @param resultTextFlow the TextFlow component representing the console area
     * @return a {@link ContextMenu} with a "Clear console" menu item
     */
    public static ContextMenu createConsoleContextMenu(TextFlow resultTextFlow) {
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
    public static ContextMenu createTableListViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem refreshTablesItem = new MenuItem("Show tables");
        MenuItem dropTableItem = new MenuItem("Drop table");
        contextMenu.getItems().addAll(refreshTablesItem, dropTableItem);
        return contextMenu;
    }

    /**
     * Creates a context menu for a code editor area with various editing options
     * such as undo, redo, cut, copy, paste, select all, font size adjustments, and clearing the editor.
     *
     * @param editorArea the {@link CodeArea} component representing the code editor
     * @param consoleTextFlow the {@link TextFlow} component for displaying messages in the console
     * @return a {@link ContextMenu} with various editing and formatting options
     */
    public static ContextMenu createEditorAreaContextMenu(CodeArea editorArea, TextFlow consoleTextFlow) {
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
     * Sets up a context menu for the console area, displaying the menu on right-click.
     *
     * @param consoleTextFlow the {@link TextFlow} component representing the console area
     */
    public static void setupConsoleContextMenu(TextFlow consoleTextFlow) {
        ContextMenu consoleContextMenu = ContextMenuHelper.createConsoleContextMenu(consoleTextFlow);

        consoleTextFlow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                consoleContextMenu.show(consoleTextFlow, event.getScreenX(), event.getScreenY());
            } else {
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
        ContextMenu tablesContextMenu = ContextMenuHelper.createTableListViewContextMenu();

        tablesListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !tablesListView.getSelectionModel().isEmpty()) {
                tablesContextMenu.show(tablesListView, event.getScreenX(), event.getScreenY());
            } else {
                tablesContextMenu.hide();
            }
        });
    }

    /**
     * Sets up a context menu for the code editor area, providing various editing and formatting options
     * through a context menu that appears on right-click.
     *
     * @param editorArea the {@link CodeArea} component representing the code editor
     * @param consoleTextFlow the {@link TextFlow} component for displaying messages in the console
     */
    public static void setupEditorAreaContextMenu(CodeArea editorArea, TextFlow consoleTextFlow) {
        ContextMenu editorAreaContextMenu = ContextMenuHelper.createEditorAreaContextMenu(editorArea, consoleTextFlow);

        editorArea.setOnContextMenuRequested(event -> editorAreaContextMenu.show(editorArea, event.getScreenX(), event.getScreenY()));

        editorArea.setOnMousePressed(event -> {
            if (event.getButton() != MouseButton.SECONDARY) {
                editorAreaContextMenu.hide();
            }
        });
    }
}
