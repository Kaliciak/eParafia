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

public class BasicWydarzenia {

    ObservableList<WydarzeniaRow> wydarzeniaRows= FXCollections.observableArrayList();
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
    private TableView<WydarzeniaRow> basicWydarzenia;

    @FXML
    private TableColumn<WydarzeniaRow, Integer> id_wydarzeniaColumn;

    @FXML
    private TableColumn<WydarzeniaRow, String> wydarzenieColumn;

    @FXML
    private TableColumn<WydarzeniaRow, Date> data_rozpoczeciaColumn;

    @FXML
    private TableColumn<WydarzeniaRow, Date> data_zakonczeniaColumn;

    @FXML
    private TextField id_wydarzenia;

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
    private TextField wydarzenie;

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
    void dodajWydarzenie(ActionEvent event) {
        try {
            openSecondStage("FXML/dodajWydarzenie.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

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
    void wyszukajWydarzenie(ActionEvent event) {
        String idWyd=id_wydarzenia.getText();
        String wyd=wydarzenie.getText();
        LocalDate dRozOd=data_rozpoczecia_od.getValue();
        LocalDate dRozDo=data_rozpoczecia_do.getValue();
        LocalDate dZakOd=data_zakonczenia_od.getValue();
        LocalDate dZakDo=data_zakonczenia_do.getValue();

        isWhere=false;

        try {
            Statement stmt = connection.createStatement();
            query="SELECT *\n" +
                    "\tFROM WYDARZENIA\n";

            if(idWyd!=null && !idWyd.isEmpty()){
                putWhere();
                query+="id_wydarzenia='"+idWyd+"'";
            }
            if(wyd!=null && !wyd.isEmpty()){
                putWhere();
                query+="wydarzenie LIKE '%"+wyd+"%'";
            }
            if(dRozOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM data_rozpoczecia)='"+dRozOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM data_rozpoczecia)='"+dRozOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM data_rozpoczecia)='"+dRozOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "data_rozpoczecia>'" + dRozOd + "'))";
            }
            if(dRozDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM data_rozpoczecia)='"+dRozDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM data_rozpoczecia)='"+dRozDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM data_rozpoczecia)='"+dRozDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "data_rozpoczecia<'" + dRozDo + "'))";
            }
            if(dZakOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM data_zakonczenia)='"+dZakOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM data_zakonczenia)='"+dZakOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM data_zakonczenia)='"+dZakOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "data_zakonczenia>'" + dZakOd + "'))";
            }
            if(dZakDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM data_zakonczenia)='"+dZakDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM data_zakonczenia)='"+dZakDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM data_zakonczenia)='"+dZakDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "data_zakonczenia<" + dZakDo + "'))";
            }
            query+=" ORDER BY data_rozpoczecia DESC";
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

            query="SELECT *\n" +
                    "\tFROM WYDARZENIA\n" +
                    "\tORDER BY data_rozpoczecia DESC;";


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
                wydarzeniaRows.add(new WydarzeniaRow(
                        rs.getInt("id_wydarzenia"),
                        rs.getString("wydarzenie"),
                        rs.getDate("data_rozpoczecia"),
                        rs.getDate("data_zakonczenia")
                ));
            }


            id_wydarzeniaColumn.setCellValueFactory(new PropertyValueFactory<>("id_wydarzenia"));
            wydarzenieColumn.setCellValueFactory(new PropertyValueFactory<>("wydarzenie"));
            data_rozpoczeciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_rozpoczecia"));
            data_zakonczeniaColumn.setCellValueFactory(new PropertyValueFactory<>("data_zakonczenia"));

            basicWydarzenia.setItems(wydarzeniaRows);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void edytujWydarzenie(ActionEvent event) {
        try {
            DodajWydarzenie.idWydarzenia=basicWydarzenia.getSelectionModel().getSelectedItem().id_wydarzenia.getValue().toString();
            openSecondStage("FXML/dodajWydarzenie.fxml");
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
    void initialize() {
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert basicWydarzenia != null : "fx:id=\"basicWydarzenia\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert id_wydarzeniaColumn != null : "fx:id=\"id_wydarzeniaColumn\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert wydarzenieColumn != null : "fx:id=\"wydarzenieColumn\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_rozpoczeciaColumn != null : "fx:id=\"data_rozpoczeciaColumn\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_zakonczeniaColumn != null : "fx:id=\"data_zakonczeniaColumn\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert id_wydarzenia_dod != null : "fx:id=\"id_wydarzenia_dod\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert id_osoby != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_zakonczenia_do != null : "fx:id=\"data_zakonczenia_do\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_zakonczenia_od != null : "fx:id=\"data_zakonczenia_od\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_rozpoczecia_od != null : "fx:id=\"data_rozpoczecia_od\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert data_rozpoczecia_do != null : "fx:id=\"data_rozpoczecia_do\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert wydarzenie != null : "fx:id=\"wydarzenie\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";
        assert id_wydarzenia != null : "fx:id=\"id_wydarzenia\" was not injected: check your FXML file 'basicWydarzenia.fxml'.";

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
        System.out.println("WYLOGUJ");
        try {
            connection.close();
            replaceSceneContent("FXML/login.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }
    ////
}
