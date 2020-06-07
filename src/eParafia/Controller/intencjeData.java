package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import static eParafia.Controller.Dane.*;
public class intencjeData {
    static public class IntencjaEntity {
        static public ArrayList<IntencjaEntity> getAllFromSakrament(int id_sakramentu) throws Exception {
            ArrayList<IntencjaEntity> result=new ArrayList<>();
            Statement stms = connection.createStatement();
            ResultSet rs = stms.executeQuery("select i.*, imie||' '||nazwisko as nazwa from intencje_mszy i left outer join parafianie on(id_zamawiajacego=id_osoby) where id_mszy="+id_sakramentu);
            while(rs.next()) {
                result.add(new IntencjaEntity(rs.getInt("id_intencji"),rs.getString("intencja"),rs.getInt("id_zamawiajacego"),rs.getString("nazwa")));
            }
            return result;
        }
        SimpleIntegerProperty id_intencji;
        SimpleStringProperty intencja;
        SimpleIntegerProperty id_zamawiajacage;
        SimpleStringProperty imie_zamawiajacego;
        public IntencjaEntity(int id_intencji,String intencja, int id_zamawiajacego, String imie) {
            this.id_intencji = new SimpleIntegerProperty(id_intencji);
            this.intencja=new SimpleStringProperty(intencja);
            this.id_zamawiajacage=new SimpleIntegerProperty(id_zamawiajacego);
            imie_zamawiajacego=new SimpleStringProperty(imie);
        }

        public int getId_intencji() {
            return id_intencji.get();
        }

        public SimpleIntegerProperty id_intencjiProperty() {
            return id_intencji;
        }

        public void setId_intencji(int id_intencji) {
            this.id_intencji.set(id_intencji);
        }

        public String getIntencja() {
            return intencja.get();
        }

        public SimpleStringProperty intencjaProperty() {
            return intencja;
        }

        public void setIntencja(String intencja) {
            this.intencja.set(intencja);
        }

        public int getId_zamawiajacage() {
            return id_zamawiajacage.get();
        }

        public SimpleIntegerProperty id_zamawiajacageProperty() {
            return id_zamawiajacage;
        }

        public void setId_zamawiajacage(int id_zamawiajacage) {
            this.id_zamawiajacage.set(id_zamawiajacage);
        }

        public String getImie_zamawiajacego() {
            return imie_zamawiajacego.get();
        }

        public SimpleStringProperty imie_zamawiajacegoProperty() {
            return imie_zamawiajacego;
        }

        public void setImie_zamawiajacego(String imie_zamawiajacego) {
            this.imie_zamawiajacego.set(imie_zamawiajacego);
        }
    }

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TableView<IntencjaEntity> table;

    @FXML
    private TableColumn<IntencjaEntity, String> intencjaColumn;

    @FXML
    private TableColumn<IntencjaEntity, String> zamawiajacyColumn;

    @FXML
    private Text sakramentText;

    @FXML
    void add_intencja(ActionEvent event) {
        SingleIntencja userData = new SingleIntencja();
        userData.getDataFromUser();
        if(!userData.isDataProvided) return;
        String query="insert into intencje_mszy(intencja, id_zamawiajacego, id_mszy) values ('"+userData.getIntencja()+"', "+(userData.getId_zamawiajacage()>0 ? userData.getId_zamawiajacage() : "null")+", "+SakramentView.currentSakrament.getId()+")";
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWindow(e);
        }
    }

    @FXML
    void delete_intencja(ActionEvent event) {
        String query = "delete from intencje_mszy where id_intencji="+table.getSelectionModel().getSelectedItem().getId_intencji();
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWindow(e);
        }
    }

    @FXML
    void return_to_sakrament(ActionEvent event) {
        try {
            replaceSceneContent("FXML/sakramentData.fxml");
        }catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void show_intencja(ActionEvent event) {
        SingleIntencja userData = new SingleIntencja(table.getSelectionModel().getSelectedItem());
        userData.getDataFromUser();
        if(!userData.isDataProvided) return;
        String query = "update intencje_mszy set (intencja, id_zamawiajacego)=('"+userData.getIntencja()+"', "+userData.getId_zamawiajacage()+") where id_intencji="+userData.getId_intencji();
        try {
            Statement st = connection.createStatement();
            st.executeUpdate(query);
            updateTable();
        } catch (Exception e) {
            e.printStackTrace();
            showErrorWindow(e);
        }
    }

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
    ObservableList<IntencjaEntity> intencje = FXCollections.observableArrayList();
    void updateTable() {
        try {
            intencje.setAll(IntencjaEntity.getAllFromSakrament(SakramentView.currentSakrament.getId()));
            table.refresh();
        }catch (Exception e) {
            e.printStackTrace();
            showErrorWindow(e);
        }
    }
    @FXML
    void initialize(){
        sakramentText.setText("Intencje sakramentu: "+SakramentView.currentSakrament.getSakrament()+" "+SakramentView.currentSakrament.getData().toString());
        intencjaColumn.setCellValueFactory(new PropertyValueFactory<>("intencja"));
        zamawiajacyColumn.setCellValueFactory(new PropertyValueFactory<>("imie_zamawiajacego"));
        table.setItems(intencje);
        updateTable();
    }
}

