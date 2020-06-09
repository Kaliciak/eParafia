package eParafia.Controller;

import eParafia.Main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static eParafia.Controller.Dane.*;

public class SakramentView {
    static SakramentyRow currentSakrament;
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
    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private Text sakrName;

    @FXML
    private Text dataName;

    @FXML
    private Text parafiaName;

    public class SakramentParticipant {
        SimpleIntegerProperty id;
        SimpleStringProperty imie;
        SimpleStringProperty nazwisko;
        SimpleStringProperty typ_uczestnictwa;
        public SakramentParticipant(int id, String imie, String nazwisko, String typ_uczestnictwa) {
            this.id=new SimpleIntegerProperty(id);
            this.imie = new SimpleStringProperty(imie);
            this.nazwisko=new SimpleStringProperty(nazwisko);
            this.typ_uczestnictwa=new SimpleStringProperty(typ_uczestnictwa);
        }

        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public String getImie() {
            return imie.get();
        }

        public SimpleStringProperty imieProperty() {
            return imie;
        }

        public void setImie(String imie) {
            this.imie.set(imie);
        }

        public String getNazwisko() {
            return nazwisko.get();
        }

        public SimpleStringProperty nazwiskoProperty() {
            return nazwisko;
        }

        public void setNazwisko(String nazwisko) {
            this.nazwisko.set(nazwisko);
        }

        public String getTyp_uczestnictwa() {
            return typ_uczestnictwa.get();
        }

        public SimpleStringProperty typ_uczestnictwaProperty() {
            return typ_uczestnictwa;
        }

        public void setTyp_uczestnictwa(String typ_uczestnictwa) {
            this.typ_uczestnictwa.set(typ_uczestnictwa);
        }
    }

    @FXML
    private TableView<SakramentParticipant> participants;

    @FXML
    private TableColumn<SakramentParticipant, Integer> idColumn;

    @FXML
    private TableColumn<SakramentParticipant, String> imieColumn;

    @FXML
    private TableColumn<SakramentParticipant, String> nazwiskoColumn;

    @FXML
    private TableColumn<SakramentParticipant, String> uczestnictwoColumn;

    ObservableList<SakramentParticipant> sakramentPersons = FXCollections.observableArrayList();
    @FXML
    private Text szafarzName;

    @FXML
    private Text szafarzSurname;

    @FXML
    void add(ActionEvent event) {
        ArrayList<FullParafianieEntity> toAdd=ChooseParafianie.selectParafianie("select * from parafianie p where not exists(select * from sakramenty_osoby s where typ_uczestnictwa='UCZESTNIK' and s.id_osoby=p.id_osoby and id_sakramentu = "+currentSakrament.getId()+")");
        String query="insert into sakramenty_osoby values ";
        if(toAdd.size()==0) return;
        query+=toAdd.stream().map(pp ->"("+currentSakrament.getId()+", "+pp.getId_osoby()+", 'UCZESTNIK' )").collect(Collectors.joining(", "));
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            showErrorWindow(e);
        }
        sakramentPersons.clear();
        initialize();
    }
    @FXML
    void add_gosc(ActionEvent event) {
        ArrayList<FullParafianieEntity> toAdd=ChooseParafianie.selectParafianie("select * from parafianie p where not exists(select * from sakramenty_osoby s where typ_uczestnictwa='GOŚĆ' and s.id_osoby=p.id_osoby and id_sakramentu = "+currentSakrament.getId()+")");
        String query="insert into sakramenty_osoby values ";
        if(toAdd.size()==0) return;
        query+=toAdd.stream().map(pp ->"("+currentSakrament.getId()+", "+pp.getId_osoby()+", 'GOŚĆ' )").collect(Collectors.joining(", "));
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            showErrorWindow(e);
        }
        sakramentPersons.clear();
        initialize();
    }
    @FXML
    void add_swiadek(ActionEvent event) {
        ArrayList<FullParafianieEntity> toAdd=ChooseParafianie.selectParafianie("select * from parafianie p where not exists(select * from sakramenty_osoby s where typ_uczestnictwa='ŚWIADEK' and s.id_osoby=p.id_osoby and id_sakramentu = "+currentSakrament.getId()+")");
        String query="insert into sakramenty_osoby values ";
        if(toAdd.size()==0) return;
        query+=toAdd.stream().map(pp ->"("+currentSakrament.getId()+", "+pp.getId_osoby()+", 'ŚWIADEK' )").collect(Collectors.joining(", "));
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            showErrorWindow(e);
        }
        sakramentPersons.clear();
        initialize();
    }

    @FXML
    void delete_part(ActionEvent event) {
        ArrayList<FullParafianieEntity> toDelete = ChooseParafianie.selectParafianie("select * from parafianie p where exists(select * from sakramenty_osoby s where p.id_osoby=s.id_osoby and id_sakramentu="+currentSakrament.getId()+")");
        if(toDelete.size()==0) return;
        String query ="delete from sakramenty_osoby where id_sakramentu="+currentSakrament.getId()+"and id_osoby in (";
        query+=toDelete.stream().map(pp-> Integer.toString(pp.getId_osoby())).collect(Collectors.joining(", "));
        query+=")";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
        } catch (Exception e) {
            showErrorWindow(e);
        }
        sakramentPersons.clear();
        initialize();
    }

    @FXML
    void delete(ActionEvent event) {
        if(Sakramenty.delete_sakrament(currentSakrament.getId())) returnToSakramenty(null);
    }
    @FXML
    void edit(ActionEvent event) {
        SakramentyRow ret = SakramentAdd.getSakramentDataFromUser(currentSakrament, szafarzName.getText().substring(5) + szafarzSurname.getText().substring(9));
        if (ret != null) {
            String query = "update sakramenty set (parafia,data,id_szafarza)=("+ret.getParafia()+", '"+new Timestamp(ret.getData().getTime()).toString()+"', "+ (ret.getId_szafarza()>0 ? Integer.toString(ret.getId_szafarza()) : "null")+") where id_sakramentu= "+ret.getId();
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(query);
                currentSakrament=ret;
                initialize();
            } catch (Exception e) {
                showErrorWindow(e);
            }
        }
    }

    @FXML
    void intencje(ActionEvent event) {
        try {
            replaceSceneContent("FXML/intencjeData.fxml");
        }catch (Exception e){
            e.printStackTrace();
            showErrorWindow(e);
        }
    }
    @FXML
    void returnToSakramenty(ActionEvent event) {
        try{
            replaceSceneContent("FXML/sakramenty.fxml");
        } catch (Exception e) {}
    }
    @FXML
    void initialize() {
        if(currentSakrament!=null) {
            sakrName.setText("Sakrament: "+currentSakrament.getSakrament());
            dataName.setText("Data: "+currentSakrament.getData().toString());
            parafiaName.setText("Parafia: "+currentSakrament.getParafiaName());
            try{

                Statement stmt = connection.createStatement();
                String query=
                        "select * from parafianie where id_osoby="+currentSakrament.getId_szafarza();
                ResultSet rs=stmt.executeQuery(query);
                if(rs.next()) {
                    szafarzName.setText("Imię: "+rs.getString("imie"));
                    szafarzSurname.setText("Nazwisko: "+rs.getString("nazwisko"));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            sakramentPersons.clear();
            try {
                Statement stmt = connection.createStatement();
                String query = "select id_osoby, imie, nazwisko, typ_uczestnictwa from sakramenty_osoby left outer join parafianie using(id_osoby) where id_sakramentu="+currentSakrament.getId();
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    sakramentPersons.add(new SakramentParticipant(
                            rs.getInt("id_osoby"),
                            rs.getString("imie"),
                            rs.getString("nazwisko"),
                            rs.getString("typ_uczestnictwa")
                     ));
                }
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                imieColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
                nazwiskoColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
                uczestnictwoColumn.setCellValueFactory(new PropertyValueFactory<>("typ_uczestnictwa"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            participants.setItems(sakramentPersons);
        }
    }
}
