package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import static eParafia.Controller.Dane.*;

public class ModifyParafianin {
    ObservableList<AdvancedParafianieRow> parafianieRows= FXCollections.observableArrayList();
    public static ResultSet wyszukaniParafianie;
    public static boolean czyPrep=true;
    boolean isWhere;
    String query;
    public static String prevNum=null;

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
    private DatePicker data_narodzin;

    @FXML
    private TextField imie;

    @FXML
    private TextField imie_z_bierzmowania;

    @FXML
    private DatePicker data_zgonu;

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

    @FXML
    private ComboBox<String> plec;

    public void wypelnijCombo(){
        try {
            plec.getItems().add("M");
            plec.getItems().add("K");
            plec.getSelectionModel().selectFirst();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    void dodajOsobe(){
        try {
            Statement stmt = connection.createStatement();
            //znajdowanie adresu
            String mia=miasto.getText();
            String ul=ulica.getText();
            String nrDom=nr_domu.getText();
            if(mia==null || mia.isEmpty() || ul==null || ul.isEmpty() || nrDom==null || nrDom.isEmpty()){
                throw new Exception("Nalezy podać adres");
            }

            query="SELECT * FROM ADRESY WHERE ";
            query+="miasto='" + mia + "'";
            query+=" AND ulica='" + ul + "'";
            query+= " AND nr_domu='" + nrDom + "'";

            stmt = connection.createStatement();
            ResultSet rs= stmt.executeQuery(query);

            String aId=null; //id adresu
            while (rs.next()){
                aId=rs.getString("id_adresu");
            }

            if(aId==null){
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
                aId=nId.toString();
            }

            query="INSERT INTO parafianie (";
            query+="id_adresu";
            if(imie.getText()!=null && !imie.getText().isEmpty()){
                query+=",";
                query+="imie";
            }
            if(drugie_imie.getText()!=null && !drugie_imie.getText().isEmpty()){
                query+=",";
                query+="drugie_imie";
            }
            if(imie_z_bierzmowania.getText()!=null && !imie_z_bierzmowania.getText().isEmpty()){
                query+=",";
                query+="imie_z_bierzmowania";
            }
            if(nazwisko.getText()!=null && !nazwisko.getText().isEmpty()){
                query+=",";
                query+="nazwisko";
            }
            if(plec.getSelectionModel().getSelectedItem()!=null && !plec.getSelectionModel().getSelectedItem().isEmpty()){
                query+=",";
                query+="plec";
            }
            if(data_narodzin.getValue()!=null){
                query+=",";
                query+="data_narodzin";
            }
            if(data_zgonu.getValue()!=null){
                query+=",";
                query+="data_zgonu";
            }
            if(id_ojca.getText()!=null && !id_ojca.getText().isEmpty()){
                query+=",";
                query+="id_ojca";
            }
            if(id_matki.getText()!=null && !id_matki.getText().isEmpty()){
                query+=",";
                query+="id_matki";
            }
            if(id_ojca_chrzestnego.getText()!=null && !id_ojca_chrzestnego.getText().isEmpty()){
                query+=",";
                query+="id_ojca_chrzestnego";
            }
            if(id_matki_chrzestnej.getText()!=null && !id_matki_chrzestnej.getText().isEmpty()){
                query+=",";
                query+="id_matki_chrzestnej";
            }
            query+=") VALUES (";
            ////wstawianie

            query+="'"+aId+"'";
            if(imie.getText()!=null && !imie.getText().isEmpty()){
                query+=",";
                query+="'"+imie.getText()+"'";
            }
            if(drugie_imie.getText()!=null && !drugie_imie.getText().isEmpty()){
                query+=",";
                query+="'"+drugie_imie.getText()+"'";
            }
            if(imie_z_bierzmowania.getText()!=null && !imie_z_bierzmowania.getText().isEmpty()){
                query+=",";
                query+="'"+imie_z_bierzmowania.getText()+"'";
            }
            if(nazwisko.getText()!=null && !nazwisko.getText().isEmpty()){
                query+=",";
                query+="'"+nazwisko.getText()+"'";
            }
            if(plec.getSelectionModel().getSelectedItem()!=null && !plec.getSelectionModel().getSelectedItem().isEmpty()){
                query+=",";
                query+="'"+plec.getSelectionModel().getSelectedItem()+"'";
            }
            if(data_narodzin.getValue()!=null){
                query+=",";
                query+="'"+data_narodzin.getValue()+"'";
            }
            if(data_zgonu.getValue()!=null){
                query+=",";
                query+="'"+data_zgonu.getValue()+"'";
            }
            if(id_ojca.getText()!=null && !id_ojca.getText().isEmpty()){
                query+=",";
                query+="'"+id_ojca.getText()+"'";
            }
            if(id_matki.getText()!=null && !id_matki.getText().isEmpty()){
                query+=",";
                query+="'"+id_matki.getText()+"'";
            }
            if(id_ojca_chrzestnego.getText()!=null && !id_ojca_chrzestnego.getText().isEmpty()){
                query+=",";
                query+="'"+id_ojca_chrzestnego.getText()+"'";
            }
            if(id_matki_chrzestnej.getText()!=null && !id_matki_chrzestnej.getText().isEmpty()){
                query+=",";
                query+="'"+id_matki_chrzestnej.getText()+"'";
            }
            query+=")";
            System.out.println(query);

            stmt = connection.createStatement();
            stmt.executeUpdate(query);

            //wstawianie do historii parafian

            Integer newParafianinId=0;
            stmt = connection.createStatement();
            query="select max(id_osoby) AS \"id\" from parafianie";
            rs=stmt.executeQuery(query);
            while (rs.next()){
                newParafianinId=rs.getInt("id");
            }

            if(id_parafii.getText()!=null && !id_parafii.getText().isEmpty()){
                query="INSERT INTO historia_parafian(id_osoby, id_parafii, apostazja)\n" +
                        "VALUES\n" +
                        "\t(";
                query+="'"+newParafianinId+"',";
                query+="'"+id_parafii.getText()+"',";
                query+="false)";
                stmt = connection.createStatement();
                stmt.executeUpdate(query);
            }

            showPar(newParafianinId.toString());

        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    void showPar(String parId){
        try {
            Statement stmt = connection.createStatement();
            query = "SELECT \n" +
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
                    " AND p.id_osoby='" + parId +"'";

            wyszukaniParafianie=stmt.executeQuery(query);
            insertParafianie();
        }catch (Exception e){
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

    void zmodyfikujOsobe(){
        try {
            Statement stmt = connection.createStatement();
            //znajdowanie adresu

            query="SELECT * FROM parafianie WHERE ";
            query+="id_osoby='" + id_osoby.getText() + "'";

            stmt = connection.createStatement();
            ResultSet rs= stmt.executeQuery(query);

            Integer idPar=null;
            String aId=null; //id adresu
            while (rs.next()){
                idPar=rs.getInt("id_osoby");
                aId=rs.getString("id_adresu");
                break;
            }
            if(idPar==null){
                throw new Exception("Brak parafianina o podanym id");
            }

            query="SELECT * FROM adresy WHERE ";
            query+="id_adresu='" + aId + "'";

            stmt = connection.createStatement();
            rs= stmt.executeQuery(query);

            String mia=miasto.getText();
            String ul=ulica.getText();
            String nrDom=nr_domu.getText();

            while (rs.next()){
                if(mia==null || mia.isEmpty()){
                    mia=rs.getString("miasto");
                }
                if(ul==null || ul.isEmpty()){
                    ul=rs.getString("ulica");
                }
                if(nrDom==null || nrDom.isEmpty()){
                    nrDom=rs.getString("nr_domu");
                }
            }

            query="SELECT * FROM ADRESY WHERE ";
            query+="miasto='" + mia + "'";
            query+=" AND ulica='" + ul + "'";
            query+= " AND nr_domu='" + nrDom + "'";

            stmt = connection.createStatement();
            rs= stmt.executeQuery(query);

            while (rs.next()){
                aId=rs.getString("id_adresu");
            }

            if(aId==null){
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
                aId=nId.toString();
            }

            query="UPDATE parafianie SET ";
            query+="id_adresu='"+aId+"'";
            if(imie.getText()!=null && !imie.getText().isEmpty()){
                query+=",";
                query+="imie='"+imie.getText()+"'";;
            }
            if(drugie_imie.getText()!=null && !drugie_imie.getText().isEmpty()){
                query+=",";
                query+="drugie_imie='"+drugie_imie.getText()+"'";;
            }
            if(imie_z_bierzmowania.getText()!=null && !imie_z_bierzmowania.getText().isEmpty()){
                query+=",";
                query+="imie_z_bierzmowania='"+imie_z_bierzmowania.getText()+"'";;
            }
            if(nazwisko.getText()!=null && !nazwisko.getText().isEmpty()){
                query+=",";
                query+="nazwisko='"+nazwisko.getText()+"'";;
            }
            if(plec.getSelectionModel().getSelectedItem()!=null && !plec.getSelectionModel().getSelectedItem().isEmpty()){
                query+=",";
                query+="plec='"+plec.getSelectionModel().getSelectedItem()+"'";;
            }
            if(data_narodzin.getValue()!=null){
                query+=",";
                query+="data_narodzin='"+data_narodzin.getValue()+"'";;
            }
            if(data_zgonu.getValue()!=null){
                query+=",";
                query+="data_zgonu='"+data_zgonu.getValue()+"'";
            }
            if(id_ojca.getText()!=null && !id_ojca.getText().isEmpty()){
                query+=",";
                query+="id_ojca='"+id_ojca.getText()+"'";
            }
            if(id_matki.getText()!=null && !id_matki.getText().isEmpty()){
                query+=",";
                query+="id_matki='"+id_matki.getText()+"'";
            }
            if(id_ojca_chrzestnego.getText()!=null && !id_ojca_chrzestnego.getText().isEmpty()){
                query+=",";
                query+="id_ojca_chrzestnego='"+id_ojca_chrzestnego.getText()+"'";
            }
            if(id_matki_chrzestnej.getText()!=null && !id_matki_chrzestnej.getText().isEmpty()){
                query+=",";
                query+="id_matki_chrzestnej='"+id_matki_chrzestnej.getText()+"'";
            }

            query+=" WHERE id_osoby='"+idPar+"'";

            stmt = connection.createStatement();
            stmt.executeUpdate(query);

            //wstawianie do historii parafian

            query="SELECT * FROM historia_parafian WHERE data_odejscia IS NULL AND id_osoby='"+idPar+"'";
            stmt = connection.createStatement();
            rs=stmt.executeQuery(query);

            String idParafii="";
            while (rs.next()){
                idParafii=rs.getString("id_parafii");
                break;
            }
            if(id_parafii.getText()!=null && !id_parafii.getText().isEmpty() && !idParafii.equals(id_parafii.getText())){
                query="UPDATE historia_parafian\n" +
                        "\tSET data_odejscia=now()\n" +
                        "\tWHERE id_osoby='"+idPar+"'";
                stmt = connection.createStatement();
                stmt.executeUpdate(query);


                query="INSERT INTO historia_parafian(id_osoby, id_parafii, apostazja)\n" +
                        "VALUES\n" +
                        "\t(";
                query+="'"+idPar+"',";
                query+="'"+id_parafii.getText()+"',";
                query+="false)";
                stmt = connection.createStatement();
                stmt.executeUpdate(query);
            }

            showPar(idPar.toString());

        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void zmienOsobe(ActionEvent event) {
        if(id_osoby.getText()!=null && !id_osoby.getText().isEmpty()){
            zmodyfikujOsobe();
        }
        else {
            dodajOsobe();
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
        assert basicParafie != null : "fx:id=\"basicParafie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_osobyColumn != null : "fx:id=\"id_osobyColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imieColumn != null : "fx:id=\"imieColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert drugie_imieColumn != null : "fx:id=\"drugie_imieColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie_z_bierzmowaniaColumn != null : "fx:id=\"imie_z_bierzmowaniaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nazwiskoColumn != null : "fx:id=\"nazwiskoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert plecColumn != null : "fx:id=\"plecColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_narodzinColumn != null : "fx:id=\"data_narodzinColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_zgonuColumn != null : "fx:id=\"data_zgonuColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert miastoColumn != null : "fx:id=\"miastoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert ulicaColumn != null : "fx:id=\"ulicaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nr_domuColumn != null : "fx:id=\"nr_domuColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_parafiiColumn != null : "fx:id=\"id_parafiiColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojcaColumn != null : "fx:id=\"id_ojcaColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matkiColumn != null : "fx:id=\"id_matkiColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca_chrzestnegoColumn != null : "fx:id=\"id_ojca_chrzestnegoColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki_chrzestnejColumn != null : "fx:id=\"id_matki_chrzestnejColumn\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert drugie_imie != null : "fx:id=\"drugie_imie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_osoby != null : "fx:id=\"id_osoby\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nazwisko != null : "fx:id=\"nazwisko\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_narodzin != null : "fx:id=\"data_narodzin\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie != null : "fx:id=\"imie\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert imie_z_bierzmowania != null : "fx:id=\"imie_z_bierzmowania\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert data_zgonu != null : "fx:id=\"data_zgonu\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca != null : "fx:id=\"id_ojca\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_ojca_chrzestnego != null : "fx:id=\"id_ojca_chrzestnego\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki != null : "fx:id=\"id_matki\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert ulica != null : "fx:id=\"ulica\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_matki_chrzestnej != null : "fx:id=\"id_matki_chrzestnej\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert miasto != null : "fx:id=\"miasto\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert nr_domu != null : "fx:id=\"nr_domu\" was not injected: check your FXML file 'modifyParafianin.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'modifyParafianin.fxml'.";

        wypelnijCombo();

        if(prevNum!=null && !prevNum.isEmpty()){
            id_osoby.setText(prevNum);
            showPar(prevNum);
            plec.getSelectionModel().select(basicParafie.getItems().get(0).plec.getValue());
            prevNum=null;
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
