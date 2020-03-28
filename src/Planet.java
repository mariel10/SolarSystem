import java.util.ArrayList;

public class Planet extends CelestialBody{
    //Кеплеровские элементы орбиты
    private double e; //эксцентриситет
    private double a; //большая полуось
    private double i; //наклонение
    private double q; //долгота восходящего узла
    private double w; //аргумент перицентра
    private double T; //сидерический период
    private double m0; //средняя аномалия в эпоху t0
    private double u = Math.sqrt(6.6 * Math.pow(10, -23) * 1.9855 * Math.pow(10, 30)); //гравитационная постоянная умноженная на массу Солнца

    public Planet(String name, double mass, int radius, double a1, double e1, double i1, double T1, double q1, double w1){
        super(name, mass, radius);
        a = a1;
        e = e1;
        i = i1;
        q = q1;
        w = w1;
        if (T1 != 0)
            T = T1;
        else
            T = (a * Math.sqrt(a) * 2 * Math.PI) / u;
        m0 = (Math.PI* 2) / T;
    }

    public Planet(String name, double mass, int radius, double a1, double e1, double i1, double T1){ //for satellites
        super(name, mass, radius);
        a = a1 * Math.pow(10, 2); // otherwise satellites will not be seen because of the planets' increased size
        e = e1;
        T = T1;
        i = i1;
        w = 0;
        q = 0;
        m0 = (Math.PI* 2) / T1;
    }

    //вычисляет координаты положения небесного тела в зависимости от прошедшего времени.
    //Возвращает координату по x по y по z, а также радиус и скорость в текущий момент
    public double[] getCoords(double dt){
        double[] result;

        //position count
        double n = u / (this.a * Math.sqrt(this.a));
        double m = this.m0 + n * dt;

        double E = this.e * Math.sin(m) + m;
        for (int i = 0; i < 10; i ++){
            E = this.e * Math.sin(E) + m;
        }
        double v = 2 * Math.atan(Math.sqrt(1 + this.e) / (1 - this.e) * Math.tan(E / 2));
        double u = v + Math.toRadians(w);
        double gamma = Math.sin(u) * Math.sin(Math.toRadians(this.i));
        double alpha = Math.cos(Math.toRadians(this.q)) * Math.cos(u) - Math.sin(Math.toRadians(this.q)) * Math.sin(u) * Math.cos(Math.toRadians(this.i));
        double beta = Math.sin(Math.toRadians(this.q)) * Math.cos(u) + Math.cos(Math.toRadians(this.q)) * Math.sin(u) * Math.cos(Math.toRadians(this.i));
        double r = this.a * (1 - this.e * this.e) / (1 + this.e * Math.cos(v));

        //speed count
        double speed = (631.3 * Math.sqrt(1985500/(5.974 * r * Math.pow(10, 6)))) * Math.sqrt(2 * this.a / r - 1);
        //getting result
        result = new double[]{r * alpha, r * beta, r * gamma, r, speed};
        return result;
    }

    //возвращает значение большей полуоси
    public double getA(){return this.a;}

    //Собирает информацию для вывода на экран
    public ArrayList<String> getAllInfo(double r, double speed){
        ArrayList<String> s = new ArrayList<>();
        s.add("Название: " + super.getName());
        s.add("Масса: " + super.getMass());
        s.add("Радиус планеты: " + Double.toString(super.getRadius() * Math.pow(10, 4)) + " км");
        s.add("Большая полуось: " + Double.toString(a) + " млн км");
        s.add("Эксцентриситет: " + Double.toString(e));
        s.add("Период обращения: " + Double.toString(T) + " лет");
        s.add("Наклонение: " + Double.toString(i));
        s.add("Аргумент перицентра: " + Double.toString(w));
        s.add("Долгота восходящего узла: " + Double.toString(q));
        s.add("Текущее расстояние до Солнца: " + Double.toString(r) + " млн км");
        s.add("Текущая скорость: " + Double.toString(speed) + " км.сек");
        return s;
    }
}
