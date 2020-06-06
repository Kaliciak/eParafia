package eParafia.Controller;

import eParafia.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChooseParafianie {

    static public ArrayList<FullParafianieEntity> all=new ArrayList<>();
    static public ArrayList<FullParafianieEntity> ret = new ArrayList<>();
    ObservableList<FullParafianieEntity> left= FXCollections.observableArrayList();
    ObservableList<FullParafianieEntity> right = FXCollections.observableArrayList();
    static public ArrayList<FullParafianieEntity> selectParafianie(String query) {
        try {
            ChooseParafianie.all=FullParafianieEntity.takeAllFromQuery(query);
            Parent r = FXMLLoader.load(Main.class.getResource("FXML/personFinder.fxml"));
            Scene scene = new Scene(r, 1000, 625);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
    @FXML
    private TableView<FullParafianieEntity> leftTable;

    @FXML
    private TableColumn<FullParafianieEntity, Integer> idLeftColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> leftNameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> leftSecondNameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> leftSurnameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, Date> leftBirthDateColumn;

    @FXML
    private TextField nameString;

    @FXML
    private TextField surnameString;

    @FXML
    private DatePicker dateTo;

    @FXML
    private DatePicker dateSince;
    @FXML
    private TableView<FullParafianieEntity> rightTable;
    @FXML
    private TableColumn<FullParafianieEntity, Integer> rightIdColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> rightNameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> rightSecondNameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> rightSurnameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, Date> rightBirthDateColumn;


    @FXML
    void mouse_left(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                if(leftTable.getSelectionModel().getSelectedItem()!=null) {
                    right.add(leftTable.getSelectionModel().getSelectedItem());
                    update(null);
                }
            }
        }
    }

    @FXML
    void mouse_right(MouseEvent event) {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                if(rightTable.getSelectionModel().getSelectedItem()!=null) {
                    right.remove(rightTable.getSelectionModel().getSelectedIndex());
                    update(null);
                }
            }
        }
    }
    @FXML
    void back(ActionEvent event) {
        ((Stage)leftTable.getScene().getWindow()).close();
    }

    @FXML
    void commit(ActionEvent event) {
        ret=new ArrayList<>(right);
        ((Stage)leftTable.getScene().getWindow()).close();
    }

    @FXML
    void move_left(ActionEvent event) {
        right.clear();
        update(null);
    }

    @FXML
    void move_right(ActionEvent event) {
        right.addAll(left);
        update(null);
    }

    @FXML
    void reset(ActionEvent event) {
        nameString.setText("");
        surnameString.setText("");
        dateTo.setValue(null);
        dateSince.setValue(null);
        update(null);
    }

    @FXML
    void update(ActionEvent event) {
        left.clear();
        left.addAll(all.stream().filter(pr ->
                !right.stream().anyMatch(pr2-> pr2.getId_osoby()==pr.getId_osoby())
                &&pr.getImie().contains(nameString.getText())
                &&pr.getNazwisko().contains(surnameString.getText())
                &&(dateTo.getValue()==null || !dateTo.getValue().isBefore(pr.getData_narodzin().toLocalDate()))
                &&(dateSince.getValue()==null || !dateSince.getValue().isAfter(pr.getData_narodzin().toLocalDate()))
                ).collect(Collectors.toList()));
    }
    @FXML
    void initialize(){
        idLeftColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
        leftNameColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        leftSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugie_imie"));
        leftSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        leftBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("data_narodzin"));
        rightIdColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
        rightNameColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        rightSecondNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugie_imie"));
        rightSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        rightBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("data_narodzin"));
        ret=new ArrayList<>();
        left.addAll(all);
        leftTable.setItems(left);
        rightTable.setItems(right);
        update(null);
    }
}

