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
    void dodajWydarzenie(ActionEvent event) {

    }

    @FXML
    void wyszukajParafie(ActionEvent event) {

    }

    @FXML
    void wyszukajUczestnikow(ActionEvent event) {

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

        prepWydarzenia();
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
