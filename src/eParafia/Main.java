package eParafia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static eParafia.Controller.Dane.*;
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("FXML/login.fxml"));
        primaryStage.setTitle("eParafia");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
        stage=primaryStage;

        secondStage=new Stage();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
