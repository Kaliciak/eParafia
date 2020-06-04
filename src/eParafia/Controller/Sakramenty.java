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
    ObservableList<String> parafiaOptions = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> parafia;

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
                    "select id_sakramentu as \"id\", \"data\", sakrament, nazwa as \"parafia\" from sakramenty left outer join parafie on(id_parafii=parafia)";
            ResultSet rs=stmt.executeQuery(query);

            while (rs.next()){
                if(dateTo.getValue()!=null&&rs.getDate("data").toLocalDate().isAfter(dateTo.getValue())
                || dateSince.getValue()!=null&&rs.getDate("data").toLocalDate().isBefore(dateSince.getValue())
                || sakrType.getValue()!=null &&!sakrType.getValue().equals(rs.getString("sakrament"))
                || parafia.getValue()!=null && !parafia.getValue().equals(rs.getString("parafia")));
                else sakramentyRows.add(new SakramentyRow(
                        rs.getInt("id"),
                        rs.getDate("data"),
                        rs.getString("sakrament"),
                        rs.getString("parafia")
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
        parafiaColumn.setCellValueFactory(new PropertyValueFactory<>("parafia"));
        insertData();
        parafiaOptions.add(null);
        try {
            Statement stmt = connection.createStatement();
            String query=
                    "select nazwa from parafie";
            ResultSet rs=stmt.executeQuery(query);

            while (rs.next()){
                parafiaOptions.add(rs.getString("nazwa"));
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

