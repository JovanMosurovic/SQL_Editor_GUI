package app.util;

import javafx.scene.Node;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FontHelper {

    private static final Logger logger = Logger.getLogger(FontHelper.class.getName());

    public static void increaseFontSize(double fontSizeStep, Node... nodes) {
        for (Node node : nodes) {
            double currentFontSize = getCurrentFontSize(node);
            logger.log(Level.INFO, "Increasing font size for node: " + node.getClass().getName() + " from " + currentFontSize + " to " + (currentFontSize + fontSizeStep));
            currentFontSize += fontSizeStep;
            changeFontSize(currentFontSize, node);
        }
    }

    public static void decreaseFontSize(double fontSizeStep, Node... nodes) {
        for (Node node : nodes) {
            double currentFontSize = getCurrentFontSize(node);
            logger.log(Level.INFO, "Decreasing font size for node: " + node.getClass().getName() + " from " + currentFontSize + " to " + (currentFontSize - fontSizeStep));
            currentFontSize -= fontSizeStep;
            changeFontSize(currentFontSize, node);
        }
    }

    private static void changeFontSize(double newFontSize, Node node) {
        try {
            node.setStyle("-fx-font-size: " + newFontSize + "px;");
            logger.log(Level.INFO, "Font size changed for node: " + node.getClass().getName() + " to " + newFontSize);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while changing font size", e);
        }
    }

    private static double getCurrentFontSize(Node node) {
        try {
            String style = node.getStyle();
            String[] styles = style.split(";");
            for (String s : styles) {
                if (s.trim().startsWith("-fx-font-size")) {
                    String[] parts = s.split(":");
                    return Double.parseDouble(parts[1].replace("px", "").trim());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while getting current font size", e);
        }
        return FontSizeConfig.getDefaultFontSize();
    }


}
