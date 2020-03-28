import java.util.ArrayList;

public class Saturn extends Planet {
    int[] colors = new int[]{100, 50, 0, 0, 100, 50};
    Satellite titan;
    Satellite iapetus;
    Satellite tethys;
    Satellite dione;
    Satellite rhea;

    public Saturn() {
        super("Saturn", 568.5, 60270, 1429.4,
                0.054,
                2.48,
                29.4,
                113,
                93);
        titan = new Satellite("Titan", 1.3, 2575, 1.221865, 0.029, 0.306, 0.043, new int[]{150, 0, 150, 0, 0, 0});
        iapetus = new Satellite("Iapetus", 0.002, 718, 3.560854, 0.029, 8.298, 0.216, new int[]{150, 0, 150 ,0, 150, 0});
        tethys = new Satellite("Tethys", 0.0062, 530, 0.294672, 0, 1.091, 0.005, new int[]{150, 0, 150, 0, 150, 0});
        dione = new Satellite("Dione", 0.011, 559, 0.377415, 0.002, 0.028, 0.0075, new int[]{150, 0, 150, 0, 150, 0});
        rhea = new Satellite("Rhea", 0.023, 764, 0.527068, 0.0013, 0.333, 0.012, new int[]{150, 0, 150, 0, 150, 0});
    }

    public int[] getColors(){return colors;}

    public ArrayList<String > getAllInfo(double r, double speed){
        ArrayList<String> s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 82");
        s.add("Название наиболее известных спутников: Титан, Япет, Тефия, Диона, Рея");
        return s;
    }
}

