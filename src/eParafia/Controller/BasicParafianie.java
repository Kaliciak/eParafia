package eParafia.Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class BasicParafianie {

    ObservableList<BasicParafianieRow> parafianieRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaniParafianie;
    boolean isWhere;
    String query;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<BasicParafianieRow> basicParafie;

    @FXML
    private TableColumn<BasicParafianieRow, Integer> id_osobyColumn;

    @FXML
    private TableColumn<BasicParafianieRow, String> imieColumn;

    @FXML
    private TableColumn<BasicParafianieRow, String> nazwiskoColumn;

    @FXML
    private TableColumn<BasicParafianieRow, Date> data_narodzinColumn;

    @FXML
    private TableColumn<BasicParafianieRow, Date> data_zgonuColumn;

    @FXML
    private Menu mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TextField imie;

    @FXML
    private TextField id_osoby;

    @FXML
    private TextField nazwisko;

    @FXML
    private DatePicker data_zgonu;

    @FXML
    private DatePicker data_narodzin;

    void prepParafianie(){
        try {
            Statement stmt = connection.createStatement();

            query="SELECT \n" +
                    "id_osoby AS \"id_osoby\",\n" +
                    "imie AS \"imie\",\n" +
                    "nazwisko AS \"nazwisko\",\n" +
                    "data_narodzin AS \"data_narodzin\",\n" +
                    "data_zgonu AS \"data_zgonu\"\n" +
                    "\tFROM parafianie";


            wyszukaniParafianie=stmt.executeQuery(query);;
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    public void insertParafianie(){

        parafianieRows.clear();

        try{
            ResultSet rs=wyszukaniParafianie;

            while (rs.next()){
                parafianieRows.add(new BasicParafianieRow(
                        rs.getInt("id_osoby"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getDate("data_narodzin"),
                        rs.getDate("data_zgonu")
                ));
            }


            id_osobyColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
            imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            data_narodzinColumn.setCellValueFactory(new PropertyValueFactory<>("data_narodzin"));
            data_zgonuColumn.setCellValueFactory(new PropertyValueFactory<>("data_zgonu"));

            basicParafie.setItems(parafianieRows);
        }
        catch (Exception e){
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
    void wyszukajOsobe(ActionEvent event) {
        String idPar=id_osoby.getText();
        String im=imie.getText();
        String naz=nazwisko.getText();
        LocalDate dNar=data_narodzin.getValue();
        LocalDate dZg=data_zgonu.getValue();

        isWhere=false;

        try {
            Statement stmt = connection.createStatement();
            query="SELECT \n" +
                    "id_osoby AS \"id_osoby\",\n" +
                    "imie AS \"imie\",\n" +
                    "nazwisko AS \"nazwisko\",\n" +
                    "data_narodzin AS \"data_narodzin\",\n" +
                    "data_zgonu AS \"data_zgonu\"\n" +
                    "\tFROM parafianie";

            if(idPar!=null && !idPar.isEmpty()){
                putWhere();
                query+="id_osoby='"+idPar+"'";
            }
            if(im!=null && !im.isEmpty()){
                putWhere();
                query+="imie LIKE '%"+im+"%'";
            }
            if(naz!=null && !naz.isEmpty()){
                putWhere();
                query+="nazwisko LIKE '%"+naz+"%'";
            }
            if(dNar!=null){
                putWhere();
                query+="EXTRACT(DAY FROM data_narodzin)='"+dNar.getDayOfMonth()+"'";
                putWhere();
                query+="EXTRACT(MONTH FROM data_narodzin)='"+dNar.getMonthValue()+"'";
                putWhere();
                query+="EXTRACT(YEAR FROM data_narodzin)='"+dNar.getYear()+"'";
            }

            if(dZg!=null){
                putWhere();
                query+="EXTRACT(DAY FROM data_zgonu)='"+dZg.getDayOfMonth()+"'";
                putWhere();
                query+="EXTRACT(MONTH FROM data_zgonu)='"+dZg.getMonthValue()+"'";
                putWhere();
                query+="EXTRACT(YEAR FROM data_zgonu)='"+dZg.getYear()+"'";
            }
            wyszukaniParafianie=stmt.executeQuery(query);
            insertParafianie();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void zaawansowaneSzukanie(ActionEvent event) {
        try {
            openSecondStage("FXML/advancedParafianie.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }


    @FXML
    void initialize() {
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert id_osobyColumn != null : "fx:id=\"id_osobyColumn\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert imieColumn != null : "fx:id=\"imieColumn\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert nazwiskoColumn != null : "fx:id=\"nazwiskoColumn\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert data_narodzinColumn != null : "fx:id=\"data_narodzinColumn\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert data_zgonuColumn != null : "fx:id=\"data_zgonuColumn\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicParafianie.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicParafianie.fxml'.";

        prepParafianie();
        insertParafianie();
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
