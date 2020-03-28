import java.util.ArrayList;

public class Neptune extends Planet {
    int[] colors = new int[]{50, 0, 70, 0, 200, 55};
    Satellite triton;
    Satellite nereid;


    public Neptune() {
        super("Neptune", 102.4, 24760, 4498.3,
                0.009,
                1.77,
                164.79,
                131,
                48);
        triton = new Satellite("Triton", 0.214, 1353, 0.354759, 0.000016, 130, -0.016, new int[]{150, 0, 150, 0, 150, 0});
        nereid = new Satellite("Nereid", 0.00031, 170, 0.55134, 0.7512, 27.6, 0.98, new int[]{150, 0, 150, 0, 150, 0});
    }

    public int[] getColors(){return colors;}

    public ArrayList<String > getAllInfo(double r, double speed){
        ArrayList<String> s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 14");
        s.add("Название наиболее известных спутников: Тритон, Нереида");
        return s;
    }
}

