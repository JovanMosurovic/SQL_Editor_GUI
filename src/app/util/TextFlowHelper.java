package app.util;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Utility class for managing and updating a {@link TextFlow} component.
 * <p>It provides methods to update the text content, clear the text, and scroll to the bottom.</p>
 */
public class TextFlowHelper {

    /**
     * Updates the content of the given {@link TextFlow} component with the specified message and color.
     * The message is appended to the existing text content if the append flag is set to true.
     *
     * @param textFlow the {@link TextFlow} component to update
     * @param message  the message to display in the text flow
     * @param color    the color of the message text
     * @param append   true to append the message, false to replace the existing text
     */
    public static void updateResultTextFlow(TextFlow textFlow, String message, Color color, boolean append) {
        if (!append) {
            clearResultTextFlow(textFlow);
        }
        Text text = new Text(message);
        text.setFill(color);
        textFlow.getChildren().add(text);
        scrollToBottom(textFlow);
    }

    /**
     * Clears the content of the given {@link TextFlow} component.
     *
     * @param textFlow the {@link TextFlow} component to clear
     */
    public static void clearResultTextFlow(TextFlow textFlow) {
        textFlow.getChildren().clear();
    }

    /**
     * Scrolls the given {@link TextFlow} component to the bottom.
     *
     * @param textFlow the {@link TextFlow} component to scroll
     */
    private static void scrollToBottom(TextFlow textFlow) {
        Parent parent = textFlow.getParent();
        while (parent != null) {
            if (parent instanceof ScrollPane) {
                ScrollPane scrollPane = (ScrollPane) parent;
                // Use Platform.runLater to ensure the scroll happens after the layout is updated
                Platform.runLater(() -> {
                    scrollPane.layout();
                    scrollPane.setVvalue(1.0);
                });
                return;
            }
            parent = parent.getParent();
        }
    }

}
