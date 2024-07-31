package app.mainwindow;

import app.ControllerBase;
import app.Window;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController extends ControllerBase {


    public Button runButton;
    public ImageView runIcon;
    public ListView tablesListView;
    public TextArea codeArea;
    public Label resultLabel;
    public TableView resultTableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        codeArea.setFont(Font.font("JetBrains Mono NL", 16));
        Window.getWindowAt(Window.MAIN_WINDOW).setController(this);
    }
}
