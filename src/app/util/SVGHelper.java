package app.util;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * Utility class for creating SVG icons.
 */
public class SVGHelper {
    /**
     * SVGIcon class for creating SVG icons.
     */
    public static class SVGIcon {
        /**
         * SVGPath object for the icon.
         */
        private final SVGPath path;

        /**
         * Constructor for SVGIcon.
         *
         * @param svgContent SVG content of the icon.
         * @param size       Size of the icon.
         * @param color      Color of the icon.
         */
        private SVGIcon(String svgContent, double size, Color color) {
            this.path = (SVGPath) loadSVG(svgContent, size);
            if (color != null) {
                this.path.setFill(color);
            }
        }

        /**
         * Returns the node of the icon.
         *
         * @return Node of the icon.
         */
        public Node getNode() {
            return path;
        }

        /**
         * Sets the color of the icon.
         *
         * @param color Color to set.
         */
        public void setColor(Color color) {
            path.setFill(color);
        }
    }

    /**
     * Loads an SVG icon.
     *
     * @param svgContent SVG content of the icon.
     * @param size      Size of the icon.
     * @return Node of the icon.
     */
    public static Node loadSVG(String svgContent, double size) {
        SVGPath path = new SVGPath();
        path.setContent(svgContent);

        // Scaling SVG
        double scale = size / Math.max(path.getBoundsInLocal().getWidth(), path.getBoundsInLocal().getHeight());
        path.setScaleX(scale);
        path.setScaleY(scale);

        return path;
    }

    /**
     * Creates a warning icon.
     *
     * @param size Size of the icon.
     * @return SVGIcon of the success icon.
     */
    public static SVGIcon createWarningIcon(double size) {
        String warningIconSVG = "M7.33,8.67V4.67c0-0.37,0.3-0.67,0.67-0.67s0.67,0.3,0.67,0.67v4c0,0.37-0.3,0.67-0.67,0.67S7.33,9.03,7.33,8.67z M8,10c-0.55,0-1,0.45-1,1s0.45,1,1,1s1-0.45,1-1S8.55,10,8,10z M15.72,13.25c-0.47,0.9-1.45,1.41-2.67,1.41H2.96c-1.23,0-2.21-0.51-2.67-1.41C-0.18,12.34-0.05,11.18,0.62,10.21l5.36-8.45C6.45,1.08,7.22,0.67,8.02,0.67s1.55,0.39,2,1.05l5.39,8.51C16.09,11.21,16.21,12.37,15.72,13.25z M14.28,10.97c-0.01-0.01-0.01-0.01-0.01-0.03L8.89,2.45C8.7,2.17,8.37,2,8,2S7.3,2.17,7.09,2.47L1.73,10.95c-0.41,0.59-0.51,1.23-0.27,1.69c0.23,0.45,0.77,0.7,1.49,0.7h10.08c0.73,0,1.26-0.25,1.49-0.7C14.76,12.2,14.67,11.56,14.28,10.97z";
        return new SVGIcon(warningIconSVG, size, AppColors.WARNING_YELLOW);
    }

    /**
     * Creates a time icon.
     *
     * @param size Size of the icon.
     * @return SVGIcon of the success icon.
     */
    public static SVGIcon createTimeIcon(double size) {
        String timeIconSVG = "M8,16C3.589,16,0,12.411,0,8S3.589,0,8,0s8,3.589,8,8-3.589,8-8,8Zm0-14.667C4.324,1.333,1.333,4.324,1.333,8s2.991,6.667,6.667,6.667S14.667,11.676,14.667,8S11.676,1.333,8,1.333ZM11.333,8c0-.368-.298-.667-.667-.667H9.333V4c0-.368-.298-.667-.667-.667S8,3.632,8,4v4c0,.368,.298,.667,.667,.667h2.667c.368,0,.667-.298,.667-.667Z";
        return new SVGIcon(timeIconSVG, size, null);
    }
}