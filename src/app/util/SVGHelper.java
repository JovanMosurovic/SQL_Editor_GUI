package app.util;

import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

public class SVGHelper {
    public static Node loadSVG(String svgContent, double size) {
        SVGPath path = new SVGPath();
        path.setContent(svgContent);

        // Scaling SVG
        double scale = size / Math.max(path.getBoundsInLocal().getWidth(), path.getBoundsInLocal().getHeight());
        path.setScaleX(scale);
        path.setScaleY(scale);

        return path;
    }
}