package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.time.LocalDate;

public class BasicParafianieRow {

    SimpleIntegerProperty id_osoby;
    SimpleStringProperty imie;
    SimpleStringProperty nazwisko;
    Date data_narodzin;
    Date data_zgonu;

    public BasicParafianieRow(Integer id_osoby, String imie, String nazwisko, Date data_narodzin, Date data_zgonu) {
        this.id_osoby = new SimpleIntegerProperty(id_osoby);
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.data_narodzin = data_narodzin;
        this.data_zgonu = data_zgonu;
    }

    public int getId_osoby() {
        return id_osoby.get();
    }

    public SimpleIntegerProperty id_osobyProperty() {
        return id_osoby;
    }

    public void setId_osoby(int id_osoby) {
        this.id_osoby.set(id_osoby);
    }

    public String getImie() {
        return imie.get();
    }

    public SimpleStringProperty imieProperty() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public SimpleStringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public Date getData_narodzin() {
        return data_narodzin;
    }

    public void setData_narodzin(Date data_narodzin) {
        this.data_narodzin = data_narodzin;
    }

    public Date getData_zgonu() {
        return data_zgonu;
    }

    public void setData_zgonu(Date data_zgonu) {
        this.data_zgonu = data_zgonu;
    }
}
