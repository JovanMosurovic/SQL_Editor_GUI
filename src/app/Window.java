package app;

import javafx.stage.Stage;

public abstract class Window {

    public static final int WINDOWS;
    public static final int MAIN_WINDOW;
    public static final int CLOSE_WINDOW;
    private static Window[] windows;

    public static Window getWindowAt(int index) {
        return windows[index];
    }

    public static void setWindowAt(int index, Window window) {
        windows[index] = window;
    }

    static {
        WINDOWS = 2;
        MAIN_WINDOW = 0;
        CLOSE_WINDOW = 1;
        windows = new Window[WINDOWS];
    }

    protected Stage stage;
    protected ControllerBase controller;

    public abstract void init(Stage stage);


    //<editor-fold desc="GETTERS AND SETTERS">
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public ControllerBase getController() {
        return controller;
    }

    public void setController(ControllerBase controller) {
        this.controller = controller;
    }
    //</editor-fold>
}
