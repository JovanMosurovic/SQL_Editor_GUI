package cpp;

public class JavaInterface {
    static JavaInterface instance = null;

    public static JavaInterface getInstance() {
        if (instance == null) {
            instance = new JavaInterface();
        }
        return instance;
    }
    public native void createNewDatabase();
    public native void executeQuery(String query);
    public native void importDatabase(String file_path);
    public native void exportDatabase(String formatStr, String file_path);
}
