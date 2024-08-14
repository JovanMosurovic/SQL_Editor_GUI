package app.util;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for managing font settings in this JavaFX application.
 * Provides methods to change the font size and family of JavaFX nodes.
 */
public class FontHelper {

    /**
     * The logger for this class.
     */
    private static final Logger logger = Logger.getLogger(FontHelper.class.getName());

    /**
     * Increases the font size of the specified nodes by the given step value.
     *
     * @param fontSizeStep the step value to increase the font size by
     * @param nodes the JavaFX {@link Node} objects to increase the font size for
     */
    public static void increaseFontSize(double fontSizeStep, Node... nodes) {
        for (Node node : nodes) {
            double currentFontSize = getCurrentFontSize(node);
            logger.log(Level.INFO, "Increasing font size for node: " + node.getClass().getName() + " from " + currentFontSize + " to " + (currentFontSize + fontSizeStep));
            currentFontSize += fontSizeStep;
            changeFontSize(currentFontSize, node);
            updateFontSizeConfig(currentFontSize, node);
        }
    }

    /**
     * Decreases the font size of the specified nodes by the given step value.
     *
     * @param fontSizeStep the step value to decrease the font size by
     * @param nodes the JavaFX {@link Node} objects to decrease the font size for
     */
    public static void decreaseFontSize(double fontSizeStep, Node... nodes) {
        for (Node node : nodes) {
            double currentFontSize = getCurrentFontSize(node);
            logger.log(Level.INFO, "Decreasing font size for node: " + node.getClass().getName() + " from " + currentFontSize + " to " + (currentFontSize - fontSizeStep));
            currentFontSize -= fontSizeStep;
            changeFontSize(currentFontSize, node);
            updateFontSizeConfig(currentFontSize, node);
        }
    }

    /**
     * Changes the font size of the specified node to the given value.
     *
     * @param newFontSize the new font size to set for the nodes
     * @param node the JavaFX {@link Node} object to change the font size for
     */
    private static void changeFontSize(double newFontSize, Node node) {
        try {
            node.setStyle("-fx-font-size: " + newFontSize + "px;");
            logger.log(Level.INFO, "Font size changed for node: " + node.getClass().getName() + " to " + newFontSize);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while changing font size", e);
        }
    }

    /**
     * Retrieves the current font size of the specified node.
     *
     * @param node the JavaFX {@link Node} object to get the font size for
     * @return the current font size of the node
     */
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
        return FontConfig.DEFAULT_FONT_SIZE;
    }

    /**
     * Sets the font size of the specified nodes to the given value.
     *
     * @param newFontSize the new font size to set for the nodes
     * @param nodes the JavaFX {@link Node} objects to set the font size for
     */
    public static void setFontSize(double newFontSize, Node... nodes) {
        for (Node node : nodes) {
            logger.log(Level.INFO, "Setting font size for node: " + node.getClass().getName() + " to " + newFontSize);
            changeFontSize(newFontSize, node);
            updateFontSizeConfig(newFontSize, node);
        }
    }

    /**
     * Retrieves the current font family of the specified node.
     *
     * @param node the JavaFX {@link Node} object to get the font family for
     * @return the current font family of the node
     */
    public static String getCurrentFontFamily(Node node) {
        try {
            String style = node.getStyle();
            String[] styles = style.split(";");
            for (String s : styles) {
                if (s.trim().startsWith("-fx-font-family")) {
                    String[] parts = s.split(":");
                    return parts[1].trim();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while getting font family", e);
        }
        return FontConfig.DEFAULT_FONT_FAMILY;
    }

    /**
     * Sets the font family of the specified nodes to the given value.
     *
     * @param fontFamily the new font family to set for the nodes
     * @param nodes the JavaFX {@link Node} objects to set the font family for
     */
    public static void setFontFamily(String fontFamily, Node... nodes) {
        for (Node node : nodes) {
            logger.log(Level.INFO, "Setting font family for node: " + node.getClass().getName() + " to " + fontFamily);
            node.setStyle("-fx-font-family: " + fontFamily + ";");
        }
    }

    /**
     * Updates the font size configuration based on the specified node.
     *
     * @param newFontSize the new font size to set for the nodes
     * @param node the JavaFX {@link Node} object to update the font size for
     */
    private static void updateFontSizeConfig(double newFontSize, Node node) {
        if (node instanceof TextArea) {
            FontConfig.setEditorFontSize(newFontSize);
        } else if (node instanceof TextFlow) {
            FontConfig.setConsoleFontSize(newFontSize);
        }
    }

}
