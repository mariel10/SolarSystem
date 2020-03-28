import java.util.ArrayList;

public class Uranus extends Planet {
    int[] colors = new int[]{50, 0, 180, 40, 255, 0};
    Satellite titania;
    Satellite oberon;
    Satellite ariel;
    Satellite umbriel;
    Satellite miranda;

    public Uranus() {
        super("Uranus", 86.85, 25560, 2871,
                0.047,
                0.77,
                -84.02,
                74,
                173);
        titania = new Satellite("Titania", 0.03527, 788, 0.4363, 0.0011, 0.079, 0.0023, new int[]{150, 0, 150, 0, 150, 0});
        oberon = new Satellite("Oberon", 0.03, 761, 0.58352, 0.0014, 0.058, 0.036, new int[]{150, 0, 150, 0, 150, 0});
        ariel = new Satellite("Ariel", 0.01353, 579, 0.19102, 0.0012, 0.26, 0.0068, new int[]{150, 0, 150, 0, 150, 0});
        umbriel = new Satellite("Umbriel", 0.01172, 585, 0.266, 0.0039, 0.128, 0.01, new int[]{150, 0, 150, 0, 150, 0});
        miranda = new Satellite("Miranda", 0.000659, 236, 0.1299, 0.0013, 4.338, 0.003, new int[]{150, 0, 150, 0, 150, 0});
    }


    public int[] getColors(){return colors;}

    public ArrayList<String > getAllInfo(double r, double speed){
        ArrayList<String> s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 27");
        s.add("Название наиболее известных спутников: Титания, Оберон, Ариэль, Умбриэль, Миранда");
        return s;
    }
}

