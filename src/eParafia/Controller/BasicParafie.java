package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.plaf.nimbus.State;

import static eParafia.Controller.Dane.*;

public class BasicParafie {

    ObservableList<BasicParafieRow> parafieRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaneParafie;
    boolean isWhere;
    String query;
    static boolean czyPrep=true;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<BasicParafieRow> basicParafie;

    @FXML
    private TableColumn<BasicParafieRow, Integer> id_pariafiiColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> nazwaColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> zakonColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> miastoColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> ulicaColumn;

    @FXML
    private TableColumn<BasicParafieRow, String> nr_domuColumn;

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TextField nazwa;

    @FXML
    private ComboBox<String> zakon;

    @FXML
    private TextField id_parafii;

    @FXML
    private TextField miasto;

    @FXML
    private TextField ulica;

    @FXML
    private TextField nr_domu;

    void prepParafie(){
        try {

            query=
                    "SELECT \n" +
                            "p.id_parafii AS \"id_parafii\",\n" +
                            "p.nazwa AS \"nazwa\",\n" +
                            "z.nazwa AS \"zakon\",\n" +
                            "a.miasto AS \"miasto\",\n" +
                            "a.ulica AS \"ulica\",\n" +
                            "a.nr_domu AS \"nr_domu\"\n" +
                            "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                            "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu";

            Statement stmt = connection.createStatement();
            wyszukaneParafie=stmt.executeQuery(query);
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

     void insertParafie(){
         try {
             parafieRows.clear();

             ResultSet rs=wyszukaneParafie;

             while (rs.next()){
                 parafieRows.add(new BasicParafieRow(
                         rs.getInt("id_parafii"),
                         rs.getString("nazwa"),
                         rs.getString("zakon"),
                         rs.getString("miasto"),
                         rs.getString("ulica"),
                         rs.getString("nr_domu")
                 ));
             }
             id_pariafiiColumn.setCellValueFactory(new PropertyValueFactory<>("id_parafii"));
             nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
             zakonColumn.setCellValueFactory(new PropertyValueFactory<>("zakon"));
             miastoColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));
             ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));
             nr_domuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));

             basicParafie.setItems(parafieRows);
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
    void wyszukajParafie(ActionEvent event) {
        String idPar=id_parafii.getText();
        String naz=nazwa.getText();
        String zak=zakon.getSelectionModel().getSelectedItem();
        String miast=miasto.getText();
        String ul=ulica.getText();
        String nrDom=nr_domu.getText();

        isWhere=false;

        try {
            Statement stmt = connection.createStatement();
            query=
                    "SELECT \n" +
                            "p.id_parafii AS \"id_parafii\",\n" +
                            "p.nazwa AS \"nazwa\",\n" +
                            "z.nazwa AS \"zakon\",\n" +
                            "a.miasto AS \"miasto\",\n" +
                            "a.ulica AS \"ulica\",\n" +
                            "a.nr_domu AS \"nr_domu\"\n" +
                            "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                            "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu";

            if(idPar!=null && !idPar.isEmpty()){
                putWhere();
                query+="id_parafii='"+idPar+"'";
            }
            if(naz!=null && !naz.isEmpty()){
                putWhere();
                query+="p.nazwa LIKE '%"+naz+"%'";
            }
            if(zak!=null && !zak.isEmpty()){
                if(zak.equals("Każdy")){}
                else if(zak.equals("Bez zakonu")){
                    putWhere();
                    query+="zakon IS NULL";
                }
                else{
                    putWhere();
                    query+="z.nazwa LIKE '%"+zak+"%'";
                }
            }
            if(miast!=null && !miast.isEmpty()){
                putWhere();
                query+="miasto LIKE '%"+miast+"%'";
            }
            if(ul!=null && !ul.isEmpty()){
                putWhere();
                query+="ulica LIKE '%"+ul+"%'";
            }
            if(nrDom!=null && !nrDom.isEmpty()){
                putWhere();
                query+="nr_domu='"+nrDom+"'";
            }

            wyszukaneParafie=stmt.executeQuery(query);
            insertParafie();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    public void wypelnijCombo(){
        try {
            Statement stmt = connection.createStatement();
            String query= "SELECT * FROM zakony ORDER BY nazwa";
            ResultSet rs=stmt.executeQuery(query);

            zakon.getItems().add("Każdy");
            zakon.getItems().add("Bez zakonu");

            while (rs.next()){
                zakon.getItems().add(rs.getString("nazwa"));
            }

            zakon.getSelectionModel().selectFirst();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    String znajdzAdres(String mia, String ul, String nrDom) throws Exception{
        Statement stmt;
        ResultSet rs;
        query="SELECT * FROM ADRESY WHERE ";
        query+="miasto='" + mia + "'";
        query+=" AND ulica='" + ul + "'";
        query+= " AND nr_domu='" + nrDom + "'";

        stmt = connection.createStatement();
        rs= stmt.executeQuery(query);
        String nAd=null;
        while (rs.next()){
            nAd=rs.getString("id_adresu");
            break;
        }

        if(nAd==null){
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
            nAd=nId.toString();
        }

        return nAd;
    }

    @FXML
    void showPrafianie(ActionEvent event) {
        try {
            Statement stmt = connection.createStatement();

            String idPar=basicParafie.getSelectionModel().getSelectedItem().id_parafii.getValue().toString();

            query="SELECT \n" +
                    "p.id_osoby AS \"id_osoby\",\n" +
                    "p.imie AS \"imie\",\n" +
                    "p.nazwisko AS \"nazwisko\",\n" +
                    "p.data_narodzin AS \"data_narodzin\",\n" +
                    "p.data_zgonu AS \"data_zgonu\"\n" +
                    "\tFROM parafianie p LEFT JOIN historia_parafian hp ON p.id_osoby=hp.id_osoby\n" +
                    "\tWHERE hp.data_odejscia IS NULL\n" +
                    "\tAND hp.id_parafii= '"+ idPar + "'";

            BasicParafianie.wyszukaniParafianie=stmt.executeQuery(query);
            BasicParafianie.czyPrep=false;
            replaceSceneContent("FXML/basicParafianie.fxml");
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void dodajParafie() throws Exception{
        if(zakon.getSelectionModel().getSelectedItem().equals("Każdy")){
            throw new Exception("Należy wybrać odpowiedni zakon");
        }
        if(nazwa.getText()==null || nazwa.getText().isEmpty()){
            throw new Exception("Należy podać nazwę parafii");
        }
        String mia=miasto.getText();
        String ul=ulica.getText();
        String nrDom=nr_domu.getText();
        if(mia==null || mia.isEmpty() || ul==null || ul.isEmpty() || nrDom==null || nrDom.isEmpty()){
            throw new Exception("Należy podać odpowiedni adres");
        }
        String aId=znajdzAdres(miasto.getText(), ulica.getText(), nr_domu.getText());
        Statement stmt;
        ResultSet rs;

        query="INSERT INTO parafie(nazwa, zakon, id_adresu) VALUES (";
        query+="'"+nazwa.getText()+"'";
        query+=",";
        if(zakon.getSelectionModel().getSelectedItem().equals("Bez zakonu")){
            query+="null";
        }
        else {
            String zId="0"; //id zakonu
            String pq="SELECT * FROM zakony WHERE nazwa = '";
            pq+=zakon.getSelectionModel().getSelectedItem()+"'";
            stmt=connection.createStatement();
            rs=stmt.executeQuery(pq);
            while(rs.next()){
                zId=rs.getString("id_zakonu");
                break;
            }
            query+="'"+zId+"'";
        }
        query+=",";
        query+="'"+aId+"')";
        stmt=connection.createStatement();
        stmt.executeUpdate(query);

    }

    void modyfikujParafie() throws Exception{
        if(zakon.getSelectionModel().getSelectedItem().equals("Każdy")){
            throw new Exception("Należy wybrać odpowiedni zakon");
        }

        String mia=miasto.getText();
        String ul=ulica.getText();
        String nrDom=nr_domu.getText();

        Statement stmt;
        ResultSet rs;

        String sAd="";
        query="SELECT * FROM parafie WHERE id_parafii='";
        query+=id_parafii.getText()+"'";
        stmt=connection.createStatement();
        rs=stmt.executeQuery(query);
        while(rs.next()) {
            sAd=rs.getString("id_adresu");
            break;
        }
        query="SELECT * FROM adresy WHERE id_adresu='";
        query+=sAd+"'";
        stmt=connection.createStatement();
        rs=stmt.executeQuery(query);

        while(rs.next()) {
            if(mia==null || mia.isEmpty()) {
                mia=rs.getString("miasto");
            }
            if(ul==null || ul.isEmpty() ) {
                ul = rs.getString("ulica");
            }
            if(nrDom==null || nrDom.isEmpty() ) {
                nrDom = rs.getString("nr_domu");
            }
            break;
        }
        String aId=znajdzAdres(mia, ul, nrDom);

        query="UPDATE parafie SET ";
        query+="id_adresu='"+aId+"'";
        if (nazwa.getText()!=null && !nazwa.getText().isEmpty()){
            query+=",";
            query+="nazwa='"+nazwa.getText()+"'";
        }
        query+=",zakon=";
        if(zakon.getSelectionModel().getSelectedItem().equals("Bez zakonu")){
            query+="null";
        }
        else {
            String zId="0"; //id zakonu
            String pq="SELECT * FROM zakony WHERE nazwa = '";
            pq+=zakon.getSelectionModel().getSelectedItem()+"'";
            stmt=connection.createStatement();
            rs=stmt.executeQuery(pq);
            while(rs.next()){
                zId=rs.getString("id_zakonu");
                break;
            }
            query+="'"+zId+"'";
        }
        query+=" WHERE id_parafii='"+id_parafii.getText()+"'";
        stmt=connection.createStatement();
        stmt.executeUpdate(query);
    }

    @FXML
    void edytujParafie(ActionEvent event){
        try {
            if(id_parafii.getText()==null || id_parafii.getText().isEmpty()){
                dodajParafie();
            }
            else {
                modyfikujParafie();
            }
            wyszukajParafie(event);
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void wklepParafie(){
        id_parafii.setText(basicParafie.getSelectionModel().getSelectedItem().id_parafii.getValue().toString());
        zakon.getSelectionModel().select(basicParafie.getSelectionModel().getSelectedItem().zakon.getValue());
    }


    @FXML
    void showPracownicy(ActionEvent event) {
        BasicPracownicy.parafia=basicParafie.getSelectionModel().getSelectedItem().id_parafii.getValue().toString();
        try {
            replaceSceneContent("FXML/basicPracownicy.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void initialize() {
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert id_pariafiiColumn != null : "fx:id=\"id_pariafiiColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert nazwaColumn != null : "fx:id=\"nazwaColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert zakonColumn != null : "fx:id=\"zakonColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'basicParafie.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'basicParafie.fxml'.";

        wypelnijCombo();

        if(czyPrep){
            prepParafie();
        }
        else {
            czyPrep=true;
        }
        insertParafie();
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
