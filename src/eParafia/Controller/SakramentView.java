package eParafia.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import javax.xml.crypto.Data;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static eParafia.Controller.Dane.*;

public class SakramentView {
    static SakramentyRow currentSakrament;
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
    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private Text sakrName;

    @FXML
    private Text dataName;

    @FXML
    private Text parafiaName;

    @FXML
    private TableView<?> participants;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> imieColumn;

    @FXML
    private TableColumn<?, ?> nazwiskoColumn;

    @FXML
    private TableColumn<?, ?> uczestnictwoColumn;

    @FXML
    private Text szafarzName;

    @FXML
    private Text szafarzSurname;

    @FXML
    void add(MouseEvent event) {

    }

    @FXML
    void edit(ActionEvent event) {

    }

    @FXML
    void returnToSakramenty(ActionEvent event) {
        try{
            replaceSceneContent("FXML/sakramenty.fxml");
        } catch (Exception e) {}
    }
    @FXML
    void initialize() {
        if(currentSakrament!=null) {
            sakrName.setText("Sakrament: "+currentSakrament.getSakrament());
            dataName.setText("Data: "+currentSakrament.getData().toString());
            parafiaName.setText("Parafia: "+currentSakrament.getParafiaName());
            try{

                Statement stmt = connection.createStatement();
                String query=
                        "select * from parafianie where id_osoby="+currentSakrament.getId_szafarza();
                ResultSet rs=stmt.executeQuery(query);
                if(rs.next()) {
                    szafarzName.setText("Imię: "+rs.getString("imie"));
                    szafarzSurname.setText("Nazwisko: "+rs.getString("nazwisko"));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
