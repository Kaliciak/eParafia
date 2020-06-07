package eParafia.Controller;

import eParafia.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static eParafia.Controller.Dane.showErrorWindow;

public class SingleIntencja extends intencjeData.IntencjaEntity {
    public boolean isDataProvided=false;
    SingleIntencja(intencjeData.IntencjaEntity a) {
        super(a.getId_intencji(),a.getIntencja(), a.getId_zamawiajacage(), a.getImie_zamawiajacego()==null ? "" : a.getImie_zamawiajacego());
    }
    SingleIntencja(){
        super(0,"", 0, "");
    }
    @FXML
    private TextArea intencjaText;

    @FXML
    private Text zamawiajacyText;

    @FXML
    void choose_zamawiajacy(ActionEvent event) {
        FullParafianieEntity chosen = ChooseParafianin.selectParafianin("select * from parafianie");
        if(chosen!= null) {
            setId_zamawiajacage(chosen.getId_osoby());
            setImie_zamawiajacego(chosen.getImie()+" "+chosen.getNazwisko());
            zamawiajacyText.setText("Zamawiający: "+getImie_zamawiajacego());
        }
    }
    @FXML
    void confirm(ActionEvent event) {
        if(intencjaText.getText()==null || "".equals(intencjaText.getText())) {
            showErrorWindow("Wprowadź tekst");
        }
        else {
            setIntencja(intencjaText.getText());
            isDataProvided=true;
            ((Stage)intencjaText.getScene().getWindow()).close();
        }
    }
    @FXML
    void initialize() {
        intencjaText.setText(getIntencja());
        zamawiajacyText.setText("Zamawiający: "+getImie_zamawiajacego());
    }

    void getDataFromUser() {
        try{
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("FXML/singeIntencja.fxml"));
            loader.setController(this);
            AnchorPane anchor = loader.load();
            Scene scene = new Scene(anchor, 400, 416);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }catch (Exception e) {
            showErrorWindow(e);
            e.printStackTrace();
        }
    }
}
