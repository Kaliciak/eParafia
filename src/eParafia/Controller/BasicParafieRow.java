package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BasicParafieRow {

    SimpleIntegerProperty id_parafii;
    SimpleStringProperty nazwa;
    SimpleStringProperty zakon;
    SimpleStringProperty miasto;
    SimpleStringProperty ulica;
    SimpleStringProperty nr_domu;

    public BasicParafieRow(Integer id_parafii, String nazwa, String zakon, String miasto, String ulica, String nr_domu) {
        this.id_parafii = new SimpleIntegerProperty(id_parafii);
        this.nazwa = new SimpleStringProperty(nazwa);
        this.zakon = new SimpleStringProperty(zakon);
        this.miasto = new SimpleStringProperty(miasto);
        this.ulica = new SimpleStringProperty(ulica);
        this.nr_domu = new SimpleStringProperty(nr_domu);
    }

    public int getId_parafii() {
        return id_parafii.get();
    }

    public SimpleIntegerProperty id_parafiiProperty() {
        return id_parafii;
    }

    public void setId_parafii(int id_parafii) {
        this.id_parafii.set(id_parafii);
    }

    public String getNazwa() {
        return nazwa.get();
    }

    public SimpleStringProperty nazwaProperty() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa.set(nazwa);
    }

    public String getZakon() {
        return zakon.get();
    }

    public SimpleStringProperty zakonProperty() {
        return zakon;
    }

    public void setZakon(String zakon) {
        this.zakon.set(zakon);
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
