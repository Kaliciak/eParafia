package eParafia.Controller;

import eParafia.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class Dane {
    public static Connection connection;
    public static Stage stage;
    public static Stage secondStage;

    public static Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(Main.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        if (scene == null) {
            System.out.println("NULL");
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }

    public void openSecondStage(String fxml) throws Exception{
        secondStage.setTitle("Podgląd");
        Parent r = FXMLLoader.load(getClass().getResource(fxml));
        secondStage.setScene(new Scene(r, 200, 100));
        secondStage.show();
    }
}
