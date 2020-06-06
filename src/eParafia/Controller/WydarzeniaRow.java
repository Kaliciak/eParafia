package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class WydarzeniaRow {

    SimpleIntegerProperty id_wydarzenia;
    SimpleStringProperty wydarzenie;
    Date data_rozpoczecia;
    Date data_zakonczenia;

    public WydarzeniaRow(Integer id_wydarzenia, String wydarzenie, Date data_rozpoczecia, Date data_zakonczenia) {
        this.id_wydarzenia = new SimpleIntegerProperty(id_wydarzenia);
        this.wydarzenie = new SimpleStringProperty(wydarzenie);
        this.data_rozpoczecia = data_rozpoczecia;
        this.data_zakonczenia = data_zakonczenia;
    }

    public int getId_wydarzenia() {
        return id_wydarzenia.get();
    }

    public SimpleIntegerProperty id_wydarzeniaProperty() {
        return id_wydarzenia;
    }

    public void setId_wydarzenia(int id_wydarzenia) {
        this.id_wydarzenia.set(id_wydarzenia);
    }

    public String getWydarzenie() {
        return wydarzenie.get();
    }

    public SimpleStringProperty wydarzenieProperty() {
        return wydarzenie;
    }

    public void setWydarzenie(String wydarzenie) {
        this.wydarzenie.set(wydarzenie);
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
}
