package eParafia.Controller;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class DodajPielgrzymke {

    ObservableList<PielgrzymkiRow> wydarzeniaRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaneWydarzenia;
    boolean isWhere;
    String query;
    static String idWydarzenia=null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<PielgrzymkiRow> basicWydarzenia;

    @FXML
    private TableColumn<PielgrzymkiRow, Integer> id_wydarzeniaColumn;

    @FXML
    private TableColumn<PielgrzymkiRow, Date> data_rozpoczeciaColumn;

    @FXML
    private TableColumn<PielgrzymkiRow, Date> data_zakonczeniaColumn;

    @FXML
    private TableColumn<PielgrzymkiRow, String> miejsce_startuColumn;

    @FXML
    private TableColumn<PielgrzymkiRow, String> celColumn;

    @FXML
    private TableColumn<PielgrzymkiRow, Integer> kosztColumn;

    @FXML
    private DatePicker data_rozpoczecia;

    @FXML
    private TextField id_wydarzenia;

    @FXML
    private DatePicker data_zakonczenia;

    @FXML
    private TextField koszt;

    @FXML
    private TextField cel;

    @FXML
    private TextField miejsce_startu;

    @FXML
    void edytujPielgrzymke(ActionEvent event) {

    }

    @FXML
    void osobyUczestniczace(ActionEvent event) {
        try {
            try {
                Statement stmt = connection.createStatement();

                query="SELECT \n" +
                        "id_osoby AS \"id_osoby\",\n" +
                        "imie AS \"imie\",\n" +
                        "nazwisko AS \"nazwisko\",\n" +
                        "data_narodzin AS \"data_narodzin\",\n" +
                        "data_zgonu AS \"data_zgonu\"\n" +
                        "\tFROM parafianie" +
                        " WHERE id_osoby IN(" +
                        "SELECT id_osoby FROM uczestnicy_wydarzenia WHERE id_wydarzenia='" +
                        basicWydarzenia.getSelectionModel().getSelectedItem().id_wydarzenia.getValue() +
                        "')"+
                        " ORDER BY id_osoby";

                BasicParafianie.wyszukaniParafianie=stmt.executeQuery(query);
                BasicParafianie.czyPrep=false;
            }
            catch (Exception e){
                showErrorWindow(e);
            }

            replaceSceneContent("FXML/basicParafianie.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void parafieOrganizujace(ActionEvent event) {
        try {
            Statement stmt = connection.createStatement();

            query="SELECT \n" +
                    "p.id_parafii AS \"id_parafii\",\n" +
                    "p.nazwa AS \"nazwa\",\n" +
                    "z.nazwa AS \"zakon\",\n" +
                    "a.miasto AS \"miasto\",\n" +
                    "a.ulica AS \"ulica\",\n" +
                    "a.nr_domu AS \"nr_domu\"\n" +
                    "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                    "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu" +
                    " WHERE id_parafii IN("+
                    "SELECT id_parafii FROM parafie_wydarzenia WHERE id_wydarzenia='" +
                    basicWydarzenia.getSelectionModel().getSelectedItem().id_wydarzenia.getValue() +
                    "')"+
                    "ORDER BY p.id_parafii";

            BasicParafie.wyszukaneParafie=stmt.executeQuery(query);
            BasicParafie.czyPrep=false;
            replaceSceneContent("FXML/basicParafie.fxml");
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void showWydarzenie(String wydId){
        try {
            query="SELECT\n" +
                    "p.id_wydarzenia AS \"id_wydarzenia\",\n" +
                    "w.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                    "w.data_zakonczenia AS \"data_zakonczenia\",\n" +
                    "p.miejsce_startu AS \"miejsce_startu\",\n" +
                    "p.cel_pielgrzymki AS \"cel\",\n" +
                    "p.koszt_pielgrzymki AS \"koszt\"\n" +
                    "\tFROM pielgrzymki p LEFT JOIN wydarzenia w ON p.id_wydarzenia=w.id_wydarzenia\n" +
                    "\tWHERE p.id_wydarzenia='"+id_wydarzenia.getText()+"'";
            Statement stmt=connection.createStatement();
            wyszukaneWydarzenia=stmt.executeQuery(query);
            insertWydarzenia();
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void dodajWydarzenie(){
        try {
            if(cel.getText()==null || cel.getText().isEmpty()){
                throw new Exception("Należy podać cel pielgrzymki");
            }
            if(miejsce_startu.getText()==null || miejsce_startu.getText().isEmpty()){
                throw new Exception("Należy podać miejsce startu pielgrzymki");
            }
            query="INSERT INTO pielgrzymki (id_wydarzenia,miejsce_startu,cel_pielgrzymki";

            if(koszt.getText()!=null && !koszt.getText().isEmpty()){
                query+=",koszt_pielgrzymki";
            }

            query+=") VALUES ('"+id_wydarzenia.getText()+"','"+miejsce_startu.getText()+"','"+cel.getText()+"'";
            if(koszt.getText()!=null && !koszt.getText().isEmpty()){
                query+=",'"+koszt.getText()+"'";
            }
            query+=")";
            //System.out.println(query);
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);

            showWydarzenie(id_wydarzenia.getText());

        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void edytujWydarzenie(){
        try {
            query="UPDATE pielgrzymki SET id_wydarzenia='"+id_wydarzenia.getText()+"'";
            if(miejsce_startu.getText()!=null && !miejsce_startu.getText().isEmpty()){
                query+=",miejsce_startu='"+miejsce_startu.getText()+"'";
            }
            if(cel.getText()!=null && !cel.getText().isEmpty()){
                query+=",cel_pielgrzymki='"+cel.getText()+"'";
            }
            if(koszt.getText()!=null && !koszt.getText().isEmpty()){
                query+=",koszt_pielgrzymki='"+koszt.getText()+"'";
            }
            query+=" WHERE id_wydarzenia='" + id_wydarzenia.getText()+ "'";
            Statement stst=connection.createStatement();
            //System.out.println(query);
            stst.executeUpdate(query);

            showWydarzenie(id_wydarzenia.getText());
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void zmienWydarzenie(ActionEvent event) {
        try {
            if(id_wydarzenia.getText()==null || id_wydarzenia.getText().isEmpty()){
                throw new Exception("Należy podać wydarzenie które jest pielgrzymką");
            }
            else {
                query="SELECT * FROM pielgrzymki WHERE id_wydarzenia='"+id_wydarzenia.getText()+"'";
                Statement stmt=connection.createStatement();
                ResultSet rs=stmt.executeQuery(query);
                boolean czyPiel=false;
                while (rs.next()){
                    czyPiel=true;
                }
                if(czyPiel){
                    edytujWydarzenie();
                }
                else {
                    dodajWydarzenie();
                }
            }
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void prepWydarzenia(){
        try {
            Statement stmt = connection.createStatement();

            query="SELECT\n" +
                    "p.id_wydarzenia AS \"id_wydarzenia\",\n" +
                    "w.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                    "w.data_zakonczenia AS \"data_zakonczenia\",\n" +
                    "p.miejsce_startu AS \"miejsce_startu\",\n" +
                    "p.cel_pielgrzymki AS \"cel\",\n" +
                    "p.koszt_pielgrzymki AS \"koszt\"\n" +
                    "\tFROM pielgrzymki p LEFT JOIN wydarzenia w ON p.id_wydarzenia=w.id_wydarzenia\n" +
                    "\tORDER BY w.data_rozpoczecia DESC";


            wyszukaneWydarzenia=stmt.executeQuery(query);;
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    public void insertWydarzenia(){

        wydarzeniaRows.clear();

        try{
            ResultSet rs=wyszukaneWydarzenia;

            while (rs.next()){
                wydarzeniaRows.add(new PielgrzymkiRow(
                        rs.getInt("id_wydarzenia"),
                        rs.getDate("data_rozpoczecia"),
                        rs.getDate("data_zakonczenia"),
                        rs.getString("miejsce_startu"),
                        rs.getString("cel"),
                        rs.getInt("koszt")
                ));
            }


            id_wydarzeniaColumn.setCellValueFactory(new PropertyValueFactory<>("id_wydarzenia"));
            data_rozpoczeciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_rozpoczecia"));
            data_zakonczeniaColumn.setCellValueFactory(new PropertyValueFactory<>("data_zakonczenia"));
            miejsce_startuColumn.setCellValueFactory(new PropertyValueFactory<>("miejsce_startu"));
            celColumn.setCellValueFactory(new PropertyValueFactory<>("cel"));
            kosztColumn.setCellValueFactory(new PropertyValueFactory<>("koszt"));

            basicWydarzenia.setItems(wydarzeniaRows);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void initialize() {
        assert basicWydarzenia != null : "fx:id=\"basicWydarzenia\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert id_wydarzeniaColumn != null : "fx:id=\"id_wydarzeniaColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert data_rozpoczeciaColumn != null : "fx:id=\"data_rozpoczeciaColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert data_zakonczeniaColumn != null : "fx:id=\"data_zakonczeniaColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert miejsce_startuColumn != null : "fx:id=\"miejsce_startuColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert celColumn != null : "fx:id=\"celColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert kosztColumn != null : "fx:id=\"kosztColumn\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert data_rozpoczecia != null : "fx:id=\"data_rozpoczecia\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert id_wydarzenia != null : "fx:id=\"id_wydarzenia\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert data_zakonczenia != null : "fx:id=\"data_zakonczenia\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert koszt != null : "fx:id=\"koszt\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert cel != null : "fx:id=\"cel\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";
        assert miejsce_startu != null : "fx:id=\"miejsce_startu\" was not injected: check your FXML file 'dodajPielgrzymke.fxml'.";

        if(idWydarzenia!=null){
            try {
                id_wydarzenia.setText(idWydarzenia);
                query="SELECT\n" +
                        "p.id_wydarzenia AS \"id_wydarzenia\",\n" +
                        "w.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                        "w.data_zakonczenia AS \"data_zakonczenia\",\n" +
                        "p.miejsce_startu AS \"miejsce_startu\",\n" +
                        "p.cel_pielgrzymki AS \"cel\",\n" +
                        "p.koszt_pielgrzymki AS \"koszt\"\n" +
                        "\tFROM pielgrzymki p LEFT JOIN wydarzenia w ON p.id_wydarzenia=w.id_wydarzenia\n" +"" +
                        " WHERE p.id_wydarzenia='"+idWydarzenia+"'";
                Statement stmt=connection.createStatement();
                wyszukaneWydarzenia=stmt.executeQuery(query);
                idWydarzenia=null;
            }catch (Exception e){
                showErrorWindow(e);
            }
        }
        else {
            prepWydarzenia();
        }
        insertWydarzenia();
    }
}
