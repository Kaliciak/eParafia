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

import static eParafia.Controller.Dane.connection;
import static eParafia.Controller.Dane.showErrorWindow;

public class DodajWydarzenie {

    ObservableList<WydarzeniaRow> wydarzeniaRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaneWydarzenia;
    boolean isWhere;
    String query;
    static String idWydarzenia=null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    private TextField wydarzenie;

    @FXML
    private DatePicker data_rozpoczecia;

    @FXML
    private TextField id_wydarzenia;

    @FXML
    private DatePicker data_zakonczenia;

    void showWydarzenie(String wydId){
        try {
            query="SELECT * FROM wydarzenia WHERE id_wydarzenia='"+wydId+"'";
            Statement stmt=connection.createStatement();
            wyszukaneWydarzenia=stmt.executeQuery(query);
            insertWydarzenia();
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void dodajWydarzenie(){
        try {
            if(wydarzenie.getText()==null || wydarzenie.getText().isEmpty()){
                throw new Exception("Należy podać opis wydarzenia");
            }
            query="INSERT INTO wydarzenia (wydarzenie";

            if(data_rozpoczecia.getValue()!=null){
                query+=",data_rozpoczecia";
            }
            if(data_zakonczenia.getValue()!=null){
                query+=",data_zakonczenia";
            }

            query+=") VALUES ('"+wydarzenie.getText()+"'";
            if(data_rozpoczecia.getValue()!=null){
                query+=",'"+data_rozpoczecia.getValue()+"'";
            }
            if(data_zakonczenia.getValue()!=null){
                query+=",'"+data_zakonczenia.getValue()+"'";
            }
            query+=")";
            //System.out.println(query);
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);

            String wydId="0";
            query="SELECT max(id_wydarzenia) AS \"max\" FROM wydarzenia";
            stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while (rs.next()){
                wydId=rs.getString("max");
                break;
            }
            showWydarzenie(wydId);

        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void edytujWydarzenie(){
        try {
            query="UPDATE wydarzenia SET id_wydarzenia='"+id_wydarzenia.getText()+"'";
            if(wydarzenie.getText()!=null && !wydarzenie.getText().isEmpty()){
                query+=",wydarzenie='"+wydarzenie.getText()+"'";
            }
            if(data_rozpoczecia.getValue()!=null){
                query+=",data_rozpoczecia='"+data_rozpoczecia.getValue()+"'";
            }
            if(data_zakonczenia.getValue()!=null){
                query+=",data_zakonczenia='"+data_zakonczenia.getValue()+"'";
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
        if(id_wydarzenia.getText()==null || id_wydarzenia.getText().isEmpty()){
            dodajWydarzenie();
        }
        else {
            edytujWydarzenie();
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
        assert basicWydarzenia != null : "fx:id=\"basicWydarzenia\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert id_wydarzeniaColumn != null : "fx:id=\"id_wydarzeniaColumn\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert wydarzenieColumn != null : "fx:id=\"wydarzenieColumn\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert data_rozpoczeciaColumn != null : "fx:id=\"data_rozpoczeciaColumn\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert data_zakonczeniaColumn != null : "fx:id=\"data_zakonczeniaColumn\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert wydarzenie != null : "fx:id=\"wydarzenie\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert data_rozpoczecia != null : "fx:id=\"data_rozpoczecia\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert id_wydarzenia != null : "fx:id=\"id_wydarzenia\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";
        assert data_zakonczenia != null : "fx:id=\"data_zakonczenia\" was not injected: check your FXML file 'dodajWydarzenie.fxml'.";

        if(idWydarzenia!=null){
            try {
                id_wydarzenia.setText(idWydarzenia);
                query="SELECT * FROM wydarzenia WHERE id_wydarzenia='"+idWydarzenia+"'";
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
