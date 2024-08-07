package app.util;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.text.Font;

import java.lang.reflect.Method;
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
            Method getFontMethod = node.getClass().getMethod("getFont");
            Font currentFont = (Font) getFontMethod.invoke(node);
            Font newFont = Font.font(currentFont.getFamily(), newFontSize);

            Method setFontMethod = node.getClass().getMethod("setFont", Font.class);
            setFontMethod.invoke(node, newFont);
            logger.log(Level.INFO, "Font size changed for node: " + node.getClass().getName() + " to " + newFontSize);
        } catch (NoSuchMethodException e) {
            // Node doesn't have getFont or setFont method, check children
            if (node instanceof Parent) {
                for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                    changeFontSize(newFontSize, child);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while changing font size", e);
        }
    }


    private static double getCurrentFontSize(Node node) {
        try {
            Method getFontMethod = node.getClass().getMethod("getFont");
            Font currentFont = (Font) getFontMethod.invoke(node);
            return currentFont.getSize();
        } catch (NoSuchMethodException e) {
            // Node doesn't have getFont method, check children
            if (node instanceof Parent) {
                for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
                    double fontSize = getCurrentFontSize(child);
                    if (fontSize != FontSizeConfig.getDefaultFontSize()) {
                        return fontSize;
                    }
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while getting current font size", e);
        }
        return FontSizeConfig.getDefaultFontSize();
    }


}
