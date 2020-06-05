package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class SakramentyRow{
    SimpleIntegerProperty id;
    Date data;
    SimpleStringProperty sakrament;
    SimpleIntegerProperty parafia;
    SimpleStringProperty parafiaName;
    SimpleIntegerProperty id_szafarza;

    public SakramentyRow(int id, Date data, String sakrament, Integer parafia, String parafiaName, int id_szafarza) {
        this.id = new SimpleIntegerProperty(id);
        this.data = data;
        this.sakrament = new SimpleStringProperty(sakrament);
        this.parafia = new SimpleIntegerProperty(parafia);
        this.parafiaName = new SimpleStringProperty(parafiaName);
        this.id_szafarza = new SimpleIntegerProperty(id_szafarza);
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getSakrament() {
        return sakrament.get();
    }

    public SimpleStringProperty sakramentProperty() {
        return sakrament;
    }

    public void setSakrament(String sakrament) {
        this.sakrament.set(sakrament);
    }

    public int getParafia() {
        return parafia.get();
    }

    public SimpleIntegerProperty parafiaProperty() {
        return parafia;
    }

    public void setParafia(int parafia) {
        this.parafia.set(parafia);
    }

    public String getParafiaName() {
        return parafiaName.get();
    }

    public SimpleStringProperty parafiaNameProperty() {
        return parafiaName;
    }

    public void setParafiaName(String parafiaName) {
        this.parafiaName.set(parafiaName);
    }

    public int getId_szafarza() {
        return id_szafarza.get();
    }

    public SimpleIntegerProperty id_szafarzaProperty() {
        return id_szafarza;
    }

    public void setId_szafarza(int id_szafarza) {
        this.id_szafarza.set(id_szafarza);
    }
}
