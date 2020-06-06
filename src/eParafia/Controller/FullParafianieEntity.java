package eParafia.Controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static eParafia.Controller.Dane.connection;

public class FullParafianieEntity {
    SimpleIntegerProperty id_osoby;
    SimpleStringProperty imie;
    SimpleStringProperty drugie_imie;
    SimpleStringProperty imie_z_bierzmowania;
    SimpleStringProperty nazwisko;
    SimpleStringProperty plec;
    SimpleIntegerProperty id_adresu;
    Date data_narodzin;
    Date data_zgonu;
    SimpleIntegerProperty id_ojca;
    SimpleIntegerProperty id_matki;
    SimpleIntegerProperty id_ojca_chrzestnego;
    SimpleIntegerProperty id_matki_chrzestnej;
    public FullParafianieEntity(int id_osoby, String imie, String drugie_imie, String imie_z_bierzmowania,String nazwisko, String plec, int id_adresu, Date data_narodzin, Date data_zgonu, int id_ojca, int id_matki, int id_ojca_chrzestnego, int id_matki_chrzestnej) {
        this.id_osoby=new SimpleIntegerProperty(id_osoby);
        this.imie = new SimpleStringProperty(imie);
        this.drugie_imie=new SimpleStringProperty(drugie_imie);
        this.imie_z_bierzmowania=new SimpleStringProperty(imie_z_bierzmowania);
        this.nazwisko = new SimpleStringProperty(nazwisko);
        this.plec=new SimpleStringProperty(plec);
        this.id_adresu=new SimpleIntegerProperty(id_adresu);
        this.data_narodzin=data_narodzin;
        this.data_zgonu=data_zgonu;
        this.id_ojca=new SimpleIntegerProperty(id_ojca);
        this.id_matki=new SimpleIntegerProperty(id_matki);
        this.id_ojca_chrzestnego = new SimpleIntegerProperty(id_ojca_chrzestnego);
        this.id_matki_chrzestnej =new SimpleIntegerProperty(id_matki_chrzestnej);
    }
    static public ArrayList<FullParafianieEntity> takeAllFromQuery(String query){
        ArrayList<FullParafianieEntity> out = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs=stmt.executeQuery(query);
            while(rs.next())
                out.add(new FullParafianieEntity(
                    rs.getInt("id_osoby"),
                    rs.getString("imie"),
                    rs.getString("drugie_imie"),
                    rs.getString( "imie_z_bierzmowania"),
                    rs.getString("nazwisko"),
                    rs.getString("plec"),
                    rs.getInt("id_adresu"),
                    rs.getDate("data_narodzin"),
                    rs.getDate("data_zgonu"),
                    rs.getInt("id_ojca"),
                    rs.getInt("id_matki"),
                    rs.getInt("id_ojca_chrzestnego"),
                    rs.getInt("id_matki_chrzestnej")
                ));
        } catch (Exception e){
            e.printStackTrace();
        }
        return out;
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

    public String getDrugie_imie() {
        return drugie_imie.get();
    }

    public SimpleStringProperty drugie_imieProperty() {
        return drugie_imie;
    }

    public void setDrugie_imie(String drugie_imie) {
        this.drugie_imie.set(drugie_imie);
    }

    public String getImie_z_bierzmowania() {
        return imie_z_bierzmowania.get();
    }

    public SimpleStringProperty imie_z_bierzmowaniaProperty() {
        return imie_z_bierzmowania;
    }

    public void setImie_z_bierzmowania(String imie_z_bierzmowania) {
        this.imie_z_bierzmowania.set(imie_z_bierzmowania);
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

    public String getPlec() {
        return plec.get();
    }

    public SimpleStringProperty plecProperty() {
        return plec;
    }

    public void setPlec(String plec) {
        this.plec.set(plec);
    }

    public int getId_adresu() {
        return id_adresu.get();
    }

    public SimpleIntegerProperty id_adresuProperty() {
        return id_adresu;
    }

    public void setId_adresu(int id_adresu) {
        this.id_adresu.set(id_adresu);
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

    public int getId_ojca() {
        return id_ojca.get();
    }

    public SimpleIntegerProperty id_ojcaProperty() {
        return id_ojca;
    }

    public void setId_ojca(int id_ojca) {
        this.id_ojca.set(id_ojca);
    }

    public int getId_matki() {
        return id_matki.get();
    }

    public SimpleIntegerProperty id_matkiProperty() {
        return id_matki;
    }

    public void setId_matki(int id_matki) {
        this.id_matki.set(id_matki);
    }

    public int getId_ojca_chrzestnego() {
        return id_ojca_chrzestnego.get();
    }

    public SimpleIntegerProperty id_ojca_chrzestnegoProperty() {
        return id_ojca_chrzestnego;
    }

    public void setId_ojca_chrzestnego(int id_ojca_chrzestnego) {
        this.id_ojca_chrzestnego.set(id_ojca_chrzestnego);
    }

    public int getId_matki_chrzestnej() {
        return id_matki_chrzestnej.get();
    }

    public SimpleIntegerProperty id_matki_chrzestnejProperty() {
        return id_matki_chrzestnej;
    }

    public void setId_matki_chrzestnej(int id_matki_chrzestnej) {
        this.id_matki_chrzestnej.set(id_matki_chrzestnej);
    }
}
