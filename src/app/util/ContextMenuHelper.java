package app.util;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.text.TextFlow;

public class ContextMenuHelper {

    public static ContextMenu createConsoleContextMenu(TextFlow resultTextFlow) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem clearConsoleItem = new MenuItem("Clear console");
        clearConsoleItem.setOnAction(event -> TextFlowHelper.clearResultTextFlow(resultTextFlow));
        contextMenu.getItems().add(clearConsoleItem);
        return contextMenu;
    }
}
