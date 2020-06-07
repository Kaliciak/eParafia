package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class PielgrzymkiRow {

    SimpleIntegerProperty id_wydarzenia;
    Date data_rozpoczecia;
    Date data_zakonczenia;
    SimpleStringProperty miejsce_startu;
    SimpleStringProperty cel;
    SimpleIntegerProperty koszt;

    public int getId_wydarzenia() {
        return id_wydarzenia.get();
    }

    public SimpleIntegerProperty id_wydarzeniaProperty() {
        return id_wydarzenia;
    }

    public void setId_wydarzenia(int id_wydarzenia) {
        this.id_wydarzenia.set(id_wydarzenia);
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

    public String getMiejsce_startu() {
        return miejsce_startu.get();
    }

    public SimpleStringProperty miejsce_startuProperty() {
        return miejsce_startu;
    }

    public void setMiejsce_startu(String miejsce_startu) {
        this.miejsce_startu.set(miejsce_startu);
    }

    public String getCel() {
        return cel.get();
    }

    public SimpleStringProperty celProperty() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel.set(cel);
    }

    public PielgrzymkiRow(Integer id_wydarzenia, Date data_rozpoczecia, Date data_zakonczenia, String miejsce_startu, String cel, Integer koszt) {
        this.id_wydarzenia = new SimpleIntegerProperty(id_wydarzenia);
        this.data_rozpoczecia = data_rozpoczecia;
        this.data_zakonczenia = data_zakonczenia;
        this.miejsce_startu = new SimpleStringProperty(miejsce_startu);
        this.cel = new SimpleStringProperty(cel);
        this.koszt =  new SimpleIntegerProperty(koszt);
    }

    public int getKoszt() {
        return koszt.get();
    }

    public SimpleIntegerProperty kosztProperty() {
        return koszt;
    }

    public void setKoszt(int koszt) {
        this.koszt.set(koszt);
    }
}
