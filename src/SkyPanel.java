import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SkyPanel extends JPanel implements ActionListener {
    private Timer timer;
    private int timerTime = 100;

    //flag to start simulation
    private boolean start = false;

    //only one time needed
    private boolean setPlanets = false;

    private int planet = 0; // set focus on planet. Changes from 0 (Sun) to 8  (Neptune)
    private boolean setNamesVisible = false;
    // delta time
    private double dt = 0;
    private double dtAdd = 0.001;
    //Celestial bodies created and their forms
    private Sun sun = new Sun();
    private List<Triangle> oneForAll = new CelestialBody().createSketch(1);
    private Mercury mercury = new Mercury();
    private ArrayList<Vertex> orbitMercury = new ArrayList<>();
    private Venus venus = new Venus();
    private ArrayList<Vertex> orbitVenus = new ArrayList<>();
    private Earth earth = new Earth();
    private ArrayList<Vertex> orbitEarth = new ArrayList<>();
    private Mars mars = new Mars();
    private ArrayList<Vertex> orbitMars = new ArrayList<>();
    private Jupiter jupiter = new Jupiter();
    private ArrayList<Vertex> orbitJupiter = new ArrayList<>();
    private Saturn saturn = new Saturn();
    private ArrayList<Vertex> orbitSaturn = new ArrayList<>();
    private ArrayList<Triangle> ringsSaturn;
    private Uranus uranus = new Uranus();
    private ArrayList<Vertex> orbitUranus = new ArrayList<>();
    private Neptune neptune = new Neptune();
    private ArrayList<Vertex> orbitNeptune = new ArrayList<>();

    private Comets halley = new Comets("1P/Halley", 0.000000000022, 5, 2667.950, 0.9671429, 162.3, 75.3, 58.42, 111.33249);
    private ArrayList<Vertex> orbitHalley = new ArrayList<>();

    private Comets myComet;
    private ArrayList<Vertex> orbitMyComet = new ArrayList<>();
    private boolean comet = false;
    private boolean cometOrbit = true;

    private Matrix3 I = new Matrix3(new double[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    });
    // control
    private boolean showOrbits = false;
    private boolean showBackground = true;
    private Color[] orbitColor = new Color[]{Color.CYAN, Color.GRAY};
    private int colorIndex = 0;
    private int showAllInfo = -1; // -1 no info, 0 - Sun, 1 to 8 - Planets
    private double scale = 10;
    private int a = 0, b = 0, c = 0, px = 0, py = 0, pz =0;
    // rotation angles
    private double heading = 0;
    private double pitch = 0;
//    double over = 0;
//    ------------------------------------------------------------------------------------------------------------------
    public SkyPanel() {
        timer = new Timer(timerTime, this);
        timer.start();
        addKeyListener(new KeyBoard());
        setFocusable(true);
    }

    public void setStart(){
        start = true;
        dt = 0;
        requestFocusInWindow();
        a = 0;
        b = 0;
        c = 0;
        px = 0;
        py = 0;
        pz = 0;
        dtAdd = 0.001;
        heading = 0;
        pitch = 0;
        scale = 10;
        showOrbits = false;
        planet = 0;
        setNamesVisible = false;
        showAllInfo = -1;
        colorIndex = 0;
        showBackground = true;
    }
    public void stopStart(){
        start = false;
        requestFocusInWindow(false);
        orbitMyComet.clear();
        cometOrbit = true;
        comet = false;
    }

    public void setMyComet(String name, double[] aeiwq, int radius){
        comet = true;
        myComet = new Comets(name, 0, radius, aeiwq[0], aeiwq[1], aeiwq[2], 0,aeiwq[4],aeiwq[3]);
    }

    public void paintComponent(Graphics g) {
        if (showBackground) {
            Image image = null;
            try {
                image = ImageIO.read(new File("pictures/milky-way.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        if (!showBackground) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (!setPlanets) {

            for (int i = 0; i < 4; i++) {
                oneForAll = inflate(oneForAll, 1);
            }
            createOrbit(orbitMercury, mercury, 1);
            createOrbit(orbitVenus, venus, 1);
            createOrbit(orbitEarth, earth, 1);
            createOrbit(orbitMars, mars, 1);
            createOrbit(orbitJupiter, jupiter, 4);
            createOrbit(orbitSaturn, saturn, 16);
            ringsSaturn = saturn.createRings(Math.sqrt(oneForAll.get(0).v1.x * oneForAll.get(0).v1.x  *saturn.getRadius() * saturn.getRadius() + oneForAll.get(0).v1.y * oneForAll.get(0).v1.y * saturn.getRadius()*saturn.getRadius()));
            createOrbit(orbitUranus, uranus, 64);
            createOrbit(orbitNeptune, neptune, 256);

            createOrbitComet(orbitHalley, halley, 512);

            setPlanets = true;
        }

        Matrix3 headingTransform = new Matrix3(new double[]{
                Math.cos(Math.toRadians(heading)), 0, -Math.sin(Math.toRadians(heading)), 0,
                0, 1, 0, 0,
                Math.sin(Math.toRadians(heading)), 0, Math.cos(Math.toRadians(heading)), 0,
                0, 0, 0, 1
        });

        Matrix3 pitchTransform = new Matrix3(new double[]{
                1, 0, 0, 0,
                0, Math.cos(Math.toRadians(pitch)), Math.sin(Math.toRadians(pitch)), 0,
                0, -Math.sin(Math.toRadians(pitch)), Math.cos(Math.toRadians(pitch)), 0,
                0, 0, 0, 1
        });


        Matrix3 rotationTransform = headingTransform.multiply4(pitchTransform);
        //for Mercury

        double[] xyzMercury = mercury.getCoords(dt);
        Matrix3 moveMercury = new Matrix3().getMoveMatrix(xyzMercury);

        //for Venus

        double[] xyzVenus = venus.getCoords(dt);
        Matrix3 moveVenus = new Matrix3().getMoveMatrix(xyzVenus);

        //for Earth

        double[] xyzEarth = earth.getCoords(dt);
        Matrix3 moveEarth = new Matrix3().getMoveMatrix(xyzEarth);

        //for Mars
        double[] xyzMars = mars.getCoords(dt);
        Matrix3 moveMars = new Matrix3().getMoveMatrix(xyzMars);

        //for Jupiter
        double[] xyzJupiter = jupiter.getCoords(dt);
        Matrix3 moveJupiter = new Matrix3().getMoveMatrix(xyzJupiter);

        //for Saturn
        double[] xyzSaturn = saturn.getCoords(dt);
        Matrix3 moveSaturn = new Matrix3().getMoveMatrix(xyzSaturn);

        //for Uranus
        double[] xyzUranus = uranus.getCoords(dt);
        Matrix3 moveUranus = new Matrix3().getMoveMatrix(xyzUranus);

        //for Neptune
        double[] xyzNeptune = neptune.getCoords(dt);
        Matrix3 moveNeptune = new Matrix3().getMoveMatrix(xyzNeptune);

        //for satellites
        //earth
        double[] xyzMoon = earth.moon.getCoords(dt, xyzEarth);
        Matrix3 moveMoon = new Matrix3().getMoveMatrix(xyzMoon);

        //mars
        double[] xyzPhobos = mars.phobos.getCoords(dt, xyzMars);
        Matrix3 movePhobos = new Matrix3().getMoveMatrix(xyzPhobos);

        double[] xyzDeimos = mars.deimos.getCoords(dt, xyzMars);
        Matrix3 moveDeimos = new Matrix3().getMoveMatrix(xyzDeimos);

        //jupiter
        double[] xyzIo = jupiter.io.getCoords(dt, xyzJupiter);
        Matrix3 moveIo = new Matrix3().getMoveMatrix(xyzIo);

        double[] xyzEuropa = jupiter.europa.getCoords(dt, xyzJupiter);
        Matrix3 moveEuropa = new Matrix3().getMoveMatrix(xyzEuropa);

        double[] xyzGanymede = jupiter.ganymede.getCoords(dt, xyzJupiter);
        Matrix3 moveGanymede = new Matrix3().getMoveMatrix(xyzGanymede);

        double[] xyzCallisto = jupiter.callisto.getCoords(dt, xyzJupiter);
        Matrix3 moveCallisto = new Matrix3().getMoveMatrix(xyzCallisto);

        //saturn
        double[] xyzTitan = saturn.titan.getCoords(dt, xyzSaturn);
        Matrix3 moveTitan = new Matrix3().getMoveMatrix(xyzTitan);

        double[] xyzIapetus = saturn.iapetus.getCoords(dt, xyzSaturn);
        Matrix3 moveIapetus = new Matrix3().getMoveMatrix(xyzIapetus);

        double[] xyzTethys = saturn.tethys.getCoords(dt, xyzSaturn);
        Matrix3 moveTethys = new Matrix3().getMoveMatrix(xyzTethys);

        double[] xyzDione = saturn.dione.getCoords(dt, xyzSaturn);
        Matrix3 moveDione = new Matrix3().getMoveMatrix(xyzDeimos);

        double[] xyzRhea = saturn.rhea.getCoords(dt, xyzSaturn);
        Matrix3 moveRhea = new Matrix3().getMoveMatrix(xyzRhea);

        //uranus
        double[] xyzTitania = uranus.titania.getCoords(dt, xyzUranus);
        Matrix3 moveTitania = new Matrix3().getMoveMatrix(xyzTitania);

        double[] xyzOberon = uranus.oberon.getCoords(dt, xyzUranus);
        Matrix3 moveOberon = new Matrix3().getMoveMatrix(xyzOberon);

        double[] xyzAriel = uranus.ariel.getCoords(dt, xyzUranus);
        Matrix3 moveAriel = new Matrix3().getMoveMatrix(xyzAriel);

        double[] xyzUmbriel = uranus.umbriel.getCoords(dt, xyzUranus);
        Matrix3 moveUmbriel = new Matrix3().getMoveMatrix(xyzUmbriel);

        double[] xyzMiranda = uranus.miranda.getCoords(dt, xyzUranus);
        Matrix3 moveMiranda = new Matrix3().getMoveMatrix(xyzMiranda);

        //neptune
        double[] xyzTriton = neptune.triton.getCoords(dt, xyzNeptune);
        Matrix3 moveTriton = new Matrix3().getMoveMatrix(xyzTriton);

        double[] xyzNereid = neptune.nereid.getCoords(dt, xyzNeptune);
        Matrix3 moveNereid = new Matrix3().getMoveMatrix(xyzNereid);

        //comets
        double[] xyzHalley = halley.getCoords(dt);
        Matrix3 moveHalley = new Matrix3().getMoveMatrix(xyzHalley);


        //size
        Matrix3 sunScale = new Matrix3().getScaleMatrix(sun.getRadius() * Math.pow(10, -1));
        Matrix3 mercuryScale = new Matrix3().getScaleMatrix(mercury.getRadius());
        Matrix3 venusScale = new Matrix3().getScaleMatrix(venus.getRadius());
        Matrix3 earthScale = new Matrix3().getScaleMatrix(earth.getRadius());
        Matrix3 marsScale = new Matrix3().getScaleMatrix(mars.getRadius());

        Matrix3 moonScale = new Matrix3().getScaleMatrix(earth.moon.getRadius());
        Matrix3 phobosScale = new Matrix3().getScaleMatrix(mars.phobos.getRadius());
        Matrix3 deimosScale = new Matrix3().getScaleMatrix(mars.deimos.getRadius());

        Matrix3 jupiterScale = new Matrix3().getScaleMatrix(jupiter.getRadius());
        Matrix3 saturnScale = new Matrix3().getScaleMatrix(saturn.getRadius());
        Matrix3 uranusScale = new Matrix3().getScaleMatrix(uranus.getRadius());
        Matrix3 neptuneScale = new Matrix3().getScaleMatrix(neptune.getRadius());

        Matrix3 ioScale = new Matrix3().getScaleMatrix(jupiter.io.getRadius());
        Matrix3 europaScale = new Matrix3().getScaleMatrix(jupiter.europa.getRadius());
        Matrix3 ganymedeScale = new Matrix3().getScaleMatrix(jupiter.ganymede.getRadius());
        Matrix3 callistoScale = new Matrix3().getScaleMatrix(jupiter.callisto.getRadius());

        Matrix3 titanScale = new Matrix3().getScaleMatrix(saturn.titan.getRadius());
        Matrix3 iapetusScale = new Matrix3().getScaleMatrix(saturn.iapetus.getRadius());
        Matrix3 tethysScale = new Matrix3().getScaleMatrix(saturn.tethys.getRadius());
        Matrix3 dioneScale = new Matrix3().getScaleMatrix(saturn.dione.getRadius());
        Matrix3 rheaScale = new Matrix3().getScaleMatrix(saturn.rhea.getRadius());

        Matrix3 titaniaScale = new Matrix3().getScaleMatrix(uranus.titania.getRadius());
        Matrix3 oberonScale = new Matrix3().getScaleMatrix(uranus.oberon.getRadius());
        Matrix3 arielScale = new Matrix3().getScaleMatrix(uranus.ariel.getRadius());
        Matrix3 umbrielScale = new Matrix3().getScaleMatrix(uranus.umbriel.getRadius());
        Matrix3 mirandaScale = new Matrix3().getScaleMatrix(uranus.miranda.getRadius());

        Matrix3 tritonScale = new Matrix3().getScaleMatrix(neptune.triton.getRadius());
        Matrix3 nereidScale = new Matrix3().getScaleMatrix(neptune.nereid.getRadius());

        Matrix3 halleyScale = new Matrix3().getScaleMatrix(halley.getRadius());

        //for all objects
        Matrix3 allScale = new Matrix3(new double[]{
                scale, 0, 0, 0,
                0, scale, 0, 0,
                0, 0, scale, 0,
                0, 0, 0, 1
        });
        if (planet != 0) {
            if (planet == 1) {
                px = (int) (xyzMercury[0] * scale);
                py = (int) (xyzMercury[1] * scale);
                pz = (int) (xyzMercury[2] * scale);
            }else
            if (planet == 2) {
                px = (int) (xyzVenus[0] * scale);
                py = (int) (xyzVenus[1] * scale);
                pz = (int) (xyzVenus[2] * scale);
            }else
            if (planet == 3) {
                px = (int) (xyzEarth[0] * scale);
                py = (int) (xyzEarth[1] * scale);
                pz = (int) (xyzEarth[2] * scale);
            }else
            if (planet == 4) {
                px = (int) (xyzMars[0] * scale);
                py = (int) (xyzMars[1] * scale);
                pz = (int) (xyzMars[2] * scale);
            }else
            if (planet == 5) {
                px = (int) (xyzJupiter[0] * scale);
                py = (int) (xyzJupiter[1] * scale);
                pz = (int) (xyzJupiter[2] * scale);
            }else
            if (planet == 6) {
                px = (int) (xyzSaturn[0] * scale);
                py = (int) (xyzSaturn[1] * scale);
                pz = (int) (xyzSaturn[2] * scale);
            }else
            if (planet == 7) {
                px = (int) (xyzUranus[0] * scale);
                py = (int) (xyzUranus[1] * scale);
                pz = (int) (xyzUranus[2] * scale);
            }else
            if (planet == 8) {
                px = (int) (xyzNeptune[0] * scale);
                py = (int) (xyzNeptune[1] * scale);
                pz = (int) (xyzNeptune[2] * scale);
            }
            if (planet == 10){
                px = (int) (xyzHalley[0] * scale);
                py = (int) (xyzHalley[1] * scale);
                pz = (int) (xyzHalley[2] * scale);
            }
        }

        Matrix3 movePlanet = new Matrix3(new double[]{
                -1, 0, 0, px ,
                0, -1, 0, py ,
                0, 0, -1, pz ,
                0, 0, 0, -1
        });
        Matrix3 moveMatrix = new Matrix3(new double[]{
                1, 0, 0, a,
                0, 1, 0, b,
                0, 0, 1, c,
                0, 0, 0, 1
        });


        // this should be applied to all objects
        Matrix3 transform;
        if (planet == 0){
            transform = moveMatrix.multiply4(allScale.multiply4(rotationTransform));
        }else{
            transform = moveMatrix.multiply4(movePlanet.multiply4(allScale.multiply4(rotationTransform)));
        }

        // personal object position
        Matrix3 transformSun = transform.multiply4(I);
        Matrix3 transformMercury = transform.multiply4(moveMercury);
        Matrix3 transformVenus = transform.multiply4(moveVenus);
        Matrix3 transformEarth = transform.multiply4(moveEarth);
        Matrix3 transformMars = transform.multiply4(moveMars);
        Matrix3 transformJupiter = transform.multiply4(moveJupiter);
        Matrix3 transformSaturn = transform.multiply4(moveSaturn);
        Matrix3 transformUranus = transform.multiply4(moveUranus);
        Matrix3 transformNeptune = transform.multiply4(moveNeptune);

        //for satellites
        Matrix3 transformMoon = transform.multiply4(moveMoon);
        Matrix3 transformPhobos = transform.multiply4(movePhobos);
        Matrix3 transformDeimos = transform.multiply4(moveDeimos);
        Matrix3 transformIo = transform.multiply4(moveIo);
        Matrix3 transformEuropa = transform.multiply4(moveEuropa);
        Matrix3 transformGanymede = transform.multiply4(moveGanymede);
        Matrix3 transformCallisto = transform.multiply4(moveCallisto);
        Matrix3 transformTitan = transform.multiply4(moveTitan);
        Matrix3 transformIapetus = transform.multiply4(moveIapetus);
        Matrix3 transformTethys = transform.multiply4(moveTethys);
        Matrix3 transformDione = transform.multiply4(moveDione);
        Matrix3 transformRhea = transform.multiply4(moveRhea);
        Matrix3 transformTitania = transform.multiply4(moveTitania);
        Matrix3 transformOberon = transform.multiply4(moveOberon);
        Matrix3 transformAriel = transform.multiply4(moveAriel);
        Matrix3 transformUmbriel = transform.multiply4(moveUmbriel);
        Matrix3 transformMiranda = transform.multiply4(moveMiranda);
        Matrix3 transformTriton = transform.multiply4(moveTriton);
        Matrix3 transformNereid = transform.multiply4(moveNereid);

        //for comets
        Matrix3 transformHalley = transform.multiply4(moveHalley);


        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);


        double[] zBuffer = new double[img.getWidth() * img.getHeight()];
        // initialize array with extremely far away depths
        for (int q = 0; q < zBuffer.length; q++) {
            zBuffer[q] = Double.NEGATIVE_INFINITY;
        }

        if (setNamesVisible) {
            double r = findDrawnRadius(oneForAll);
            if (scale >= 0.1) {
                drawNames(g2, transform, xyzMercury, "Mercury", r * mercury.getRadius());
                drawNames(g2, transform, xyzVenus, "Venus", r * venus.getRadius());
                drawNames(g2, transform, xyzEarth, "Earth",r * earth.getRadius());
                drawNames(g2, transform, xyzMars, "Mars", r * mars.getRadius());

                if (scale >= 1) {
                    drawNames(g2, transform, xyzMoon, "Moon", r * earth.moon.getRadius());
                    drawNames(g2, transform, xyzPhobos, "Phobos", r * mars.phobos.getRadius());
                    drawNames(g2, transform, xyzDeimos, "Deimos", r * mars.deimos.getRadius());
                    drawNames(g2, transform, xyzIo, "Io", r * jupiter.io.getRadius());
                    drawNames(g2, transform, xyzEuropa, "Europa", r * jupiter.europa.getRadius());
                    drawNames(g2, transform, xyzGanymede, "Ganymede", r * jupiter.ganymede.getRadius());
                    drawNames(g2, transform, xyzCallisto, "Callisto", r * jupiter.callisto.getRadius());
                    drawNames(g2, transform, xyzTitan, "Titan", r * saturn.titan.getRadius());
                    drawNames(g2, transform, xyzIapetus, "Iapetus", r * saturn.iapetus.getRadius());
                    drawNames(g2, transform, xyzTethys, "Tethys", r * saturn.tethys.getRadius());
                    drawNames(g2, transform, xyzDione, "Dione", r * saturn.dione.getRadius());
                    drawNames(g2, transform, xyzRhea, "Rhea", r * saturn.rhea.getRadius());
                    drawNames(g2, transform, xyzTitania, "Titania", r * uranus.titania.getRadius());
                    drawNames(g2, transform, xyzOberon, "Oberon", r * uranus.oberon.getRadius());
                    drawNames(g2, transform, xyzAriel, "Ariel", r * uranus.ariel.getRadius());
                    drawNames(g2, transform, xyzUmbriel, "Umbriel", r * uranus.umbriel.getRadius());
                    drawNames(g2, transform, xyzMiranda, "Miranda", r * uranus.miranda.getRadius());
                    drawNames(g2, transform, xyzTriton, "Triton", r * neptune.triton.getRadius());
                    drawNames(g2, transform, xyzNereid, "Nereid", r * neptune.nereid.getRadius());
                }

            }
            drawNames(g2, transform, xyzJupiter, "Jupiter", r * jupiter.getRadius());
            drawNames(g2, transform, xyzSaturn, "Saturn", r * saturn.getRadius());
            drawNames(g2, transform, xyzUranus, "Uranus", r * uranus.getRadius());
            drawNames(g2, transform, xyzNeptune, "Neptune", r * neptune.getRadius());

            drawNames(g2, transform, xyzHalley, "1P/Halley", r*halley.getRadius());

        }

        if (showAllInfo == 0) {
            drawStarInfo(g2,sun);
        }else if (showAllInfo == 1){
            drawInfo(g2, mercury, xyzMercury[3],xyzMercury[4]);
            drawSelectedOrbit(g2, transform, xyzMercury, mercury.getRadius() + 5);
        }else if(showAllInfo == 2){
            drawInfo(g2, venus, xyzVenus[3], xyzVenus[4]);
            drawSelectedOrbit(g2, transform, xyzVenus, venus.getRadius() + 5);
        }else if(showAllInfo == 3){
            drawInfo(g2, earth, xyzEarth[3], xyzEarth[4]);
            drawSelectedOrbit(g2, transform, xyzEarth, earth.getRadius() + 5);
        }else if(showAllInfo == 4){
            drawInfo(g2, mars, xyzMars[3], xyzMars[4]);
            drawSelectedOrbit(g2, transform, xyzMars, mars.getRadius() + 5);
        }else if(showAllInfo == 5){
            drawInfo(g2, jupiter, xyzJupiter[3], xyzJupiter[4]);
            drawSelectedOrbit(g2, transform, xyzJupiter, jupiter.getRadius() + 5);
        }else if(showAllInfo == 6){
            drawInfo(g2, saturn, xyzSaturn[3], xyzSaturn[4]);
            drawSelectedOrbit(g2, transform, xyzSaturn, saturn.getRadius() + 5);
        }else if(showAllInfo == 7){
            drawInfo(g2, uranus, xyzUranus[3], xyzUranus[4]);
            drawSelectedOrbit(g2, transform, xyzUranus, uranus.getRadius() + 5);
        }else if(showAllInfo == 8){
            drawInfo(g2, neptune, xyzNeptune[3], xyzNeptune[4]);
            drawSelectedOrbit(g2, transform, xyzNeptune, neptune.getRadius() + 5);
        }

        drawImage(img, oneForAll, zBuffer, transformSun.multiply4(sunScale), sun.getColors());
        drawImage(img,oneForAll,zBuffer, transformHalley.multiply4(halleyScale), halley.getColors());
        if (scale >= 0.1) {
            drawImage(img, oneForAll, zBuffer, transformMercury.multiply4(mercuryScale), mercury.getColors());
            drawImage(img, oneForAll, zBuffer, transformVenus.multiply4(venusScale), venus.getColors());
            drawImage(img, oneForAll, zBuffer, transformEarth.multiply4(earthScale), earth.getColors());
            drawImage(img, oneForAll, zBuffer, transformMars.multiply4(marsScale), mars.getColors());

            drawImage(img, oneForAll, zBuffer, transformMoon.multiply4(moonScale), earth.moon.getColors());

            drawImage(img, oneForAll, zBuffer, transformPhobos.multiply4(phobosScale), mars.phobos.getColors());
            drawImage(img, oneForAll, zBuffer, transformDeimos.multiply4(deimosScale), mars.deimos.getColors());

            drawImage(img, oneForAll, zBuffer, transformIo.multiply4(ioScale), jupiter.io.getColors());
            drawImage(img, oneForAll, zBuffer, transformEuropa.multiply4(europaScale), jupiter.europa.getColors());
            drawImage(img, oneForAll, zBuffer, transformGanymede.multiply4(ganymedeScale), jupiter.ganymede.getColors());
            drawImage(img, oneForAll, zBuffer, transformCallisto.multiply4(callistoScale), jupiter.callisto.getColors());

            drawImage(img, oneForAll, zBuffer, transformTitan.multiply4(titanScale), saturn.titan.getColors());
            drawImage(img, oneForAll, zBuffer, transformIapetus.multiply4(iapetusScale), saturn.iapetus.getColors());
            drawImage(img, oneForAll, zBuffer, transformTethys.multiply4(tethysScale), saturn.tethys.getColors());
            drawImage(img, oneForAll, zBuffer, transformDione.multiply4(dioneScale), saturn.dione.getColors());
            drawImage(img, oneForAll, zBuffer, transformRhea.multiply4(rheaScale), saturn.rhea.getColors());

            drawImage(img, oneForAll, zBuffer, transformTitania.multiply4(titaniaScale), uranus.titania.getColors());
            drawImage(img, oneForAll, zBuffer, transformOberon.multiply4(oberonScale), uranus.oberon.getColors());
            drawImage(img, oneForAll, zBuffer, transformAriel.multiply4(arielScale), uranus.ariel.getColors());
            drawImage(img, oneForAll, zBuffer, transformUmbriel.multiply4(umbrielScale), uranus.umbriel.getColors());
            drawImage(img, oneForAll, zBuffer, transformMiranda.multiply4(mirandaScale), uranus.miranda.getColors());

            drawImage(img, oneForAll, zBuffer, transformTriton.multiply4(tritonScale), neptune.triton.getColors());
            drawImage(img, oneForAll, zBuffer, transformNereid.multiply4(nereidScale), neptune.nereid.getColors());
        }
        if ((scale < 0.8) || (a == 0) || (b == 0) || (c == 0) || (px == 0) || (py == 0) || (pz == 0)) {
            drawImage(img, oneForAll, zBuffer, transformJupiter.multiply4(jupiterScale), jupiter.getColors());
            drawImage(img, oneForAll, zBuffer, transformSaturn.multiply4(saturnScale), saturn.getColors());
            drawImage(img, ringsSaturn, zBuffer, transformSaturn, saturn.getColors());
            drawImage(img, oneForAll, zBuffer, transformUranus.multiply4(uranusScale), uranus.getColors());
            drawImage(img, oneForAll, zBuffer, transformNeptune.multiply4(neptuneScale), neptune.getColors());
        }

        if (comet){
            if (cometOrbit) {
                if (myComet.getE() < 1){
                    createOrbitComet(orbitMyComet, myComet, 512);
                }else
                    createOrbitComet(orbitMyComet, myComet, 2048);
                cometOrbit = false;
            }
            double[] xyzMyComet = myComet.getCoords(dt);
            Matrix3 moveMyComet = new Matrix3().getMoveMatrix(xyzMyComet);
            Matrix3 myCometScale = new Matrix3().getScaleMatrix(myComet.getRadius());
            Matrix3 transformMyComet = transform.multiply4(moveMyComet);
            if (setNamesVisible){
                drawNames(g2, transform, xyzMyComet, myComet.getName(), findDrawnRadius(oneForAll) * myComet.getRadius());
            }
            drawImage(img, oneForAll, zBuffer, transformMyComet.multiply4(myCometScale), myComet.getColors());

        }

        g2.drawImage(img, 0, 0, null);
        if (showOrbits) {
            drawOrbit(g2, transform, orbitMercury);
            drawOrbit(g2, transform, orbitVenus);
            drawOrbit(g2, transform, orbitEarth);
            drawOrbit(g2, transform, orbitMars);
            drawOrbit(g2, transform, orbitJupiter);
            drawOrbit(g2, transform, orbitSaturn);
            drawOrbit(g2, transform, orbitUranus);
            drawOrbit(g2, transform, orbitNeptune);

            drawOrbit(g2, transform, orbitHalley);

            if (comet){
                drawOrbit(g2, transform, orbitMyComet);
            }
        }


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (start) {
            dt += dtAdd;
            repaint();
        }
    }

    public static List<Triangle> inflate(List<Triangle> tris, double radius) {
        List<Triangle> result = new ArrayList<>();
        for (Triangle t : tris) {
            Vertex m1 = new Vertex((t.v1.x + t.v2.x) / 2, (t.v1.y + t.v2.y) / 2, (t.v1.z + t.v2.z) / 2);
            Vertex m2 = new Vertex((t.v2.x + t.v3.x) / 2, (t.v2.y + t.v3.y) / 2, (t.v2.z + t.v3.z) / 2);
            Vertex m3 = new Vertex((t.v1.x + t.v3.x) / 2, (t.v1.y + t.v3.y) / 2, (t.v1.z + t.v3.z) / 2);
            result.add(new Triangle(t.v1, m1, m3));
            result.add(new Triangle(t.v2, m1, m2));
            result.add(new Triangle(t.v3, m2, m3));
            result.add(new Triangle(m1, m2, m3));
        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[]{t.v1, t.v2, t.v3}) {
                double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(3 * radius);
                v.x /= l;
                v.y /= l;
                v.z /= l;
            }
        }
        return result;
    }

    public static Color getShade(int[] colors, double shade) {
        double redLinear = Math.pow(colors[0] + (int) colors[1] * Math.random(), 2.4) * shade;
        double greenLinear = Math.pow(colors[2] + (int) colors[3] * Math.random(), 2.4) * shade;
        double blueLinear = Math.pow(colors[4] + (int) colors[5] * Math.random(), 2.4) * shade;

        int red = (int) Math.pow(redLinear, 1 / 2.4);
        int green = (int) Math.pow(greenLinear, 1 / 2.4);
        int blue = (int) Math.pow(blueLinear, 1 / 2.4);

        return new Color(red, green, blue);
    }

    private void drawImage(BufferedImage img, List<Triangle> tris, double[] zBuffer, Matrix3 transform, int[] colors) {
        for (Triangle t : tris) {
            Vertex v1 = transform.transform4(t.v1);
            v1.x += getWidth() / 2;
            v1.y += getHeight() / 2;
            Vertex v2 = transform.transform4(t.v2);
            v2.x += getWidth() / 2;
            v2.y += getHeight() / 2;
            Vertex v3 = transform.transform4(t.v3);
            v3.x += getWidth() / 2;
            v3.y += getHeight() / 2;

            Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
            Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
            Vertex norm = new Vertex(
                    ab.y * ac.z - ab.z * ac.y,
                    ab.z * ac.x - ab.x * ac.z,
                    ab.x * ac.y - ab.y * ac.x
            );
            double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
            norm.x /= normalLength;
            norm.y /= normalLength;
            norm.z /= normalLength;


            double angleCos = Math.abs(norm.z);

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

            double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
                    double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
                    double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
                    if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
                        double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
                        int zIndex = y * img.getWidth() + x;
                        if (zBuffer[zIndex] < depth) {
                            img.setRGB(x, y, getShade(colors, angleCos).getRGB());
                            zBuffer[zIndex] = depth;
                        }
                    }
                }
            }

        }
    }

    private void drawOrbit(Graphics2D g2, Matrix3 transform, ArrayList<Vertex> all) {
        for (Vertex v: all) {
            Vertex v1 = transform.transform4(v);
            Ellipse2D.Double point = new Ellipse2D.Double(v1.x + getWidth() / 2, v1.y + getHeight() / 2, 0.1, 0.1);
            g2.setColor(orbitColor[colorIndex]);
            g2.draw(point);
        }
    }
    private void createOrbit(ArrayList<Vertex> all, Planet body, double r){
        double step = (r * Math.PI) / (body.getA());
        double time = 0;
        while (time < r * Math.PI) {
            double[] xyz = body.getCoords(time);
            all.add(new Vertex(xyz[0], xyz[1], xyz[2]));
            time += step;
        }
    }

    private void createOrbitComet(ArrayList<Vertex> all, Comets body, double r){
        double step;
        if (body.getA() != 0)
            step = (r * Math.PI) / (body.getA());
        else
            step = 10;
        double time = 0;
        while (time < r * Math.PI) {
            double[] xyz = body.getCoords(time);
            all.add(new Vertex(xyz[0], xyz[1], xyz[2]));
            time += step;
        }
    }

    private void drawSelectedOrbit(Graphics2D g2, Matrix3 transform, double[] pos, double radius){
        Vertex v2 = transform.transform4(new Vertex(pos[0], pos[1], pos[2]));
        Ellipse2D.Double circle = new Ellipse2D.Double(v2.x + getWidth() / 2 - radius* scale, v2.y + getHeight() / 2 - radius* scale, radius*2* scale , radius*2* scale);
        g2.setColor(Color.CYAN);
        g2.draw(circle);
    }
    private void drawNames(Graphics2D g2, Matrix3 transform, double[] xyz, String name, double radius){
        Vertex v = transform.transform4(new Vertex(xyz[0] + radius * 2, xyz[1] - radius * 2, xyz[2]));
        g2.setColor(Color.white);
        g2.drawString(name, (int)(v.x) + getWidth()/2, (int)(v.y) + getHeight()/2);
    }

    private double findDrawnRadius(List<Triangle> tris){
        return Math.sqrt(tris.get(0).v1.x * tris.get(0).v1.x + tris.get(0).v1.y * tris.get(0).v1.y);
    }
    private void drawInfo(Graphics2D g2, Planet body, double r, double speed){
        g2.setColor(Color.WHITE);
        //String[] s = body.getAllInfo(r);
        int y = 10;
        for (String s : body.getAllInfo(r, speed)) {
            g2.drawString(s, 10, y);
            y += 5;
            g2.drawString("", 10, y);
            y += 10;
        }
    }


    private void drawStarInfo(Graphics2D g2, CelestialBody body){
        g2.setColor(Color.WHITE);
        String[] s = body.getInfo();
        int y = 10;
        for (int i = 0; i < 3; i ++) {
            g2.drawString(s[i], 10, y);
            y += 5;
            g2.drawString("", 10, y);
            y += 10;
        }
            }
    public class KeyBoard extends KeyAdapter implements KeyListener {
        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();
            //scaling solar system
            if (key == KeyEvent.VK_Q) {
                if (scale >= 1)
                    scale += 1;
                else{
                    if (scale > 0.1) {
                        scale += 0.1;
                        scale = roundAvoid(scale, 1);
                    }else{
                        if (scale >= 0.01) {
                            scale += 0.01;
                            scale = roundAvoid(scale, 2);
                        }else{
                            scale += 0.001;
                        }
                    }
                }
            }
            if (key == KeyEvent.VK_E) {
                if (scale > 1)
                    scale -= 1;
                else {
                    if (scale > 0.1) {
                        scale -= 0.1;
                        scale = roundAvoid(scale, 1);
                    }else{
                        if (scale > 0.01) {
                            scale -= 0.01;
                            scale = roundAvoid(scale, 2);
                        }else{
                            scale -=0.001;
                        }
                    }
                }
            }
            //moving solar system up/down/left/right
            if (key == KeyEvent.VK_W) b -= 10*scale;
            if (key == KeyEvent.VK_S) b += 10*scale;
            if (key == KeyEvent.VK_A) a -= 10*scale;
            if (key == KeyEvent.VK_D) a += 10*scale;
            //increasing/decreasing speed
            if (key == KeyEvent.VK_G) {
                if (dtAdd <= 100)
                    dtAdd *= 5;
            }
            if (key == KeyEvent.VK_T) {
                if (dtAdd >= 0.0001)
                    dtAdd /= 5;
            }
            //stopping and restarting
            if (key == KeyEvent.VK_O) {
                timer.stop();
            }
            if (key == KeyEvent.VK_P) {
                timer.restart();
            }

            //rotation
            if (key == KeyEvent.VK_I) {
                pitch += 10;
            }
            if (key == KeyEvent.VK_J) {
                heading += 10;
            }
            if (key == KeyEvent.VK_U) {
                pitch -= 10;
            }
            if (key == KeyEvent.VK_H) {
                heading -= 10;
            }
            if (key == KeyEvent.VK_1) {
                showOrbits = true;
            }
            if (key == KeyEvent.VK_2) {
                showOrbits = false;
            }
            if (key == KeyEvent.VK_SPACE){
                a = 0;
                b = 0;
                c = 0;
                dtAdd = 0.001;
                heading = 0;
                pitch = 0;
                scale = 10;
                showOrbits = false;
                planet = 0;
                setNamesVisible = false;
                showAllInfo = -1;
                colorIndex = 0;
                showBackground = true;
            }
            if (key == KeyEvent.VK_F1){ //Mercury
                dt = 0;
                planet = 1;
            }
            if (key == KeyEvent.VK_F2){ //Venus
                dt = 0;
                planet = 2;
            }
            if (key == KeyEvent.VK_F3){ //Earth
                dt = 0;
                planet = 3;
            }
            if (key == KeyEvent.VK_F4){ //Mars
                dt = 0;
                planet = 4;
            }
            if (key == KeyEvent.VK_F5){ //Jupiter
                dt = 0;
                planet = 5;
            }
            if (key == KeyEvent.VK_F6){ //Saturn
                dt = 0;
                planet = 6;
            }
            if (key == KeyEvent.VK_F7){ //Uranus
                dt = 0;
                planet = 7;
            }
            if (key == KeyEvent.VK_F8){ //Neptune
                dt = 0;
                planet = 8;
            }
            if (key == KeyEvent.VK_F10){ //Neptune
                dt = 0;
                planet = 10;
            }
            if (key == KeyEvent.VK_Z){
                if (setNamesVisible){
                    setNamesVisible = false;
                }else{
                    setNamesVisible = true;
                }
            }
            if (key == KeyEvent.VK_RIGHT){
                if (showAllInfo == 8){
                    showAllInfo = -1;
                }else {
                    showAllInfo += 1;
                }
            }
            if (key == KeyEvent.VK_LEFT){
                if (showAllInfo == -1){
                    showAllInfo = 8;
                }else {
                    showAllInfo -= 1;
                }
            }
            if (key == KeyEvent.VK_CAPS_LOCK){
                showBackground = !showBackground;
                colorIndex = (colorIndex + 1) % 2;
            }
        }
    }
    private double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}