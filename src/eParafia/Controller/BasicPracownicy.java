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

public class BasicPracownicy {

    ObservableList<PracowincyRow> pracownicyRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaniPracownicy;
    boolean isWhere;
    String query;
    static String osoba=null;
    static String parafia=null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Menu mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TableView<PracowincyRow> basicPracownicy;

    @FXML
    private TableColumn<PracowincyRow, Integer> id_osobyColumn;

    @FXML
    private TableColumn<PracowincyRow, String> imieColumn;

    @FXML
    private TableColumn<PracowincyRow, String> nazwiskoColumn;

    @FXML
    private TableColumn<PracowincyRow, String> rolaColumn;

    @FXML
    private TableColumn<PracowincyRow, Integer> id_parafiiColumn;

    @FXML
    private TableColumn<PracowincyRow, String> nazwaColumn;

    @FXML
    private TableColumn<PracowincyRow, Date> data_rozpoczeciaColumn;

    @FXML
    private TableColumn<PracowincyRow, Date> data_zakonczeniaColumn;

    @FXML
    private DatePicker data_rozpoczecia_od;

    @FXML
    private DatePicker data_rozpoczecia_do;

    @FXML
    private DatePicker data_zakonczenia_do;

    @FXML
    private DatePicker data_zakonczenia_od;

    @FXML
    private TextField id_parafii;

    @FXML
    private TextField id_osoby;

    @FXML
    private TextField rola;

    @FXML
    void edytujPracownika(ActionEvent event) {
        try {
            if(id_osoby.getText()==null || id_osoby.getText().isEmpty()){
                throw new Exception("Należy podać id pracownika");
            }
            if(id_parafii.getText()==null || id_parafii.getText().isEmpty()){
                throw new Exception("Należy podać id parafii");
            }
            if(rola.getText()==null || rola.getText().isEmpty()){
                throw new Exception("Należy podać rolę pracownika");
            }

            query="INSERT INTO pracownicy(id_osoby, id_parafii, rola) VALUES ( ";
            query+="'"+id_osoby.getText()+"'";
            query+=",";
            query+="'"+id_parafii.getText()+"'";
            query+=",";
            query+="'"+rola.getText()+"')";

            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);

            wyszukajPracownika(event);
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
    void endPraca(){
        query="UPDATE pracownicy SET data_zakonczenia=now() WHERE id_osoby='";
        query+=basicPracownicy.getSelectionModel().getSelectedItem().id_osoby.getValue()+"'";
        try {
            Statement stmt=connection.createStatement();
            stmt.executeUpdate(query);
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void wyszukajPracownika(ActionEvent event) {
        String idPar=id_parafii.getText();
        String idOs=id_osoby.getText();
        String rol=rola.getText();
        LocalDate dRozOd=data_rozpoczecia_od.getValue();
        LocalDate dRozDo=data_rozpoczecia_do.getValue();
        LocalDate dZakOd=data_zakonczenia_od.getValue();
        LocalDate dZakDo=data_zakonczenia_do.getValue();

        try {
            Statement stmt = connection.createStatement();
            query="SELECT \n" +
                    "pa.id_osoby AS \"id_osoby\",\n" +
                    "pa.imie AS \"imie\",\n" +
                    "pa.nazwisko AS \"nazwisko\",\n" +
                    "pr.rola AS \"rola\",\n" +
                    "par.id_parafii AS \"id_parafii\",\n" +
                    "par.nazwa AS \"nazwa\",\n" +
                    "pr.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                    "pr.data_zakonczenia AS \"data_zakonczenia\"\n" +
                    "\tFROM pracownicy pr LEFT JOIN parafianie pa ON pr.id_osoby=pa.id_osoby\n" +
                    "\tLEFT JOIN parafie par ON pr.id_parafii=par.id_parafii\n";

            isWhere=false;

            if(idOs!=null && !idOs.isEmpty()){
                putWhere();
                query+="pa.id_osoby='"+idOs+"'";
            }
            if(idPar!=null && !idPar.isEmpty()){
                putWhere();
                query+="par.id_parafii= '"+idPar+"'";
            }
            if(rol!=null && !rol.isEmpty()){
                putWhere();
                query+="pr.rola LIKE '%"+rol+"%'";
            }
            if(dRozOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM pr.data_rozpoczecia)='"+dRozOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM pr.data_rozpoczecia)='"+dRozOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM pr.data_rozpoczecia)='"+dRozOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "pr.data_rozpoczecia>'" + dRozOd + "'))";
            }
            if(dRozDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM pr.data_rozpoczecia)='"+dRozDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM pr.data_rozpoczecia)='"+dRozDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM pr.data_rozpoczecia)='"+dRozDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "pr.data_rozpoczecia<'" + dRozDo + "'))";
            }
            if(dZakOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM pr.data_zakonczenia)='"+dZakOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM pr.data_zakonczenia)='"+dZakOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM pr.data_zakonczenia)='"+dZakOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "pr.data_zakonczenia>'" + dZakOd + "'))";
            }
            if(dZakDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM pr.data_zakonczenia)='"+dZakDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM pr.data_zakonczenia)='"+dZakDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM pr.data_zakonczenia)='"+dZakDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "pr.data_zakonczenia<'" + dZakDo + "'))";
            }
            wyszukaniPracownicy=stmt.executeQuery(query);
            insertPracownicy();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void prepPracowincy(){
        try {
            Statement stmt = connection.createStatement();

            query="SELECT \n" +
                    "pa.id_osoby AS \"id_osoby\",\n" +
                    "pa.imie AS \"imie\",\n" +
                    "pa.nazwisko AS \"nazwisko\",\n" +
                    "pr.rola AS \"rola\",\n" +
                    "par.id_parafii AS \"id_parafii\",\n" +
                    "par.nazwa AS \"nazwa\",\n" +
                    "pr.data_rozpoczecia AS \"data_rozpoczecia\",\n" +
                    "pr.data_zakonczenia AS \"data_zakonczenia\"\n" +
                    "\tFROM pracownicy pr LEFT JOIN parafianie pa ON pr.id_osoby=pa.id_osoby\n" +
                    "\tLEFT JOIN parafie par ON pr.id_parafii=par.id_parafii\n" +
                    "\tWHERE pr.data_zakonczenia IS NULL";


            wyszukaniPracownicy=stmt.executeQuery(query);;
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void insertPracownicy(){
        pracownicyRows.clear();

        try{
            ResultSet rs=wyszukaniPracownicy;

            while (rs.next()){
                pracownicyRows.add(new PracowincyRow(
                        rs.getInt("id_osoby"),
                        rs.getString("imie"),
                        rs.getString("nazwisko"),
                        rs.getString("rola"),
                        rs.getInt("id_parafii"),
                        rs.getString("nazwa"),
                        rs.getDate("data_rozpoczecia"),
                        rs.getDate("data_zakonczenia")
                ));
            }


            id_osobyColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
            imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
            nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            rolaColumn.setCellValueFactory(new PropertyValueFactory<>("rola"));
            id_parafiiColumn.setCellValueFactory(new PropertyValueFactory<>("id_parafii"));
            nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
            data_rozpoczeciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_rozpoczecia"));
            data_zakonczeniaColumn.setCellValueFactory(new PropertyValueFactory<>("data_zakonczenia"));

            basicPracownicy.setItems(pracownicyRows);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void showParafia(ActionEvent event) {
        try {
            Statement stmt = connection.createStatement();

            String idPar=basicPracownicy.getSelectionModel().getSelectedItem().id_parafii.getValue().toString();

            query="SELECT \n" +
                    "p.id_parafii AS \"id_parafii\",\n" +
                    "p.nazwa AS \"nazwa\",\n" +
                    "z.nazwa AS \"zakon\",\n" +
                    "a.miasto AS \"miasto\",\n" +
                    "a.ulica AS \"ulica\",\n" +
                    "a.nr_domu AS \"nr_domu\"\n" +
                    "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                    "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu" +
                    " WHERE p.id_parafii='" + idPar + "'";

            BasicParafie.wyszukaneParafie=stmt.executeQuery(query);
            BasicParafie.czyPrep=false;
            replaceSceneContent("FXML/basicParafie.fxml");
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void showPracownik(ActionEvent event) {
        try {
            Statement stmt = connection.createStatement();

            String idOS=basicPracownicy.getSelectionModel().getSelectedItem().id_osoby.getValue().toString();

            query="SELECT \n" +
                    "p.id_osoby AS \"id_osoby\",\n" +
                    "p.imie AS \"imie\",\n" +
                    "p.nazwisko AS \"nazwisko\",\n" +
                    "p.data_narodzin AS \"data_narodzin\",\n" +
                    "p.data_zgonu AS \"data_zgonu\"\n" +
                    "\tFROM parafianie p" +
                    "\tWHERE p.id_osoby= '"+ idOS + "'";

            BasicParafianie.wyszukaniParafianie=stmt.executeQuery(query);
            BasicParafianie.czyPrep=false;
            replaceSceneContent("FXML/basicParafianie.fxml");
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
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert basicPracownicy != null : "fx:id=\"basicPracownicy\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert id_osobyColumn != null : "fx:id=\"id_osobyColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert imieColumn != null : "fx:id=\"imieColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert nazwiskoColumn != null : "fx:id=\"nazwiskoColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert rolaColumn != null : "fx:id=\"rolaColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert id_parafiiColumn != null : "fx:id=\"id_parafiiColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert nazwaColumn != null : "fx:id=\"nazwaColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_rozpoczeciaColumn != null : "fx:id=\"data_rozpoczeciaColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_zakonczeniaColumn != null : "fx:id=\"data_zakonczeniaColumn\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_rozpoczecia_od != null : "fx:id=\"data_rozpoczęcia_od\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_rozpoczecia_do != null : "fx:id=\"data_rozpoczęcia_do\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_zakonczenia_do != null : "fx:id=\"data_zakończenia_do\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert data_zakonczenia_od != null : "fx:id=\"data_zakończenia_od\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert id_osoby != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'basicPracownicy.fxml'.";
        assert rola != null : "fx:id=\"rola\" was not injected: check your FXML file 'basicPracownicy.fxml'.";

        if(osoba!=null){
            id_osoby.setText(osoba);
            wyszukajPracownika(new ActionEvent());
            osoba=null;
        }
        else if(parafia!=null){
            id_parafii.setText(parafia);
            wyszukajPracownika(new ActionEvent());
            parafia=null;
        }
        else {
            prepPracowincy();
            insertPracownicy();
        }
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
