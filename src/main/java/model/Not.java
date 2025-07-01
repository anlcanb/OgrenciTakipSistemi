package model;

public class Not {
    private int id;
    private int ogrenciId;
    private int dersId;
    private double not;

    public Not() {}

    public Not(int id, int ogrenciId, int dersId, double not) {
        this.id = id;
        this.ogrenciId = ogrenciId;
        this.dersId = dersId;
        this.not = not;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOgrenciId() {
        return ogrenciId;
    }

    public void setOgrenciId(int ogrenciId) {
        this.ogrenciId = ogrenciId;
    }

    public int getDersId() {
        return dersId;
    }

    public void setDersId(int dersId) {
        this.dersId = dersId;
    }

    public double getNot() {
        return not;
    }

    public void setNot(double not) {
        this.not = not;
    }
}
