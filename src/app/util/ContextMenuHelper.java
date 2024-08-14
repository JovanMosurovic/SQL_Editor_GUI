package app.util;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.text.TextFlow;
import org.fxmisc.richtext.CodeArea;

public class ContextMenuHelper {

    public static ContextMenu createConsoleContextMenu(TextFlow resultTextFlow) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem clearConsoleItem = new MenuItem("Clear console");
        clearConsoleItem.setOnAction(event -> TextFlowHelper.clearResultTextFlow(resultTextFlow));
        contextMenu.getItems().add(clearConsoleItem);
        return contextMenu;
    }

    public static ContextMenu createTableListViewContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem refreshTablesItem = new MenuItem("Show tables");
        MenuItem dropTableItem = new MenuItem("Drop table");
        contextMenu.getItems().addAll(refreshTablesItem, dropTableItem);
        return contextMenu;
    }

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
