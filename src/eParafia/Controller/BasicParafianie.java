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
    static boolean czyPrep=true;

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
    void showSzczegoly(ActionEvent event) {
        try {
            Statement stmt = connection.createStatement();

            String idOs=basicParafie.getSelectionModel().getSelectedItem().id_osoby.getValue().toString();

            query="SELECT \n" +
                    "p.id_osoby AS \"id_osoby\",\n" +
                    "p.imie AS \"imie\",\n" +
                    "p.drugie_imie AS \"drugie_imie\",\n" +
                    "p.imie_z_bierzmowania AS \"imie_z_bierzmowania\",\n" +
                    "p.nazwisko AS \"nazwisko\",\n" +
                    "p.plec AS \"plec\",\n" +
                    "p.data_narodzin AS \"data_narodzin\",\n" +
                    "p.data_zgonu AS \"data_zgonu\",\n" +
                    "a.miasto AS \"miasto\",\n" +
                    "a.ulica AS \"ulica\",\n" +
                    "a.nr_domu AS \"nr_domu\",\n" +
                    "hp.id_parafii AS \"id_parafii\",\n" +
                    "p.id_ojca AS \"id_ojca\",\n" +
                    "p.id_matki AS \"id_matki\",\n" +
                    "p.id_ojca_chrzestnego AS \"id_ojca_chrzestnego\",\n" +
                    "p.id_matki_chrzestnej AS \"id_matki_chrzestnej\"\n" +
                    "\tFROM parafianie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                    "\tLEFT JOIN historia_parafian hp ON p.id_osoby=hp.id_osoby\n" +
                    "\tWHERE hp.data_odejscia IS NULL" +
                    " AND p.id_osoby='" + idOs +"'";

            AdvancedParafianie.czyPrep=false;
            AdvancedParafianie.wyszukaniParafianie=stmt.executeQuery(query);
            if(!AdvancedParafianie.wyszukaniParafianie.next()){
                query="SELECT \n" +
                        "p.id_osoby AS \"id_osoby\",\n" +
                        "p.imie AS \"imie\",\n" +
                        "p.drugie_imie AS \"drugie_imie\",\n" +
                        "p.imie_z_bierzmowania AS \"imie_z_bierzmowania\",\n" +
                        "p.nazwisko AS \"nazwisko\",\n" +
                        "p.plec AS \"plec\",\n" +
                        "p.data_narodzin AS \"data_narodzin\",\n" +
                        "p.data_zgonu AS \"data_zgonu\",\n" +
                        "a.miasto AS \"miasto\",\n" +
                        "a.ulica AS \"ulica\",\n" +
                        "a.nr_domu AS \"nr_domu\",\n" +
                        "null AS \"id_parafii\",\n" +
                        "p.id_ojca AS \"id_ojca\",\n" +
                        "p.id_matki AS \"id_matki\",\n" +
                        "p.id_ojca_chrzestnego AS \"id_ojca_chrzestnego\",\n" +
                        "p.id_matki_chrzestnej AS \"id_matki_chrzestnej\"\n" +
                        "\tFROM parafianie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                        "\tWHERE p.id_osoby='" + idOs +"'";
            }

            AdvancedParafianie.wyszukaniParafianie=stmt.executeQuery(query);

            openSecondStage("FXML/advancedParafianie.fxml");
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void dodajOsobe(ActionEvent event) {
        try {
            replaceSceneContent("FXML/modifyParafianin.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void editParafianin(ActionEvent event) {
        ModifyParafianin.prevNum=basicParafie.getSelectionModel().getSelectedItem().id_osoby.getValue().toString();
        try {
            replaceSceneContent("FXML/modifyParafianin.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void showHistoriaPracy(){
        BasicPracownicy.osoba=basicParafie.getSelectionModel().getSelectedItem().id_osoby.getValue().toString();
        czyPrep=false;
        try {
            replaceSceneContent("FXML/basicPracownicy.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void showHistoriaParafi(ActionEvent event) {
        HistoriaParafian.idPar=basicParafie.getSelectionModel().getSelectedItem().id_osoby.getValue().toString();
        HistoriaParafian.imiePar=basicParafie.getSelectionModel().getSelectedItem().imie.getValue();
        HistoriaParafian.nazwiskoPar=basicParafie.getSelectionModel().getSelectedItem().nazwisko.getValue();
        try{
            openSecondStage("FXML/historiaParafian.fxml");
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

        if (czyPrep) {
            prepParafianie();
        }
        else {
            czyPrep=true;
        }
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
