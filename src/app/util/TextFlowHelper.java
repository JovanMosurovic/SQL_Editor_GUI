package app.util;

import javafx.application.Platform;
import javafx.scene.Node;
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

    public static void addWarningMessage(TextFlow consoleTextFlow, String warningText) {
        double iconSize = FontConfig.getConsoleFontSize() - 2;
        SVGHelper.SVGIcon warningIcon = SVGHelper.createWarningIcon(iconSize);
        updateResultTextFlow(consoleTextFlow, "\n", Color.TRANSPARENT, true);
        TextFlow messageLine = createMessageLine("Warning", warningText, warningIcon, AppColors.WARNING_YELLOW, false);

        addToConsole(consoleTextFlow, messageLine);
    }

    public static void addExecutionTime(TextFlow consoleTextFlow, long executionTime) {
        double iconSize = FontConfig.getConsoleFontSize() - 2;
        SVGHelper.SVGIcon timeIcon = SVGHelper.createTimeIcon(iconSize);

        String executionTimeText = String.format("%.2f ms", (double) executionTime / 1000000);
        TextFlow messageLine = createMessageLine("Execution time", executionTimeText, timeIcon, Color.BLACK, true);

        addToConsole(consoleTextFlow, messageLine);
        updateResultTextFlow(consoleTextFlow, "\n", Color.TRANSPARENT, true);
    }

    private static TextFlow createMessageLine(String label, String messageText, SVGHelper.SVGIcon icon, Color labelColor, boolean underlineLabel) {
        Text labelText = new Text(label);
        labelText.setStyle("-fx-font-weight: bold;" + (underlineLabel ? " -fx-underline: true;" : ""));
        labelText.setFill(labelColor);

        Text colonText = new Text(":");
        colonText.setStyle("-fx-font-weight: bold;");
        colonText.setFill(labelColor);

        Text messageTextNode = new Text(" " + messageText);
        messageTextNode.setFill(Color.BLACK);

        TextFlow messageLine = new TextFlow();
        messageLine.getChildren().addAll(icon.getNode(), new Text(" "), labelText, colonText, messageTextNode);
        icon.getNode().setTranslateY(3); // for alignment with the text

        return messageLine;
    }

    private static void addToConsole(TextFlow consoleTextFlow, Node content) {
        Text spacer = new Text("\n");
        spacer.setStyle("-fx-font-size: 10px;");
        consoleTextFlow.getChildren().addAll(spacer, content);
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
