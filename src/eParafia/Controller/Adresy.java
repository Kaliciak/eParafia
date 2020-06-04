package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class Adresy {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<AdresRow> adresy;

    @FXML
    private TableColumn<AdresRow, String> id_adresuColumn;

    @FXML
    private TableColumn<AdresRow, String> miastoColumn;

    @FXML
    private TableColumn<AdresRow, String> ulicaColumn;

    @FXML
    private TableColumn<AdresRow, String> nr_domuColumn;

    ObservableList<AdresRow> adresRows= FXCollections.observableArrayList();

    void wstawAdresy(){
        try {
            Statement stmt = connection.createStatement();
            String query="select * from adresy";
            ResultSet rs=stmt.executeQuery(query);

            while (rs.next()){
                adresRows.add(new AdresRow(
                        rs.getString("id_adresu"),
                        rs.getString("miasto"),
                        rs.getString("ulica"),
                        rs.getString("nr_domu")
                ));
            }
            id_adresuColumn.setCellValueFactory(new PropertyValueFactory<AdresRow, String>("id_adresu"));
            miastoColumn.setCellValueFactory(new PropertyValueFactory<AdresRow, String>("miasto"));
            ulicaColumn.setCellValueFactory(new PropertyValueFactory<AdresRow, String>("ulica"));
            nr_domuColumn.setCellValueFactory(new PropertyValueFactory<AdresRow, String>("nr_domu"));

            adresy.setItems(adresRows);
        }
        catch (Exception e){
            showErrorWindow(e);
            System.out.println("Nie powiodło się");
        }
    }

    @FXML
    void initialize() {
        assert id_adresuColumn != null : "fx:id=\"id_adresu\" was not injected: check your FXML file 'adresy.fxml'.";
        assert miastoColumn != null : "fx:id=\"miasto\" was not injected: check your FXML file 'adresy.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulica\" was not injected: check your FXML file 'adresy.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domu\" was not injected: check your FXML file 'adresy.fxml'.";

        wstawAdresy();
    }
}
