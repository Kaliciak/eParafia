package eParafia.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import static eParafia.Controller.Dane.*;

public class MainMenu {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    void goToParafie(){
        try {
            replaceSceneContent("FXML/basicParafie.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void goToParafianie(){
        try {
            replaceSceneContent("FXML/basicParafianie.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    @FXML
    void goToSakramenty(ActionEvent event) {
        try {
            replaceSceneContent("FXML/sakramenty.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    @FXML
    void goToPracownicy(ActionEvent event){
        try {
            replaceSceneContent("FXML/basicPracownicy.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    @FXML
    void goToWydarzenia(ActionEvent event){
        try {
            replaceSceneContent("FXML/basicWydarzenia.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    //MENU

    @FXML
    void goToMenu(ActionEvent event) {
        try {
            replaceSceneContent("FXML/mainMenu.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void wyloguj(ActionEvent event) {
        System.out.println("WYLOGUJ");
        try {
            connection.close();
            replaceSceneContent("FXML/login.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    ////

    @FXML
    void initialize() {
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'mainMenu.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'mainMenu.fxml'.";

    }
}
