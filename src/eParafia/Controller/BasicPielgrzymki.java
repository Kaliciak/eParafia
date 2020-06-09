package eParafia.Controller;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class BasicPielgrzymki {

    ObservableList<PielgrzymkiRow> wydarzeniaRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaneWydarzenia;
    public static boolean czyPrep=true;
    boolean isWhere;
    String query;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu mainMenu;

    @FXML
    private MenuItem logout;

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
    private TextField id_wydarzenia_dod;

    @FXML
    private TextField id_osoby;

    @FXML
    private TextField id_parafii;

    @FXML
    private DatePicker data_zakonczenia_do;

    @FXML
    private DatePicker data_zakonczenia_od;

    @FXML
    private DatePicker data_rozpoczecia_od;

    @FXML
    private DatePicker data_rozpoczecia_do;

    @FXML
    private TextField koszt;

    @FXML
    private TextField id_wydarzenia;

    @FXML
    private TextField cel;

    @FXML
    private TextField miejsce_startu;

    public void putWhere(){
        if(!isWhere){
            isWhere=true;
            query+=" WHERE ";
        }
        else {
            query+=" AND ";
        }
    }

    @FXML
    void dodajUczestnika(ActionEvent event) {
        try {
            if(id_wydarzenia_dod.getText()==null || id_wydarzenia_dod.getText().isEmpty()){
                throw new Exception("Należy podać id wydarzenia");
            }
            if(id_osoby.getText()==null || id_osoby.getText().isEmpty()){
                throw new Exception("Należy podać id osoby");
            }
            query="INSERT INTO uczestnicy_wydarzenia (id_wydarzenia, id_osoby) VALUES (";
            query+="'"+id_wydarzenia_dod.getText()+"','"+id_osoby.getText()+"')";
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void dodajParafie(ActionEvent event) {
        try {
            if(id_wydarzenia_dod.getText()==null || id_wydarzenia_dod.getText().isEmpty()){
                throw new Exception("Należy podać id wydarzenia");
            }
            if(id_parafii.getText()==null || id_parafii.getText().isEmpty()){
                throw new Exception("Należy podać id parafii");
            }
            query="INSERT INTO parafie_wydarzenia (id_wydarzenia, id_parafii) VALUES (";
            query+="'"+id_wydarzenia_dod.getText()+"','"+id_parafii.getText()+"')";
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void dodajPielgrzymke(ActionEvent event) {
        try {
            openSecondStage("FXML/dodajPielgrzymke.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void edytujPielgrzymke(ActionEvent event) {
        try {
            DodajPielgrzymke.idWydarzenia=basicWydarzenia.getSelectionModel().getSelectedItem().id_wydarzenia.getValue().toString();
            openSecondStage("FXML/dodajPielgrzymke.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
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

    @FXML
    void wyszukajWydarzenie(ActionEvent event) {
        String idWyd=id_wydarzenia.getText();
        LocalDate dRozOd=data_rozpoczecia_od.getValue();
        LocalDate dRozDo=data_rozpoczecia_do.getValue();
        LocalDate dZakOd=data_zakonczenia_od.getValue();
        LocalDate dZakDo=data_zakonczenia_do.getValue();
        String kosz=koszt.getText();
        String mStart=miejsce_startu.getText();
        String ce=cel.getText();

        isWhere=false;

        try {
            Statement stmt = connection.createStatement();
            query="SELECT\n" +
                    "p.id_wydarzenia AS \"id_wydarzenia\",\n" +
                    "w.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                    "w.data_zakonczenia AS \"data_zakonczenia\",\n" +
                    "p.miejsce_startu AS \"miejsce_startu\",\n" +
                    "p.cel_pielgrzymki AS \"cel\",\n" +
                    "p.koszt_pielgrzymki AS \"koszt\"\n" +
                    "\tFROM pielgrzymki p LEFT JOIN wydarzenia w ON p.id_wydarzenia=w.id_wydarzenia\n";

            if(idWyd!=null && !idWyd.isEmpty()){
                putWhere();
                query+="p.id_wydarzenia='"+idWyd+"'";
            }
            if(dRozOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM w.data_rozpoczecia)='"+dRozOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM w.data_rozpoczecia)='"+dRozOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM w.data_rozpoczecia)='"+dRozOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "w.data_rozpoczecia>'" + dRozOd + "'))";
            }
            if(dRozDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM w.data_rozpoczecia)='"+dRozDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM w.data_rozpoczecia)='"+dRozDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM w.data_rozpoczecia)='"+dRozDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "w.data_rozpoczecia<'" + dRozDo + "'))";
            }
            if(dZakOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM w.data_zakonczenia)='"+dZakOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM w.data_zakonczenia)='"+dZakOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM w.data_zakonczenia)='"+dZakOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "w.data_zakonczenia>'" + dZakOd + "'))";
            }
            if(dZakDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM w.data_zakonczenia)='"+dZakDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM w.data_zakonczenia)='"+dZakDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM w.data_zakonczenia)='"+dZakDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "w.data_zakonczenia<'" + dZakDo + "'))";
            }
            if(mStart!=null && !mStart.isEmpty()){
                putWhere();
                query+="p.miejsce_startu LIKE'%"+mStart+"%'";
            }
            if(ce!=null && !ce.isEmpty()){
                putWhere();
                query+="p.cel_pielgrzymki LIKE'%"+ce+"%'";
            }
            if(kosz!=null && !kosz.isEmpty()){
                putWhere();
                query+="p.koszt_pielgrzymki ='"+kosz+"'";
            }
            query+=" ORDER BY w.data_rozpoczecia DESC";
            wyszukaneWydarzenia=stmt.executeQuery(query);
            insertWydarzenia();
        }
        catch (Exception e){
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
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert basicWydarzenia != null : "fx:id=\"basicWydarzenia\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert id_wydarzeniaColumn != null : "fx:id=\"id_wydarzeniaColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_rozpoczeciaColumn != null : "fx:id=\"data_rozpoczeciaColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_zakonczeniaColumn != null : "fx:id=\"data_zakonczeniaColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert miejsce_startuColumn != null : "fx:id=\"miejsce_startuColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert celColumn != null : "fx:id=\"celColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert kosztColumn != null : "fx:id=\"kosztColumn\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert id_wydarzenia_dod != null : "fx:id=\"id_wydarzenia_dod\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert id_osoby != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_zakonczenia_do != null : "fx:id=\"data_zakonczenia_do\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_zakonczenia_od != null : "fx:id=\"data_zakonczenia_od\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_rozpoczecia_od != null : "fx:id=\"data_rozpoczecia_od\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert data_rozpoczecia_do != null : "fx:id=\"data_rozpoczecia_do\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert koszt != null : "fx:id=\"koszt\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert id_wydarzenia != null : "fx:id=\"id_wydarzenia\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert cel != null : "fx:id=\"cel\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";
        assert miejsce_startu != null : "fx:id=\"miejsce_startu\" was not injected: check your FXML file 'basicPielgrzymki.fxml'.";

        if(czyPrep){
            prepWydarzenia();
        }
        else {
            czyPrep=true;
        }
        insertWydarzenia();

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
        logout();
    }
    ////
}
