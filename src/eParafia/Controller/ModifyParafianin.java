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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import static eParafia.Controller.Dane.*;

public class ModifyParafianin {
    ObservableList<AdvancedParafianieRow> parafianieRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaniParafianie;
    public static boolean czyPrep=true;
    boolean isWhere;
    String query;
    String prevNum=null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<AdvancedParafianieRow> basicParafie;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_osobyColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> imieColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> drugie_imieColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> imie_z_bierzmowaniaColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> nazwiskoColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> plecColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Date> data_narodzinColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Date> data_zgonuColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> miastoColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> ulicaColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, String> nr_domuColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_parafiiColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_ojcaColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_matkiColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_ojca_chrzestnegoColumn;

    @FXML
    private TableColumn<AdvancedParafianieRow, Integer> id_matki_chrzestnejColumn;

    @FXML
    private TextField drugie_imie;

    @FXML
    private TextField id_osoby;

    @FXML
    private TextField nazwisko;

    @FXML
    private DatePicker data_narodzin;

    @FXML
    private TextField imie;

    @FXML
    private TextField imie_z_bierzmowania;

    @FXML
    private DatePicker data_zgonu;

    @FXML
    private TextField id_ojca;

    @FXML
    private TextField id_ojca_chrzestnego;

    @FXML
    private TextField id_matki;

    @FXML
    private TextField ulica;

    @FXML
    private TextField id_matki_chrzestnej;

    @FXML
    private TextField miasto;

    @FXML
    private TextField nr_domu;

    @FXML
    private TextField id_parafii;

    void dodajOsobe(){
        try {
            Statement stmt = connection.createStatement();
            //znajdowanie adresu
            String mia=miasto.getText();
            String ul=ulica.getText();
            String nrDom=nr_domu.getText();
            if(mia==null || mia.isEmpty() || ul==null || ul.isEmpty() || nrDom==null || nrDom.isEmpty()){
                throw new Exception("Nalezy podać adres");
            }

            query="SELECT * FROM ADRESY WHERE ";
            query+="miasto='" + mia + "'";
            query+=" AND ulica='" + ul + "'";
            query+= " AND nr_domu='" + nrDom + "'";

            stmt = connection.createStatement();
            ResultSet rs= stmt.executeQuery(query);

            String aId=null;
            while (rs.next()){
                aId=rs.getString("id_adresu");
            }

            if(aId==null){
                stmt = connection.createStatement();
                ResultSet nid= stmt.executeQuery("SELECT max(id_adresu) AS \"id\" FROM adresy");
                Integer nId=0;
                while (nid.next()){
                    nId=nid.getInt("id");
                }
                nId++;
                query="INSERT INTO ADRESY(id_adresu, miasto, ulica, nr_domu)\n" +
                        "VALUES\n" +
                        " (\n";
                query+= "'"+ nId + "'," ;
                query+="'"+ mia + "'," ;
                query+="'"+ ul + "'," ;
                query+= "'"+nrDom + "')" ;

                stmt = connection.createStatement();
                stmt.executeUpdate(query);
            }

            Integer newParafianinId;
            stmt = connection.createStatement();
            //query=""

        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void zmienOsobe(ActionEvent event) {
        if(id_osoby.getText()!=null && !id_osoby.getText().isEmpty()){

        }
        else {
            dodajOsobe();
        }
    }

    @FXML
    void initialize() {
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_osobyColumn != null : "fx:id=\"id_osobyColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imieColumn != null : "fx:id=\"imieColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert drugie_imieColumn != null : "fx:id=\"drugie_imieColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie_z_bierzmowaniaColumn != null : "fx:id=\"imie_z_bierzmowaniaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nazwiskoColumn != null : "fx:id=\"nazwiskoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert plecColumn != null : "fx:id=\"plecColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_narodzinColumn != null : "fx:id=\"data_narodzinColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_zgonuColumn != null : "fx:id=\"data_zgonuColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_parafiiColumn != null : "fx:id=\"id_parafiiColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojcaColumn != null : "fx:id=\"id_ojcaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matkiColumn != null : "fx:id=\"id_matkiColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca_chrzestnegoColumn != null : "fx:id=\"id_ojca_chrzestnegoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki_chrzestnejColumn != null : "fx:id=\"id_matki_chrzestnejColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert drugie_imie != null : "fx:id=\"drugie_imie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_osoby != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nazwisko != null : "fx:id=\"nazwisko\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_narodzin != null : "fx:id=\"data_narodzin\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie != null : "fx:id=\"imie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie_z_bierzmowania != null : "fx:id=\"imie_z_bierzmowania\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_zgonu != null : "fx:id=\"data_zgonu\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca != null : "fx:id=\"id_ojca\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca_chrzestnego != null : "fx:id=\"id_ojca_chrzestnego\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki != null : "fx:id=\"id_matki\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert ulica != null : "fx:id=\"ulica\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki_chrzestnej != null : "fx:id=\"id_matki_chrzestnej\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert miasto != null : "fx:id=\"miasto\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nr_domu != null : "fx:id=\"nr_domu\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'modifyParafianin.fxml'.";

        if(prevNum!=null && !prevNum.isEmpty()){
            id_osoby.setText(prevNum);
        }
        else {
            prevNum=null;
        }

    }
}
