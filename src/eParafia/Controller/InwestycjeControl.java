package eParafia.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import static eParafia.Controller.Dane.*;

public class InwestycjeControl {

    public static class InwestycjeEntity {
        public static ArrayList<InwestycjeEntity> getInwestycje() {
            ArrayList<InwestycjeEntity> result = new ArrayList<>();
            try {
                ResultSet rs = connection.createStatement().executeQuery(
                  "select i.*, parafie.nazwa as \"nazwa_parafii\" from inwestycje i left outer join parafie using(id_parafii)"
                );
                while(rs.next()) {
                    result.add(new InwestycjeEntity(
                            rs.getInt("id_inwestycji"),
                            rs.getString("nazwa"),
                            rs.getDate("data_rozpoczecia"),
                            rs.getDate("data_zakonczenia"),
                            rs.getInt("id_parafii"),
                            rs.getString("nazwa_parafii"),
                            rs.getString("kwota")
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
            return result;
        }

        int id_inwestycji;
        String nazwa;
        Date data_rozpoczecia;
        Date data_zakonczenia;
        int id_parafii;
        String nazwa_parafii;
        String kwota;

        public InwestycjeEntity(int id_inwestycji, String nazwa, Date data_rozpoczecia, Date data_zakonczenia, int id_parafii, String nazwa_parafii, String kwota) {
            this.id_inwestycji = id_inwestycji;
            this.nazwa = nazwa;
            this.data_rozpoczecia = data_rozpoczecia;
            this.data_zakonczenia = data_zakonczenia;
            this.id_parafii = id_parafii;
            this.nazwa_parafii = nazwa_parafii;
            this.kwota = kwota;
        }
        public InwestycjeEntity() {
            this(0,"",new Date(System.currentTimeMillis()),null,0,"","1000.00");
        }
        public int getId_inwestycji() {
            return id_inwestycji;
        }

        public void setId_inwestycji(int id_inwestycji) {
            this.id_inwestycji = id_inwestycji;
        }

        public String getNazwa() {
            return nazwa;
        }

        public void setNazwa(String nazwa) {
            this.nazwa = nazwa;
        }

        public Date getData_rozpoczecia() {
            return data_rozpoczecia;
        }

        public void setData_rozpoczecia(Date data_rozpoczecia) {
            this.data_rozpoczecia = data_rozpoczecia;
        }

        public Date getData_zakonczenia() {
            return data_zakonczenia;
        }

        public void setData_zakonczenia(Date data_zakonczenia) {
            this.data_zakonczenia = data_zakonczenia;
        }

        public int getId_parafii() {
            return id_parafii;
        }

        public void setId_parafii(int id_parafii) {
            this.id_parafii = id_parafii;
        }

        public String getNazwa_parafii() {
            return nazwa_parafii;
        }

        public void setNazwa_parafii(String nazwa_parafii) {
            this.nazwa_parafii = nazwa_parafii;
        }

        public String getKwota() {
            return kwota;
        }

        public void setKwota(String kwota) {
            this.kwota = kwota;
        }
    }

    boolean editMode=false;
    ObservableList<InwestycjeEntity> inwestycje = FXCollections.observableArrayList();
    ObservableList<Sakramenty.ShortParafia> parafie = FXCollections.observableArrayList();
    InwestycjeEntity currentInwestycja;
    @FXML
    void addedit(ActionEvent event) {
        if("".equals(nameText.getText()) || nameText.getText()==null) {
            showErrorWindow("podaj nazwę");
            return;
        }
        if(dateFrom.getValue()==null) {
            showErrorWindow("wybierz datę rozpoczecia");
            return;
        }
        if(chooseParafie.getValue()==null) {
            showErrorWindow("wybierz parafię");
            return;
        }
        if(grText.getText().length()>2) grText.setText(grText.getText().substring(0,2));
        if(!editMode) {
            try {
                connection.createStatement().executeUpdate(
                    "insert into inwestycje(nazwa, data_rozpoczecia, data_zakonczenia, id_parafii, kwota) values ('"
                        +nameText.getText()+"', '"
                        + Timestamp.valueOf(dateFrom.getValue().atStartOfDay())+ "', "
                        + (
                                dateTo.getValue()!=null
                                        ? "'"+Timestamp.valueOf(dateTo.getValue().atStartOfDay())+"'"
                                        : "null"
                        ) +", "
                        +chooseParafie.getValue().getId()+", "
                        +("".equals(zlText.getText()) ?  "0" : zlText.getText())+"." + DatkiControl.padLeftZeros(grText.getText(),2)
                        +")"
                );
                clear(null);
                insertData();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        }
        else {
            try {
                connection.createStatement().executeUpdate(
                        "update inwestycje set (nazwa, data_rozpoczecia, data_zakonczenia, id_parafii, kwota) = ('"
                                +nameText.getText()+"', '"
                                + Timestamp.valueOf(dateFrom.getValue().atStartOfDay())+ "', "
                                + (
                                dateTo.getValue()!=null
                                        ? "'"+Timestamp.valueOf(dateTo.getValue().atStartOfDay())+"'"
                                        : "null"
                        ) +", "
                                +chooseParafie.getValue().getId()+", "
                                +("".equals(zlText.getText()) ? "0" : zlText.getText())+"." + DatkiControl.padLeftZeros(grText.getText(),2)
                                +") where id_inwestycji="+currentInwestycja.getId_inwestycji()
                );
                clear(null);
                insertData();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        }
    }

    @FXML
    void clear(ActionEvent event) {
        currentInwestycja=new InwestycjeEntity();
        update();
        editMode=false;
    }

    @FXML
    void delete(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem()!=null) {
            try {
                connection.createStatement().executeUpdate(
                        "delete from inwestycje where id_inwestycji="+table.getSelectionModel().getSelectedItem().getId_inwestycji()
                );
                if(table.getSelectionModel().getSelectedItem().getId_inwestycji()==currentInwestycja.getId_inwestycji()) clear(null);
                insertData();
            }catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        }
    }

    @FXML
    void edit(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem()!=null) {
            currentInwestycja=table.getSelectionModel().getSelectedItem();
            update();
            editMode=true;
        }
    }
    void update() {
        idText.setText(currentInwestycja.getId_inwestycji()==0 ? "" : Integer.toString(currentInwestycja.getId_inwestycji()));
        nameText.setText(currentInwestycja.getNazwa());
        dateFrom.setValue(currentInwestycja.getData_rozpoczecia().toLocalDate());
        if(currentInwestycja.getData_zakonczenia()!=null) dateTo.setValue(currentInwestycja.getData_zakonczenia().toLocalDate());
        else dateTo.setValue(null);
        if(currentInwestycja.getId_parafii()!=0) chooseParafie.setValue(new Sakramenty.ShortParafia(currentInwestycja.getId_parafii(), currentInwestycja.getNazwa_parafii()));
        else if(parafie.size()>0) chooseParafie.setValue(parafie.get(0));
        else chooseParafie.setValue(null);
        zlText.setText(currentInwestycja.getKwota().split("\\.")[0]);
        grText.setText(currentInwestycja.getKwota().split("\\.")[1]);
    }
    void insertData() {
        inwestycje.setAll(InwestycjeEntity.getInwestycje());
    }
    @FXML
    void initialize(){
        zlText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    zlText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        grText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    grText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        currentInwestycja=new InwestycjeEntity();
        parafie.setAll(Sakramenty.ShortParafia.getParafie());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_inwestycji"));
        nazwaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        dataRozpoczeciaColumn.setCellValueFactory(new PropertyValueFactory<>("data_rozpoczecia"));
        dataZakonczeniaColumn.setCellValueFactory(new PropertyValueFactory<>("data_zakonczenia"));
        parafiaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa_parafii"));
        kwotaColumn.setCellValueFactory(new PropertyValueFactory<>("kwota"));
        table.setItems(inwestycje);
        insertData();
        update();
        chooseParafie.setItems(parafie);
        if(parafie.size()>0) chooseParafie.setValue(parafie.get(0));
    }
    //------------------------------------------------------------------------
    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TableView<InwestycjeEntity> table;

    @FXML
    private TableColumn<InwestycjeEntity, Integer> idColumn;

    @FXML
    private TableColumn<InwestycjeEntity, String> nazwaColumn;

    @FXML
    private TableColumn<InwestycjeEntity, Date> dataRozpoczeciaColumn;

    @FXML
    private TableColumn<InwestycjeEntity, Date> dataZakonczeniaColumn;

    @FXML
    private TableColumn<InwestycjeEntity, String> parafiaColumn;

    @FXML
    private TableColumn<InwestycjeEntity, String> kwotaColumn;

    @FXML
    private Text idText;

    @FXML
    private TextField nameText;

    @FXML
    private TextField zlText;

    @FXML
    private TextField grText;

    @FXML
    private ComboBox<Sakramenty.ShortParafia> chooseParafie;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

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
}
