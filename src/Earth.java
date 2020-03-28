import java.util.ArrayList;

public class Earth extends Planet {
    int[] colors = new int[]{0, 0, 170, 85, 80, 175};
    Satellite moon;

    public Earth() {
        super("Earth", 59.74, 6378, 149.6,
                0.017,
                0,
                1,
                174,
                76);
        moon = new Satellite("Moon", 0.73477, 1737, 0.384399, // /10
                0.05,
                5.145,
                0.074,
                new int[]{150, 0, 150, 0, 150, 0});
    }

    public int[] getColors(){return colors;}

    // дополненая информация, включающая информацию о спутнике
    public ArrayList<String> getAllInfo(double r, double speed){
        ArrayList<String> s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 1");
        s.add("Название спутника: Луна");
        return s;
    }
}

