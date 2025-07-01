package model;

public class Ders {
    private int id;
    private String ad;
    private int ogretmen_id;


    public Ders() {}


    public Ders(int id, String ad, int  ogretmen_id) {
        this.id = id;
        this.ad = ad;
        this.ogretmen_id = ogretmen_id;
    }

    public  int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getAd() {return ad;}
    public void setAd(String ad) {this.ad = ad;}

    public int getOgretmenid() {return ogretmen_id;}
    public void setOgretmenid() {this.ogretmen_id = ogretmen_id;}
}
