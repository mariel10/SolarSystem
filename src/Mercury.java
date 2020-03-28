import java.util.ArrayList;

public class Mercury extends Planet {
    int[] colors = new int[]{125, 0, 125, 0, 115, 30};

    public Mercury() {
        super("Mercury", 3.302, 2440, 57.909,
        0.206,
        7,
        0.241,
        48,
        77);
    }

    public int[] getColors(){return colors;}
}
