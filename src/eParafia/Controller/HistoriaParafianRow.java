package eParafia.Controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class HistoriaParafianRow {

    SimpleIntegerProperty id_parafii;
    SimpleStringProperty nazwa;
    SimpleStringProperty miasto;
    SimpleStringProperty ulica;
    SimpleStringProperty nr_domu;
    Date data_przybycia;
    Date data_odejscia;
    SimpleStringProperty apostazja;

    public HistoriaParafianRow(Integer id_parafii, String nazwa, String miasto, String ulica, String nr_domu, Date data_przybycia, Date data_odejscia, String apostazja) {
        this.id_parafii = new SimpleIntegerProperty(id_parafii);
        this.nazwa = new SimpleStringProperty(nazwa);
        this.miasto = new SimpleStringProperty(miasto);
        this.ulica =new SimpleStringProperty(ulica);
        this.nr_domu = new SimpleStringProperty(nr_domu);
        this.data_przybycia = data_przybycia;
        this.data_odejscia = data_odejscia;
        this.apostazja = new SimpleStringProperty(apostazja);
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

    public Date getData_przybycia() {
        return data_przybycia;
    }

    public void setData_przybycia(Date data_przybycia) {
        this.data_przybycia = data_przybycia;
    }

    public Date getData_odejscia() {
        return data_odejscia;
    }

    public void setData_odejscia(Date data_odejscia) {
        this.data_odejscia = data_odejscia;
    }

    public String getApostazja() {
        return apostazja.get();
    }

    public SimpleStringProperty apostazjaProperty() {
        return apostazja;
    }

    public void setApostazja(String apostazja) {
        this.apostazja.set(apostazja);
    }
}
