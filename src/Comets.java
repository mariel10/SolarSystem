import java.util.ArrayList;

public class Comets{
    private String name;
    private int radius;
    private double mass;
    private double e; //эксцентриситет
    private double a; //большая полуось
    private double i; //наклонение
    private double q; //долгота восходящего узла
    private double w; //аргумент перицентра
    private double T; //сидерический период
    private double m0; //средняя аномалия в эпоху t0
    private double u = Math.sqrt(6.6 * Math.pow(10, -23) * 1.9855 * Math.pow(10, 30));

    int[] colors = new int[]{150, 0, 150, 0, 150 ,0};

    public Comets(String name1, double mass1, int radius1, double a1, double e1, double i1, double T1, double q1, double w1) {
        name = name1;
        radius = radius1;
        mass = mass1;
        a = a1;
        e = e1;
        i = i1;
        q = q1;
        w = w1;
        T = (a * Math.sqrt(a) * 2 * Math.PI) / u;
        m0 = (Math.PI* 2) / T;
    }
    public int[] getColors(){return colors;}

    public double getRadius(){return radius * Math.pow(10, -4);}

    public String getName(){return name;}

    public double getA(){return a;}
    public double getE(){return e;}

    // вычисляет положение кометы
    public double[] getCoords(double dt){

        double[] result;
        double r;
        double u1;
        if ((e >= 0) && (e < 1)) {
            double n = u / (this.a * Math.sqrt(this.a));
            double m = this.m0 + n * dt;

            double E = this.e * Math.sin(m) + m;
            for (int i = 0; i < 10; i++) {
                E = this.e * Math.sin(E) + m;
            }
            double v = 2 * Math.atan(Math.sqrt(1 + this.e) / (1 - this.e) * Math.tan(E / 2));
            u1 = v + Math.toRadians(w);
            r = this.a * (1 - this.e * this.e) / (1 + this.e * Math.cos(v));
        }else if ( e > 1){
            dt -= 100;
            double n = u / (this.a * Math.sqrt(this.a));
            double m = this.m0 + n * dt;

            double H = Math.log(Math.tan(m/2 + Math.toRadians(45)));
            double Hn = Math.log((m + H)/this.e + Math.sqrt(((m + H)/this.e) * ((m + H)/this.e) + 1));
            for (int i = 0; i < 10; i++) {
                Hn = Math.log((m + Hn)/this.e + Math.sqrt(((m + Hn)/this.e) * ((m + Hn)/this.e) + 1));;
            }

            double v = 2 * Math.atan(Math.sqrt(1 + this.e) / (1 - this.e) * Math.tanh(Hn / 2));
            u1 = v + Math.toRadians(w);
            r = this.a * ( - 1 + this.e * this.e) / (1 + this.e * Math.cos(v));
        }else{
            u1 = w;
            r = a + (dt - 100);
        }

        double gamma = Math.sin(u1) * Math.sin(Math.toRadians(this.i));
        double alpha = Math.cos(Math.toRadians(this.q)) * Math.cos(u1) - Math.sin(Math.toRadians(this.q)) * Math.sin(u1) * Math.cos(Math.toRadians(this.i));
        double beta = Math.sin(Math.toRadians(this.q)) * Math.cos(u1) + Math.cos(Math.toRadians(this.q)) * Math.sin(u1) * Math.cos(Math.toRadians(this.i));

        //result = new double[] {r * Math.cos(v), r * Math.sin(v), r * gamma, r};
        result = new double[]{r * alpha, r * beta, r * gamma, r};
        return result;
    }
}
