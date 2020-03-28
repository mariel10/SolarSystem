import java.util.ArrayList;

public class Mars extends Planet {
    int[] colors = new int[]{180, 75, 10, 0, 0, 0};
    Satellite phobos;
    Satellite deimos;

    public Mars() {
        super("Mars", 6.419, 3397, 227.94,
                0.093,
                1.8,
                1.88,
                49,
                336);
        phobos = new Satellite("Phobos", 0.0000001072, 11, 0.093772, 0.0151, 1.093, 0.0008, new int[]{150, 0, 150, 0, 150, 0});
        deimos = new Satellite("Deimos", 0.0000000148, 6, 0.23458, 0.0002, 27.58, 0.003, new int[]{150, 0, 150, 0, 150, 0});
    }

    public int[] getColors(){return colors;}

    public ArrayList<String> getAllInfo(double r, double speed){
        ArrayList<String > s = super.getAllInfo(r, speed);
        s.add("Количество известных спутников: 2");
        s.add("Название спутников: Фобос и Деймос");
        return s;
    }
}

