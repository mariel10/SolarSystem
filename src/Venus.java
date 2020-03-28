public class Venus extends Planet{
    int[] colors = new int[]{255, 0, 210, 45, 0, 0};
    public Venus() {
        super("Venus", 4869, 6052, 108.21, 0.007, 3.39, -0.615, 76, 131);
    }

    public int[] getColors(){return colors;}
}
