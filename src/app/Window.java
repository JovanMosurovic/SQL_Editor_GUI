package app;

import javafx.stage.Stage;

public abstract class Window {

    protected Stage stage;
    protected ControllerBase controller;
    public static final int WINDOWS;
    public static final int MAIN_WINDOW;
    public static final int CLOSE_WINDOW;
    public static final int SAVE_WINDOW;
    private static Window[] windows;

    static {
        WINDOWS = 2;
        MAIN_WINDOW = 0;
        CLOSE_WINDOW = 1;
        SAVE_WINDOW = 2;
        windows = new Window[WINDOWS];
    }

    public abstract void init(Stage stage);

    public static void initAllWindows() {
        for (Window window : windows) {
            window.init(new Stage());
        }
    }

    //<editor-fold desc="GETTERS AND SETTERS">

    public static Window getWindowAt(int index) {
        if(index < 0 || index >= WINDOWS) {
            throw new IllegalArgumentException("[GET WINDOW]: Invalid window position");
        }
        return windows[index];
    }

    public static void setWindowAt(int index, Window window) {
        if(index < 0 || index >= WINDOWS) {
            throw new IllegalArgumentException("[SET WINDOW]: Invalid window position");
        }
        windows[index] = window;
    }

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
