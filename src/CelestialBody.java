import java.util.ArrayList;

public class CelestialBody {
    private String name;
    private double mass; // mass*10^23
    private int radius; // planet or sun radius (км)

    CelestialBody(String name1,double mass1, int radius1){
        name = name1;
        mass = mass1;
        radius = radius1;
    }
    CelestialBody(){}

    // создает начальный объект, который в последствие превращается в шар
    public ArrayList<Triangle> createSketch(double radius){
        double r = radius;
        ArrayList<Triangle> tris = new ArrayList<>();
        tris.add(new Triangle(new Vertex(r, r, r),
                new Vertex(-r, -r, r),
                new Vertex(-r, r, -r)));

        tris.add(new Triangle(new Vertex(r, r, r),
                new Vertex(-r, -r, r),
                new Vertex(r, -r, -r)));

        tris.add(new Triangle(new Vertex(-r, r, -r),
                new Vertex(r, -r, -r),
                new Vertex(r, r, r)));

        tris.add(new Triangle(new Vertex(-r, r, -r),
                new Vertex(r, -r, -r),
                new Vertex(-r, -r, r)));
        return tris;
    }

    // создание кольца для Сатурна. Создается небольшая часть кольца, которая поврачивается нужное количество раз
    public ArrayList<Triangle> createRings(double radius){
        double r = radius *2;
        double r2 = r  + r * 0.6;
        ArrayList<Triangle> tris = new ArrayList<>();
        int angle = 5;
        for (int i = 0; i < 36; i ++){
            double x1 = r * Math.cos(Math.toRadians(angle));
            double y1 = r * Math.sin(Math.toRadians(angle));
            double x2 = r * Math.cos(Math.toRadians(angle - 10));
            double y2 = r * Math.sin(Math.toRadians(angle-10));
            double x3 = r2 * Math.cos(Math.toRadians(angle));
            double y3 = r2 * Math.sin(Math.toRadians(angle));
            double x4 = r2 * Math.cos(Math.toRadians(angle - 10));
            double y4 = r2 * Math.sin(Math.toRadians(angle-10));

            tris.add(new Triangle(new Vertex(x1, y1, 0),
                    new Vertex(x2, y2, 0),
                    new Vertex(x3, y3, 0)));
            tris.add(new Triangle(new Vertex(x3, y3, 0),
                    new Vertex(x4, y4, 0),
                    new Vertex(x2, y2, 0)));
            angle += 10;
        }
        return tris;
    }

    // возвращает радиус небесного тела, увеличенный в 100 раз для наглядности (соотношение орбит остается реальным)
    public double getRadius(){return radius * Math.pow(10, -4);}
    public String getName(){return  name;}
    public String getMass(){return Double.toString(mass) + " * 10^23 kg";}
    // возвращает информацию, которая испольцуется для выдачи информации о Солнце
    public String[] getInfo(){return new String[]{"Name: " + name,
            "Mass: " + Double.toString(roundAvoid(mass * Math.pow(10, -7), 4)) + " * 10 ^30 kg",
             name + " radius: " + Double.toString(radius ) + " km"};
    }
    //округление до нужного знака
    private double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
