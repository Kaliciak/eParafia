package eParafia.Controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


import static eParafia.Controller.Dane.*;

public class WyszukajParafie {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem mainMenu;

    @FXML
    private MenuItem logout;

    @FXML
    private TextField nazwa;

    @FXML
    private ComboBox<String> zakon;

    @FXML
    private TextField id_parafii;

    @FXML
    private TextField miasto;

    @FXML
    private TextField ulica;

    @FXML
    private TextField nr_domu;

    boolean isWhere;
    String query;

    public void putWhere(){
        if(!isWhere){
            isWhere=true;
            query+=" WHERE ";
        }
        else {
            query+=" AND ";
        }
    }

    @FXML
    void wyszukajParafie(ActionEvent event) {
        String idPar=id_parafii.getText();
        String naz=nazwa.getText();
        String zak=zakon.getSelectionModel().getSelectedItem();
        String miast=miasto.getText();
        String ul=ulica.getText();
        String nrDom=nr_domu.getText();

        isWhere=false;

        try {
            Statement stmt = connection.createStatement();
            query=
                    "SELECT \n" +
                            "p.id_parafii AS \"id_parafii\",\n" +
                            "p.nazwa AS \"nazwa\",\n" +
                            "z.nazwa AS \"zakon\",\n" +
                            "a.miasto AS \"miasto\",\n" +
                            "a.ulica AS \"ulica\",\n" +
                            "a.nr_domu AS \"nr_domu\"\n" +
                            "\tFROM parafie p LEFT JOIN adresy a ON p.id_adresu=a.id_adresu\n" +
                            "\tLEFT JOIN zakony z ON p.zakon=z.id_zakonu";

            if(idPar!=null && !idPar.isEmpty()){
                putWhere();
                query+="id_parafii='"+idPar+"'";
            }
            if(naz!=null && !naz.isEmpty()){
                putWhere();
                query+="p.nazwa LIKE '%"+naz+"%'";
            }
            if(zak!=null && !zak.isEmpty()){
                if(zak.equals("Każdy")){}
                else if(zak.equals("Bez zakonu")){
                    putWhere();
                    query+="zakon IS NULL";
                }
                else{
                    putWhere();
                    query+="z.nazwa LIKE '%"+zak+"%'";
                }
            }
            if(miast!=null && !miast.isEmpty()){
                putWhere();
                query+="miasto LIKE '%"+miast+"%'";
            }
            if(ul!=null && !ul.isEmpty()){
                putWhere();
                query+="ulica LIKE '%"+ul+"%'";
            }
            if(nrDom!=null && !nrDom.isEmpty()){
                putWhere();
                query+="nr_domu='"+nrDom+"'";
            }

            BasicParafie.wyszukaneParafie=stmt.executeQuery(query);
            replaceSceneContent("FXML/basicParafie.fxml");
        }
        catch (Exception e){
            showErrorWindow(e);
        }

    }

    public void wypelnijCombo(){
        try {
            Statement stmt = connection.createStatement();
            String query= "SELECT * FROM zakony";
            ResultSet rs=stmt.executeQuery(query);

            zakon.getItems().add("Każdy");
            zakon.getItems().add("Bez zakonu");

            while (rs.next()){
                zakon.getItems().add(rs.getString("nazwa"));
            }

            zakon.getSelectionModel().selectFirst();
        }
        catch (Exception e){
            showErrorWindow(e);
        }
    }

    @FXML
    void initialize() {
        assert mainMenu != null : "fx:id=\"mainMenu\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert logout != null : "fx:id=\"logout\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert nazwa != null : "fx:id=\"nazwa\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert zakon != null : "fx:id=\"zakon\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert id_parafii != null : "fx:id=\"id_parafii\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert miasto != null : "fx:id=\"miasto\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert ulica != null : "fx:id=\"ulica\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";
        assert nr_domu != null : "fx:id=\"nr_domu\" was not injected: check your FXML file 'wyszukajParafie.fxml'.";

        wypelnijCombo();
    }

    //MENU

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
    ////
}
