package app;

public class WindowHelper {

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

}
