package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class AdvancedParafianie {

    ObservableList<AdvancedParafianieRow> parafianieRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaniParafianie;
    public static boolean czyPrep=true;
    boolean isWhere;
    String query;

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
    private DatePicker data_narodzin_od;

    @FXML
    private DatePicker data_narodzin_do;

    @FXML
    private TextField imie;

    @FXML
    private TextField imie_z_bierzmowania;

    @FXML
    private DatePicker data_zgonu_od;

    @FXML
    private DatePicker data_zgonu_do;

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


    void prepParafianie(){
        try {
            Statement stmt = connection.createStatement();

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
                    " ORDER BY p.id_osoby";

            wyszukaniParafianie=stmt.executeQuery(query);;
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
    public void wyszukajOsobe(){
        String idOs=id_osoby.getText();
        String im=imie.getText();
        String dIm=drugie_imie.getText();
        String imZB=imie_z_bierzmowania.getText();
        String naz=nazwisko.getText();
        LocalDate dNarOd=data_narodzin_od.getValue();
        LocalDate dNarDo=data_narodzin_do.getValue();
        LocalDate dZgOd=data_zgonu_od.getValue();
        LocalDate dZgDo=data_zgonu_do.getValue();
        String mia=miasto.getText();
        String ul=ulica.getText();
        String nrDom=nr_domu.getText();
        String idPar=id_parafii.getText();
        String idOj=id_ojca.getText();
        String idMat=id_matki.getText();
        String idOjCh=id_ojca_chrzestnego.getText();
        String idMatCh=id_matki_chrzestnej.getText();

        isWhere=true;

        try {
            Statement stmt = connection.createStatement();
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
                    "\tWHERE hp.data_odejscia IS NULL";

            if(idOs!=null && !idOs.isEmpty()){
                putWhere();
                query+="p.id_osoby='"+idOs+"'";
            }
            if(im!=null && !im.isEmpty()){
                putWhere();
                query+="p.imie LIKE '%"+im+"%'";
            }
            if(dIm!=null && !dIm.isEmpty()){
                putWhere();
                query+="p.drugie_imie LIKE '%"+dIm+"%'";
            }
            if(imZB!=null && !imZB.isEmpty()){
                putWhere();
                query+="p.imie_z_bierzmowania LIKE '%"+imZB+"%'";
            }
            if(naz!=null && !naz.isEmpty()){
                putWhere();
                query+="p.nazwisko LIKE '%"+naz+"%'";
            }
            if(dNarOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM p.data_narodzin)='"+dNarOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM p.data_narodzin)='"+dNarOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM p.data_narodzin)='"+dNarOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "p.data_narodzin>'" + dNarOd + "'))";
            }
            if(dNarDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM p.data_narodzin)='"+dNarDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM p.data_narodzin)='"+dNarDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM p.data_narodzin)='"+dNarDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "p.data_narodzin<'" + dNarDo + "'))";
            }
            if(dZgOd!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM p.data_zgonu)='"+dZgOd.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM p.data_zgonu)='"+dZgOd.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM p.data_zgonu)='"+dZgOd.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "p.data_zgonu>'" + dZgOd + "'))";
            }
            if(dZgDo!=null){
                putWhere();
                query+="((" +
                        " EXTRACT(DAY FROM p.data_zgonu)='"+dZgDo.getDayOfMonth()+"'" +
                        " AND " +
                        "EXTRACT(MONTH FROM p.data_zgonu)='"+dZgDo.getMonthValue()+"'" +
                        " AND " +
                        "EXTRACT(YEAR FROM p.data_zgonu)='"+dZgDo.getYear()+"'" +
                        ")" +
                        " OR (" +
                        "p.data_zgonu<'" + dZgDo + "'))";
            }
            if(mia!=null && !mia.isEmpty()){
                putWhere();
                query+="a.miasto LIKE '%"+mia+"%'";
            }
            if(ul!=null && !ul.isEmpty()){
                putWhere();
                query+="a.ulica LIKE '%"+ul+"%'";
            }
            if(nrDom!=null && !nrDom.isEmpty()){
                putWhere();
                query+="a.nr_domu = '"+nrDom+"'";
            }
            if(idPar!=null && !idPar.isEmpty()){
                putWhere();
                query+="hp.id_parafii = '"+idPar+"'";
            }
            if(idOj!=null && !idOj.isEmpty()){
                putWhere();
                query+="p.id_ojca = '"+idOj+"'";
            }
            if(idOjCh!=null && !idOjCh.isEmpty()){
                putWhere();
                query+="p.id_ojca_chrzestnego = '"+idOjCh+"'";
            }
            if(idMat!=null && !idMat.isEmpty()){
                putWhere();
                query+="p.id_matki= '"+idMat+"'";
            }
            if(idMatCh!=null && !idMatCh.isEmpty()){
                putWhere();
                query+="p.id_matki_chrzestnej = '"+idMatCh+"'";
            }

            wyszukaniParafianie=stmt.executeQuery(query);
            insertParafianie();
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
                parafianieRows.add(new AdvancedParafianieRow(
                        rs.getInt("id_osoby"),
                        rs.getString("imie"),
                        rs.getString("drugie_imie"),
                        rs.getString("imie_z_bierzmowania"),
                        rs.getString("nazwisko"),
                        rs.getString("plec"),
                        rs.getDate("data_narodzin"),
                        rs.getDate("data_zgonu"),
                        rs.getString("miasto"),
                        rs.getString("ulica"),
                        rs.getString("nr_domu"),
                        rs.getInt("id_parafii"),
                        rs.getInt("id_ojca"),
                        rs.getInt("id_matki"),
                        rs.getInt("id_ojca_chrzestnego"),
                        rs.getInt("id_matki_chrzestnej")
                ));
            }


            id_osobyColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
            imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
            drugie_imieColumn.setCellValueFactory(new PropertyValueFactory<>("drugie_imie"));
            imie_z_bierzmowaniaColumn.setCellValueFactory(new PropertyValueFactory<>("imie_z_bierzmowania"));
            nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            plecColumn.setCellValueFactory(new PropertyValueFactory<>("plec"));
            data_narodzinColumn.setCellValueFactory(new PropertyValueFactory<>("data_narodzin"));
            data_zgonuColumn.setCellValueFactory(new PropertyValueFactory<>("data_zgonu"));
            miastoColumn.setCellValueFactory(new PropertyValueFactory<>("miasto"));
            ulicaColumn.setCellValueFactory(new PropertyValueFactory<>("ulica"));
            nr_domuColumn.setCellValueFactory(new PropertyValueFactory<>("nr_domu"));
            id_parafiiColumn.setCellValueFactory(new PropertyValueFactory<>("id_parafii"));
            id_ojcaColumn.setCellValueFactory(new PropertyValueFactory<>("id_ojca"));
            id_matkiColumn.setCellValueFactory(new PropertyValueFactory<>("id_matki"));
            id_ojca_chrzestnegoColumn.setCellValueFactory(new PropertyValueFactory<>("id_ojca_chrzestnego"));
            id_matki_chrzestnejColumn.setCellValueFactory(new PropertyValueFactory<>("id_matki_chrzestnej"));

            basicParafie.setItems(parafianieRows);
        }
        catch (Exception e){
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
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_osobyColumn != null : "fx:id=\"id_osobyColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert imieColumn != null : "fx:id=\"imieColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert drugie_imieColumn != null : "fx:id=\"drugie_imieColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert imie_z_bierzmowaniaColumn != null : "fx:id=\"imie_z_bierzmowaniaColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert nazwiskoColumn != null : "fx:id=\"nazwiskoColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert plecColumn != null : "fx:id=\"plecColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert data_narodzinColumn != null : "fx:id=\"data_narodzinColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert data_zgonuColumn != null : "fx:id=\"data_zgonuColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_parafiiColumn != null : "fx:id=\"id_parafiiColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_ojcaColumn != null : "fx:id=\"id_ojcaColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_matkiColumn != null : "fx:id=\"id_matkiColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_ojca_chrzestnegoColumn != null : "fx:id=\"id_ojca_chrzestnegoColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";
        assert id_matki_chrzestnejColumn != null : "fx:id=\"id_matki_chrzestnejColumn\" was not injected: check your FXML file 'advancedParafianie.fxml'.";

        if(czyPrep){
            prepParafianie();
        }
        else {
            czyPrep=true;
        }
        insertParafianie();
    }
}
