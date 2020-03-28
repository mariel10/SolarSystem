import java.util.ArrayList;

public class Sun extends CelestialBody{
    int[] colors = new int[]{255, 0, 110, 145, 0, 0};
    Sun(){
        super("Sun", 19855000, 696340);
    }


    //public double getRadius(){return super.getRadius();}
    public int[] getColors(){return colors;}
}
