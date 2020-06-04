package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class SakramentyRow{
    SimpleIntegerProperty id;
    Date data;
    SimpleStringProperty sakrament;
    SimpleStringProperty parafia;

    public SakramentyRow(int id, Date data, String sakrament, String parafia) {
        this.id = new SimpleIntegerProperty(id);
        this.data = data;
        this.sakrament = new SimpleStringProperty(sakrament);
        this.parafia = new SimpleStringProperty(parafia);
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
        this.data=data;
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

    public String getParafia() {
        return parafia.get();
    }

    public SimpleStringProperty parafiaProperty() {
        return parafia;
    }

    public void setParafia(String parafia) {
        this.parafia.set(parafia);
    }
}
