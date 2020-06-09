package eParafia.Controller;

import eParafia.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    public static void openSecondStage(String fxml) throws Exception{
        secondStage.setTitle("Podgląd");
        Parent r = FXMLLoader.load(Main.class.getResource(fxml));
        secondStage.setScene(new Scene(r, 1000, 600));
        secondStage.show();
    }

    public static void showErrorWindow(Exception e){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e.getMessage());
        alert.setTitle("BŁĄD");
        alert.setHeaderText("Wystapił błąd");
        alert.setResizable(true);
        alert.showAndWait();
    }
    public static void showErrorWindow(String e){
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setContentText(e);
        alert.setTitle("BŁĄD");
        alert.setHeaderText("Wystapił błąd");
        alert.setResizable(true);
        alert.showAndWait();
    }
    public static void logout(){
        try {
            connection.close();
            replaceSceneContent("FXML/login.fxml");
            secondStage.close();
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
}
