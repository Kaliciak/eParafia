package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import static eParafia.Controller.Dane.connection;
import static eParafia.Controller.Dane.showErrorWindow;

public class HistoriaParafian {

    ObservableList<HistoriaParafianRow> historiaRow= FXCollections.observableArrayList();
    public static ResultSet wyszukaneHistorie;
    String query;
    static String idPar;
    static String imiePar;
    static String nazwiskoPar;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text imie_i_nazwisko;

    @FXML
    private Text id_historyka;

    @FXML
    private TableView<HistoriaParafianRow> historiaParafian;

    @FXML
    private TableColumn<HistoriaParafianRow, Integer> id_pariafiiColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, String> nazwaColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, String> miastoColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, String> ulicaColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, String> nr_domuColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, Date> data_przybyciaColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, Date> data_odejsciaColumn;

    @FXML
    private TableColumn<HistoriaParafianRow, String> apostazjaColumn;

    void wklepHistorie(){
        try {
            query="SELECT \n" +
                    "p.id_parafii AS \"id_parafii\",\n" +
                    "p.nazwa AS \"nazwa\",\n" +
                    "a.miasto AS \"miasto\",\n" +
                    "a.ulica AS \"ulica\",\n" +
                    "a.nr_domu AS \"nr_domu\",\n" +
                    "hp.data_przybycia AS \"data_przybycia\",\n" +
                    "hp.data_odejscia AS \"data_odejscia\",\n" +
                    "hp.apostazja AS \"apostazja\"\n" +
                    "\tFROM historia_parafian hp LEFT JOIN parafie p ON hp.id_parafii=p.id_parafii\n" +
                    "\tLEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                    "\tWHERE hp.id_osoby='";
            query+=idPar+"'";

            Statement stmt = connection.createStatement();
            wyszukaneHistorie=stmt.executeQuery(query);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void insertHistorie(){
        try {
            historiaRow.clear();

            ResultSet rs=wyszukaneHistorie;

            while (rs.next()){
                historiaRow.add(new HistoriaParafianRow(
                        rs.getInt("id_parafii"),
                        rs.getString("nazwa"),
                        rs.getString("miasto"),
                        rs.getString("ulica"),
                        rs.getString("nr_domu"),
                        rs.getDate("data_przybycia"),
                        rs.getDate("data_odejscia"),
                        (rs.getBoolean("apostazja")? "TAK" : "NIE" )
                ));
            }
            id_pariafiiColumn.setCellValueFactory(new PropertyValueFactory<>("id_parafii"));
            nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            miastoColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));
            ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));
            nr_domuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));
            data_przybyciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_przybycia"));
            data_odejsciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_odejscia"));
            apostazjaColumn.setCellValueFactory(new PropertyValueFactory<>("apostazja"));

            historiaParafian.setItems(historiaRow);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void initialize() {
        assert imie_i_nazwisko != null : "fx:id=\"imie_i_nazwisko\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert id_historyka != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert historiaParafian != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert id_pariafiiColumn != null : "fx:id=\"id_pariafiiColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert nazwaColumn != null : "fx:id=\"nazwaColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert data_przybyciaColumn != null : "fx:id=\"data_przybyciaColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert data_odejsciaColumn != null : "fx:id=\"data_odejsciaColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";
        assert apostazjaColumn != null : "fx:id=\"apostazjaColumn\" was not injected: check your FXML file 'historiaParafian.fxml'.";

        imie_i_nazwisko.setText(imiePar+" "+nazwiskoPar);
        id_historyka.setText("id: "+idPar);
        wklepHistorie();
        insertHistorie();
    }
}
