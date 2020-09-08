import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args)
    {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        ViewManager manager= new ViewManager();
        stage = manager.getMainStage();
        stage.setTitle("Backgammon!");
        stage.show();
    }
}