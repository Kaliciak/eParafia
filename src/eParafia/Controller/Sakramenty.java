package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static eParafia.Controller.Dane.*;

public class Sakramenty {

    @FXML
    private TableView<SakramentyRow> table;

    @FXML
    private TableColumn<SakramentyRow, Integer> idColumn;

    @FXML
    private TableColumn<SakramentyRow, Date> dataColumn;

    @FXML
    private TableColumn<SakramentyRow, String> sakramentColumn;

    @FXML
    private TableColumn<SakramentyRow, String> parafiaColumn;
    ObservableList<String> sakrTypeOptions =
            FXCollections.observableArrayList(
                    null,
                    "EUCHARYSTIA",
                    "CHRZEST",
                    "PIERWSZA KOMUNIA",
                    "BIERZMOWANIE",
                    "MAŁŻEŃSTWO",
                    "POKUTA",
                    "NAMASZCZENIE CHORYCH",
                    "KAPŁAŃSTWO"
            );
    @FXML
    private ComboBox<String> sakrType;
    public class ShortParafia {
        SimpleIntegerProperty id;
        SimpleStringProperty nazwa;
        ShortParafia(int id, String nazwa) {
            this.id = new SimpleIntegerProperty(id);
            this.nazwa = new SimpleStringProperty(nazwa);
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

        public String getNazwa() {
            return nazwa.get();
        }

        public SimpleStringProperty nazwaProperty() {
            return nazwa;
        }

        public void setNazwa(String nazwa) {
            this.nazwa.set(nazwa);
        }
        public String toString() {
            return nazwa.getValue();
        }
    }
    ObservableList<ShortParafia> parafiaOptions = FXCollections.observableArrayList();
    @FXML
    private ComboBox<ShortParafia> parafia;

    @FXML
    private DatePicker dateSince;

    @FXML
    private DatePicker dateTo;

    @FXML
    void addSakr(javafx.event.ActionEvent event) {

    }

    @FXML
    void delete(javafx.event.ActionEvent event) {

    }

    @FXML
    void show(javafx.event.ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem()==null) return;
        SakramentView.currentSakrament=table.getSelectionModel().getSelectedItem();
        try {
            replaceSceneContent("FXML/sakramentData.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clear(ActionEvent event) {
        dateTo.setValue(null);
        dateSince.setValue(null);
        parafia.setValue(null);
        sakrType.setValue(null);
    }
    ObservableList<SakramentyRow> sakramentyRows= FXCollections.observableArrayList();
    void insertData() {
        sakramentyRows.clear();
        try {
            Statement stmt = connection.createStatement();
            String query=
                    "select s.*, nazwa|| ', ' || miasto || ', ' || ulica ||' '|| nr_domu as \"parafiaName\" from sakramenty s left outer join parafie left outer join adresy using(id_adresu) on(parafia=id_parafii);";
            ResultSet rs=stmt.executeQuery(query);

            while (rs.next()){
                if(dateTo.getValue()!=null&&rs.getDate("data").toLocalDate().isAfter(dateTo.getValue())
                || dateSince.getValue()!=null&&rs.getDate("data").toLocalDate().isBefore(dateSince.getValue())
                || sakrType.getValue()!=null &&!sakrType.getValue().equals(rs.getString("sakrament"))
                || parafia.getValue()!= null &&parafia.getValue().id.getValue()!=rs.getInt("parafia"));
                else sakramentyRows.add(new SakramentyRow(
                        rs.getInt("id_sakramentu"),
                        rs.getDate("data"),
                        rs.getString("sakrament"),
                        rs.getInt("parafia"),
                        rs.getString("parafiaName"),
                        rs.getInt("id_szafarza")
                ));
            }
            table.setItems(sakramentyRows);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Nie powiodło się");
        }
    }
    @FXML
    void update(ActionEvent event) {
        insertData();
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
    @FXML
    void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        sakramentColumn.setCellValueFactory(new PropertyValueFactory<>("sakrament"));
        parafiaColumn.setCellValueFactory(new PropertyValueFactory<>("parafiaName"));
        insertData();
        parafiaOptions.add(null);
        try {
            Statement stmt = connection.createStatement();
            String query=
                    "select id_parafii,  nazwa|| ', ' || miasto || ', ' || ulica ||' '|| nr_domu as \"nazwa\" from parafie  left outer join adresy using(id_adresu);";
            ResultSet rs=stmt.executeQuery(query);

            while (rs.next()){
                parafiaOptions.add(new ShortParafia(rs.getInt("id_parafii"), rs.getString("nazwa")));
            }
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Nie powiodło się");
        }
        sakrType.setItems(sakrTypeOptions);
        parafia.setItems(parafiaOptions);
    }
}

