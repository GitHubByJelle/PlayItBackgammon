import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewManager {

    private AnchorPane mainPane;
    private Scene mainScene;
    private Stage mainStage;

    public ViewManager(){
        mainPane= new AnchorPane();
        mainScene= new Scene(mainPane, Variables.SCREEN_WIDTH/2,Variables.SCREEN_HEIGHT/2);
        mainStage=new Stage();
        mainStage.setScene(mainScene);
    }

    public Stage getMainStage(){
        return mainStage;
    }

}
