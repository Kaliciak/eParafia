package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class PracowincyRow {

    SimpleIntegerProperty id_osoby;
    SimpleStringProperty imie;
    SimpleStringProperty nazwisko;
    SimpleStringProperty rola;
    SimpleIntegerProperty id_parafii;
    SimpleStringProperty nazwa;
    Date data_rozpoczecia;
    Date data_zakonczenia;

    public PracowincyRow(Integer id_osoby, String imie, String nazwisko, String rola, Integer id_parafii, String nazwa, Date data_rozpoczecia, Date data_zakonczenia) {
        this.id_osoby = new SimpleIntegerProperty(id_osoby);
        this.imie = new SimpleStringProperty(imie);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.rola = new SimpleStringProperty(rola);
        this.id_parafii = new SimpleIntegerProperty(id_parafii);
        this.nazwa = new SimpleStringProperty(nazwa);
        this.data_rozpoczecia = data_rozpoczecia;
        this.data_zakonczenia = data_zakonczenia;
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

    public String getRola() {
        return rola.get();
    }

    public SimpleStringProperty rolaProperty() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola.set(rola);
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
