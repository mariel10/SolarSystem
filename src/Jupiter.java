import java.util.ArrayList;

public class Jupiter extends Planet {
    int[] colors = new int[]{175, 80, 120, 40, 80, 70};
    Satellite io;
    Satellite europa;
    Satellite ganymede;
    Satellite callisto;

    public Jupiter() {
        super("Jupiter", 1899, 71490, 778.41,
                0.048,
                1.31,
                11.857,
                100,
                14);
        io = new Satellite("Io", 0.893, 1821, 0.4218, 0.0041, 2.21, 0.004, new int[]{150, 50, 0, 0, 0, 0});
        europa = new Satellite("Europa", 0.48, 1560, 0.6711, 0.0094, 1.79, 0.0095, new int[]{150, 0, 150, 0, 150, 0});
        ganymede = new Satellite("Ganymede", 1.4819, 2634, 1.0704, 0.0013, 0.20, 0.0195, new int[]{150, 0, 150, 0, 150, 0});
        callisto = new Satellite("Callisto", 1.075, 2410, 1.8827, 0.0074, 0.192, 0.045, new int[]{150, 0, 150, 0, 150, 0});
    }

    public int[] getColors(){return colors;}

    public ArrayList<String > getAllInfo(double r, double speed){
        ArrayList<String> s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 79");
        s.add("Название наиболее известных спутников: Ио, Европа, Ганимед, Калисто");
        return s;
    }
}

