package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.connection;
import static eParafia.Controller.Dane.replaceSceneContent;

public class BasicParafie {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<BasicParafieRow> basicParafie;

    @FXML
    private TableColumn<BasicParafieRow, Integer> id_pariafiiColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> nazwaColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> zakonColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> miastoColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> ulicaColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> nr_domuColumn;

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    ObservableList<BasicParafieRow> parafieRows= FXCollections.observableArrayList();

     void insertParafie(){
         try {
             Statement stmt = connection.createStatement();
             String query=
                     "SELECT \n" +
                             "p.id_parafii AS \"id_parafii\",\n" +
                             "p.nazwa AS \"nazwa\",\n" +
                             "z.nazwa AS \"zakon\",\n" +
                             "a.miasto AS \"miasto\",\n" +
                             "a.ulica AS \"ulica\",\n" +
                             "a.nr_domu AS \"nr_domu\"\n" +
                             "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                             "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu";
             ResultSet rs=stmt.executeQuery(query);

             while (rs.next()){
                 parafieRows.add(new BasicParafieRow(
                         rs.getInt("id_parafii"),
                         rs.getString("nazwa"),
                         rs.getString("zakon"),
                         rs.getString("miasto"),
                         rs.getString("ulica"),
                         rs.getString("nr_domu")
                 ));
             }
             id_pariafiiColumn.setCellValueFactory(new PropertyValueFactory<>("id_parafii"));
             nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
             zakonColumn.setCellValueFactory(new PropertyValueFactory<>("zakon"));
             miastoColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));
             ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));
             nr_domuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));

             basicParafie.setItems(parafieRows);
         }
         catch (Exception e){
             e.printStackTrace();
             System.out.println("Nie powiodło się");
         }
     }

    //MENU

    @FXML
    void goToMenu(ActionEvent event) {
        try {
            replaceSceneContent("FXML/mainMenu.fxml");
        }catch (Exception e){}
    }

    @FXML
    void wyloguj(ActionEvent event) {
        System.out.println("WYLOGUJ");
        try {
            connection.close();
            replaceSceneContent("FXML/login.fxml");
        }catch (Exception e){}
    }
    ////

    @FXML
    void initialize() {
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert id_pariafiiColumn != null : "fx:id=\"id_pariafiiColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert nazwaColumn != null : "fx:id=\"nazwaColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert zakonColumn != null : "fx:id=\"zakonColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicParafie.fxml'.";

        insertParafie();
    }
}
