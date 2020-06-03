package eParafia.Controller;

import javafx.beans.property.SimpleStringProperty;

public class AdresRow {

    SimpleStringProperty id_adresu;
    SimpleStringProperty miasto;
    SimpleStringProperty ulica;
    SimpleStringProperty nr_domu;

    public AdresRow(String id_adresu, String miasto, String ulica, String nr_domu){
        this.id_adresu=new SimpleStringProperty(id_adresu);
        this.miasto=new SimpleStringProperty(miasto);
        this.ulica=new SimpleStringProperty(ulica);
        this.nr_domu=new SimpleStringProperty(nr_domu);
    }

    public String getId_adresu() {
        return id_adresu.get();
    }

    public SimpleStringProperty id_adresuProperty() {
        return id_adresu;
    }

    public void setId_adresu(String id_adresu) {
        this.id_adresu.set(id_adresu);
    }

    public String getMiasto() {
        return miasto.get();
    }

    public SimpleStringProperty miastoProperty() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto.set(miasto);
    }

    public String getUlica() {
        return ulica.get();
    }

    public SimpleStringProperty ulicaProperty() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica.set(ulica);
    }

    public String getNr_domu() {
        return nr_domu.get();
    }

    public SimpleStringProperty nr_domuProperty() {
        return nr_domu;
    }

    public void setNr_domu(String nr_domu) {
        this.nr_domu.set(nr_domu);
    }
}
