package app;

import javafx.stage.Stage;

public abstract class Window {

    public static final int WINDOWS;
    public static final int MAIN_WINDOW;
    public static final int CLOSE_WINDOW;
    public static final int SAVE_WINDOW;
    public static final int WELCOME_WINDOW;
    public static final int ABOUT_WINDOW;
    public static final int SETTINGS_WINDOW;

    private static final Window[] windows;

    protected Stage stage;
    protected ControllerBase controller;

    static {
        WINDOWS = 6;
        MAIN_WINDOW = 0;
        CLOSE_WINDOW = 1;
        SAVE_WINDOW = 2;
        WELCOME_WINDOW = 3;
        ABOUT_WINDOW = 4;
        SETTINGS_WINDOW = 5;
        windows = new Window[WINDOWS];
    }

    public abstract void init(Stage stage);

    public static void initAllWindows() {
        for (Window window : windows) {
            window.init(new Stage());
        }
    }

    public static void showWindow(int position) {
        if(position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[SHOW WINDOW]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().show();
    }

    public static void hideWindow(int position) {
        if(position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[HIDE WINDOW]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().hide();
    }

    public static void showOnly(int position) {
        if(position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[SHOW ONLY WINDOW]: Invalid window position");
        }
        for (int i = 0; i < Window.WINDOWS; i++) {
            if(i == position) {
                showWindow(position);
            } else {
                Window.getWindowAt(i).getStage().hide();
            }
        }
    }

    public static void centerOnScreen(int position) {
        if(position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[CENTER ON SCREEN]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().centerOnScreen();
    }

    public static void closeAllWindows() {
        for (int i = 0; i < Window.WINDOWS; i++) {
            Window.getWindowAt(i).getStage().close();
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
