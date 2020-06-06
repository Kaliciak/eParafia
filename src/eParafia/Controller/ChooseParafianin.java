package eParafia.Controller;

import eParafia.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static eParafia.Controller.Dane.showErrorWindow;

public class ChooseParafianin {

    public static FullParafianieEntity selectParafianin(String query){
        ArrayList<FullParafianieEntity> chosen = FullParafianieEntity.takeAllFromQuery(query);
        ChooseParafianin x=new ChooseParafianin(chosen);
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML/chooseParafianin.fxml"));
            loader.setController(x);
            AnchorPane anchor = loader.load();
            Scene scene = new Scene(anchor, 403, 346);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (Exception e) {
            showErrorWindow(e);
            e.printStackTrace();
        }
        return x.chosenOne;
    }

    ArrayList<FullParafianieEntity> chosen;
    ObservableList<FullParafianieEntity> parafianie = FXCollections.observableArrayList();
    FullParafianieEntity chosenOne;
    ChooseParafianin(ArrayList<FullParafianieEntity> chosen) {
        this.chosen=chosen;
        chosenOne=null;
    }
    @FXML
    private TableView<FullParafianieEntity> table;
    @FXML
    private TableColumn<FullParafianieEntity, Integer> idColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> nameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> secondNameColumn;

    @FXML
    private TableColumn<FullParafianieEntity, String> surnameColumn;

    @FXML
    private TextField nameText;

    @FXML
    private TextField surnameText;

    @FXML
    private DatePicker dateTo;

    @FXML
    private DatePicker dateSince;

    @FXML
    void onChoose(MouseEvent event) {
        if(table.getSelectionModel().getSelectedItem()!=null) {
            chosenOne=table.getSelectionModel().getSelectedItem();
            ((Stage)table.getScene().getWindow()).close();
        }
    }

    @FXML
    void update(ActionEvent event) {
        parafianie.clear();
        parafianie.addAll(chosen.stream().filter(pr ->
                        pr.getImie().contains(nameText.getText())
                        &&pr.getNazwisko().contains(surnameText.getText())
                        &&(dateTo.getValue()==null || !dateTo.getValue().isBefore(pr.getData_narodzin().toLocalDate()))
                        &&(dateSince.getValue()==null || !dateSince.getValue().isAfter(pr.getData_narodzin().toLocalDate()))
        ).collect(Collectors.toList()));
    }
    @FXML
    void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_osoby"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("imie"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<>("drugie_imie"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        table.setItems(parafianie);
        update(null);
    }
}
