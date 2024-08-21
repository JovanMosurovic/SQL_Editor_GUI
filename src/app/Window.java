package app;

import javafx.stage.Stage;

/**
 * The base class for all windows in the application.
 * <p>It provides a common interface for initializing windows and showing/hiding them.</p>
 */
public abstract class Window {

    /**
     * The number of windows in the application.
     */
    public static final int WINDOWS;
    /**
     * The position of the main window in the application.
     */
    public static final int MAIN_WINDOW;
    /**
     * The position of the close window in the application.
     */
    public static final int CLOSE_WINDOW;
    /**
     * The position of the save window in the application.
     */
    public static final int SAVE_WINDOW;
    /**
     * The position of the welcome window in the application.
     */
    public static final int WELCOME_WINDOW;
    /**
     * The position of the about window in the application.
     */
    public static final int ABOUT_WINDOW;
    /**
     * The position of the settings window in the application.
     */
    public static final int SETTINGS_WINDOW;

    /**
     * The position of the force quit window in the application.
     */
    public static final int FORCE_QUIT_WINDOW;

    /**
     * The array of all windows in the application.
     */
    private static final Window[] windows;

    /**
     * The stage of the window.
     */
    protected Stage stage;
    /**
     * The controller of the window.
     */
    protected ControllerBase controller;

    static {
        WINDOWS = 7;
        MAIN_WINDOW = 0;
        CLOSE_WINDOW = 1;
        SAVE_WINDOW = 2;
        WELCOME_WINDOW = 3;
        ABOUT_WINDOW = 4;
        SETTINGS_WINDOW = 5;
        FORCE_QUIT_WINDOW = 6;

        windows = new Window[WINDOWS];
    }

    /**
     * Initializes the window with the specified stage.
     *
     * @param stage the {@link Stage} of the window
     */
    public abstract void init(Stage stage);

    /**
     * Initializes all windows in the application.
     */
    public static void initAllWindows() {
        for (Window window : windows) {
            window.init(new Stage());
        }
    }

    /**
     * Shows the window at the specified position.
     *
     * @param position the position of the window to show
     */
    public static void showWindow(int position) {
        if (position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[SHOW WINDOW]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().show();
    }

    /**
     * Hides the window at the specified position.
     *
     * @param position the position of the window to hide
     */
    public static void hideWindow(int position) {
        if (position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[HIDE WINDOW]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().hide();
    }

    /**
     * Shows only the window at the specified position and hides all other windows.
     *
     * @param position the position of the window to show
     */
    public static void showOnly(int position) {
        if (position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[SHOW ONLY WINDOW]: Invalid window position");
        }
        for (int i = 0; i < Window.WINDOWS; i++) {
            if (i == position) {
                showWindow(position);
            } else {
                Window.getWindowAt(i).getStage().hide();
            }
        }
    }

    /**
     * Centers the window at the specified position on the screen.
     *
     * @param position the position of the window to center
     */
    public static void centerOnScreen(int position) {
        if (position < 0 || position >= Window.WINDOWS) {
            throw new IllegalArgumentException("[CENTER ON SCREEN]: Invalid window position");
        }
        Window.getWindowAt(position).getStage().centerOnScreen();
    }

    /**
     * Closes all windows in the application.
     */
    public static void closeAllWindows() {
        for (int i = 0; i < Window.WINDOWS; i++) {
            Window.getWindowAt(i).getStage().close();
        }
    }

    //<editor-fold desc="GETTERS AND SETTERS">

    /**
     * Returns the window at the specified index.
     *
     * @param index the index of the window
     * @return the window at the specified index
     */
    public static Window getWindowAt(int index) {
        if (index < 0 || index >= WINDOWS) {
            throw new IllegalArgumentException("[GET WINDOW]: Invalid window position");
        }
        return windows[index];
    }

    /**
     * Sets the window at the specified index.
     *
     * @param index  the index of the window
     * @param window the window to set
     */
    public static void setWindowAt(int index, Window window) {
        if (index < 0 || index >= WINDOWS) {
            throw new IllegalArgumentException("[SET WINDOW]: Invalid window position");
        }
        windows[index] = window;
    }

    /**
     * Returns the stage of the window.
     *
     * @return the stage of the window
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Sets the stage of the window.
     *
     * @param stage the stage of the window
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Returns the controller of the window.
     *
     * @return the controller of the window
     */
    public ControllerBase getController() {
        return controller;
    }

    /**
     * Sets the controller of the window.
     *
     * @param controller the controller of the window
     */
    public void setController(ControllerBase controller) {
        this.controller = controller;
    }

    //</editor-fold>

}
