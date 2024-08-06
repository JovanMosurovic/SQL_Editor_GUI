package app.util;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextFlowHelper {

    public static void updateResultTextFlow(TextFlow textFlow, String message, Color color, boolean append) {
        if (!append) {
            clearResultTextFlow(textFlow);
        }
        Text text = new Text(message);
        text.setFill(color);
        textFlow.getChildren().add(text);
        scrollToBottom(textFlow);
    }

    private static void scrollToBottom(TextFlow textFlow) {
        Parent parent = textFlow.getParent();
        while (parent != null) {
            if (parent instanceof ScrollPane) {
                ScrollPane scrollPane = (ScrollPane) parent;
                scrollPane.layout();
                scrollPane.setVvalue(1.0);
                return;
            }
            parent = parent.getParent();
        }
    }

    public static void clearResultTextFlow(TextFlow textFlow) {
        textFlow.getChildren().clear();
    }

}
