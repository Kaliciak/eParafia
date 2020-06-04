package eParafia.Controller;

import java.net.URL;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static eParafia.Controller.Dane.*;

public class Login {

    @FXML
    private ResourceBundle resources;

    @FXML
    private Text addd;

    @FXML
    private URL location;

    @FXML
    private TextField adresSerwera;

    @FXML
    private TextField port;

    @FXML
    private TextField login;

    @FXML
    private PasswordField haslo;

    @FXML
    private Button log;

    @FXML
    private TextField nazwaBazy;

    @FXML
    void zaloguj(ActionEvent event) {
        String adr=adresSerwera.getText();
        String por=port.getText();
        String baz=nazwaBazy.getText();
        String log=login.getText();
        String has=haslo.getText();
        String formula="jdbc:postgresql://"+adr+":"+por+"/"+baz;
        try {
            connection = DriverManager.getConnection(formula, log, has);
            System.out.println("Polaczono");

            try{
                replaceSceneContent("FXML/mainMenu.fxml");
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("HERE");
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("Nie udalo sie polaczyc");
        }
    }

    @FXML
    void initialize() {
        assert adresSerwera != null : "fx:id=\"adresSerwera\" was not injected: check your FXML file 'login.fxml'.";
        assert port != null : "fx:id=\"port\" was not injected: check your FXML file 'login.fxml'.";
        assert login != null : "fx:id=\"login\" was not injected: check your FXML file 'login.fxml'.";
        assert haslo != null : "fx:id=\"haslo\" was not injected: check your FXML file 'login.fxml'.";
        assert log != null : "fx:id=\"log\" was not injected: check your FXML file 'login.fxml'.";
    }
}
