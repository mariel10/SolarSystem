import java.util.ArrayList;

public class Satellite extends Planet {
    private int[] colors;
    public Satellite(String name, double mass, int radius, double a1, double e1, double i1, double T1, int[] color) {
        super(name, mass, radius, a1, e1, i1, T1);
        colors = color;
    }
    //вычисляет положение спутника в начале координат, а потом переносить центр в центр соответствующей планеты
    public double[] getCoords(double dt, double[] xyzPlanet){
        double[] xyzMoon = super.getCoords(dt);
        xyzMoon[0] += xyzPlanet[0];
        xyzMoon[1] += xyzPlanet[1];
        xyzMoon[2] += xyzPlanet[2];
        return xyzMoon;
    }
    public int[] getColors(){return colors;}
}
