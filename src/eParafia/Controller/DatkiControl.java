package eParafia.Controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import static eParafia.Controller.Dane.*;

public class DatkiControl {

    static public class DatekEntity {

        static ArrayList<DatekEntity> getDatki() {
            ArrayList<DatekEntity> result = new ArrayList<>();
            try{
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("select datki.*,  nazwa , imie||' '||nazwisko as \"nazwa_darczyncy\" from datki left outer join parafie using(id_parafii) left outer join parafianie on id_osoby=darczynca;");
                while(rs.next())
                result.add(new DatekEntity(
                        rs.getInt("id_datku"),
                        rs.getDate("data"),
                        rs.getInt("id_parafii"),
                        rs.getString("nazwa"),
                        rs.getString("kwota"),
                        rs.getInt("darczynca"),
                        rs.getString("nazwa_darczyncy")
                ));
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
            return result;
        }

        int id_datku;
        Date data;
        int id_parafii;
        String nazwa_parafii;
        String kwota;
        int id_darczyncy;
        String nazwa_darczyncy;

        public DatekEntity(int id_datku, Date data, int id_parafii, String nazwa_parafii, String kwota, int id_darczyncy, String nazwa_darczyncy) {
            this.id_datku = id_datku;
            this.data = data;
            this.id_parafii = id_parafii;
            this.nazwa_parafii = nazwa_parafii;
            this.kwota = kwota;
            this.id_darczyncy = id_darczyncy;
            this.nazwa_darczyncy = nazwa_darczyncy;
        }
        public DatekEntity() {
            this(0,new Date(System.currentTimeMillis()),0,"","10.00", 0, "");
        }
        public int getId_datku() {
            return id_datku;
        }

        public void setId_datku(int id_datku) {
            this.id_datku = id_datku;
        }

        public Date getData() {
            return data;
        }

        public void setData(Date data) {
            this.data = data;
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

        public int getId_darczyncy() {
            return id_darczyncy;
        }

        public void setId_darczyncy(int id_darczyncy) {
            this.id_darczyncy = id_darczyncy;
        }

        public String getNazwa_darczyncy() {
            return nazwa_darczyncy;
        }

        public void setNazwa_darczyncy(String nazwa_darczyncy) {
            this.nazwa_darczyncy = nazwa_darczyncy;
        }
    }
    boolean editMode=false;
    DatekEntity currentDatek;
    ObservableList<DatekEntity> datki = FXCollections.observableArrayList();
    ObservableList<Sakramenty.ShortParafia> parafie = FXCollections.observableArrayList();
    @FXML
    void addEdit(ActionEvent event) {
        if(!editMode) {
            if(date.getValue()==null) {
                showErrorWindow("podaj datę");
                return;
            }
            if(parafiaChoose.getSelectionModel().getSelectedItem()==null) {
                showErrorWindow("wybierz parafię");
                return;
            }
            if(grText.getText().length()>2) grText.setText(grText.getText().substring(0,2));
            String query = "insert into datki(data,id_parafii,kwota, darczynca) values ('"+Timestamp.valueOf(date.getValue().atStartOfDay()).toString()+"', "+parafiaChoose.getValue().getId()+", "+("".equals(zlText.getText())? "0" : zlText.getText())+"."+padLeftZeros(grText.getText(),2)+", "+(currentDatek.getId_darczyncy()!=0 ? currentDatek.getId_darczyncy() : "null")+")";
            try {
                Statement st = connection.createStatement();
                st.executeUpdate(query);
                clearData(null);
                insertData();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        } else {
            if(date.getValue()==null) {
                showErrorWindow("podaj datę");
                return;
            }
            if(parafiaChoose.getSelectionModel().getSelectedItem()==null) {
                showErrorWindow("wybierz parafię");
                return;
            }
            if(grText.getText().length()>2) grText.setText(grText.getText().substring(0,2));
            try{
                connection.createStatement().executeUpdate(
                        "update datki set (data,id_parafii,kwota,darczynca)=('"+Timestamp.valueOf(date.getValue().atStartOfDay()).toString()+"', "+parafiaChoose.getValue().getId()+", "+("".equals(zlText.getText())? "0" : zlText.getText())+"."+padLeftZeros(grText.getText(),2)+", "+(currentDatek.getId_darczyncy()!=0 ? currentDatek.getId_darczyncy() : "null")+") where id_datku="+currentDatek.getId_datku()
                );
                clearData(null);
                insertData();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        }
    }
    static public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
    @FXML
    void chooseDarczynca(ActionEvent event) {
        FullParafianieEntity chosen = ChooseParafianin.selectParafianin("select * from parafianie");
        if(chosen!=null) {
            currentDatek.setId_darczyncy(chosen.getId_osoby());
            currentDatek.setNazwa_darczyncy(chosen.getImie()+" "+chosen.getNazwisko());
            darczyncaText.setText(currentDatek.getNazwa_darczyncy());
        }
    }

    @FXML
    void clearData(ActionEvent event) {
        currentDatek=new DatekEntity();
        update();
        editMode=false;
    }

    void update() {
        idText.setText(currentDatek.getId_datku()!=0 ? Integer.toString(currentDatek.getId_datku()) : "");
        zlText.setText(currentDatek.getKwota().split("\\.")[0]);
        grText.setText(currentDatek.getKwota().split("\\.")[1]);
        darczyncaText.setText(currentDatek.getNazwa_darczyncy());
        date.setValue(currentDatek.getData().toLocalDate());
        parafiaChoose.setValue(new Sakramenty.ShortParafia(currentDatek.getId_parafii(),currentDatek.getNazwa_parafii()));
    }

    void insertData() {
        datki.setAll(DatekEntity.getDatki());
    }

    @FXML
    void delete(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem()!=null) {
            try {
                connection.createStatement().executeUpdate("delete from datki where id_datku="+table.getSelectionModel().getSelectedItem().getId_datku());
                insertData();
            } catch (Exception e) {
                e.printStackTrace();
                showErrorWindow(e);
            }
        }
    }

    @FXML
    void edit(ActionEvent event) {
        if(table.getSelectionModel().getSelectedItem()!=null) {
            currentDatek=table.getSelectionModel().getSelectedItem();
            editMode=true;
            System.out.println(currentDatek.getId_datku()+" "+currentDatek.getNazwa_parafii());
            update();
        }
    }
    //------------------------------------------------------------------------------------------

    @FXML
    void initialize() {
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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_datku"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        parafiaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa_parafii"));
        kwotaColumn.setCellValueFactory(new PropertyValueFactory<>("kwota"));
        darczyncaColumn.setCellValueFactory(new PropertyValueFactory<>("nazwa_darczyncy"));
        table.setItems(datki);
        insertData();
        currentDatek=new DatekEntity();
        update();
        parafiaChoose.setItems(parafie);
        parafie.addAll(Sakramenty.ShortParafia.getParafie());
        if(parafie.size()>0) parafiaChoose.setValue(parafie.get(0));
    }

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TableView<DatekEntity> table;

    @FXML
    private TableColumn<DatekEntity, Integer> idColumn;

    @FXML
    private TableColumn<DatekEntity, Date> dateColumn;

    @FXML
    private TableColumn<DatekEntity, String> parafiaColumn;

    @FXML
    private TableColumn<DatekEntity, String> kwotaColumn;

    @FXML
    private TableColumn<DatekEntity, String> darczyncaColumn;

    @FXML
    private Text idText;

    @FXML
    private TextField zlText;

    @FXML
    private TextField grText;

    @FXML
    private Text darczyncaText;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<Sakramenty.ShortParafia> parafiaChoose;
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

}
