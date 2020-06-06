package eParafia.Controller;

import eParafia.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;

import static eParafia.Controller.Dane.connection;
import static eParafia.Controller.Dane.showErrorWindow;

public class SakramentAdd extends SakramentyRow{
    static public SakramentyRow getSakramentDataFromUser() {
        SakramentAdd control=new SakramentAdd();
        try{
            getData(control);
        } catch (Exception e){
            e.printStackTrace();
        }
        return control.success ? control : null;
    }
    static public SakramentyRow getSakramentDataFromUser(SakramentyRow init, String szafarz) {
        SakramentAdd control=new SakramentAdd(init, szafarz);
        try{
            getData(control);
        } catch (Exception e){            e.printStackTrace();
        }
        return control.success ? control : null;
    }
    static void getData(SakramentAdd control) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML/addupdateSakrament.fxml"));
        loader.setController(control);
        AnchorPane anchor = loader.load();
        Scene scene = new Scene(anchor, 311, 178);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
    boolean initiallySet;
    boolean success;
    String szafarz;
    static ObservableList<String> sakrTypes = FXCollections.observableArrayList(
            "EUCHARYSTIA",
            "CHRZEST",
            "PIERWSZA KOMUNIA",
            "BIERZMOWANIE",
            "MAŁŻEŃSTWO",
            "POKUTA",
            "NAMASZCZENIE CHORYCH",
            "KAPŁAŃSTWO");
    ObservableList<Sakramenty.ShortParafia> parafie = FXCollections.observableArrayList();

    SakramentAdd() {
        super();
        initiallySet=false;
        szafarz="";
        success=false;
    }
    SakramentAdd(SakramentyRow init, String szafarz) {
        super(init.getId(), init.getData(), init.getSakrament(), init.getParafia(), init.getParafiaName(), init.getId_szafarza());
        initiallySet=true;
        this.szafarz=szafarz;
        success=false;
    }
    @FXML
    private ComboBox<String> sakrType;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<Sakramenty.ShortParafia> parafia;

    @FXML
    private Text szafarzName;
    @FXML
    void choose_szafarz(ActionEvent event) {
        FullParafianieEntity x=ChooseParafianin.selectParafianin("select * from parafianie");
        if(x!=null) {
            setId_szafarza(x.getId_osoby());
            szafarz=x.getImie()+" "+x.getNazwisko();
            szafarzName.setText("Szafarz: "+szafarz);
        }
    }
    @FXML
    void initialize(){
        parafie.addAll(Sakramenty.ShortParafia.getParafie());
        parafia.setItems(parafie);
        if(initiallySet) parafia.setValue(new Sakramenty.ShortParafia(getParafia(),getParafiaName()));
        else if(parafie.size()>0) parafia.setValue(parafie.get(0));

        sakrType.setItems(sakrTypes);
        if(initiallySet) {
            sakrType.setValue(getSakrament());
            sakrType.setDisable(true);
        } else {
            sakrType.setValue("EUCHARYSTIA");
        }

        if(initiallySet) date.setValue(getData().toLocalDate());

        if(initiallySet) szafarzName.setText("Szafarz: "+szafarz);
    }
    @FXML
    void confirm(ActionEvent event) {
        if(date.getValue()==null) {
            showErrorWindow("Podaj datę");
        }
        else if (parafia.getValue()==null) {
            showErrorWindow("Podaj parafię");
        }
        else {
            setSakrament(sakrType.getValue());
            setParafia(parafia.getValue().getId());
            setParafiaName(parafia.getValue().getNazwa());
            setData(Date.valueOf(date.getValue()));
            success=true;
            ((Stage)parafia.getScene().getWindow()).close();
        }
    }

    @FXML
    void clear_szafarz(ActionEvent event) {
        setId_szafarza(-1);
        szafarz="";
        szafarzName.setText("Szafarz: ");
    }
    @FXML
    void back(ActionEvent event) {
        success=false;
        ((Stage)parafia.getScene().getWindow()).close();
    }
}
