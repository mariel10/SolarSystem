import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class World {
    private JFrame mainFrame;
    private JPanel settingsPanel = new JPanel();
    private SkyPanel skyPanel = new SkyPanel();
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public void go(){
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        mainFrame = new JFrame("Solar System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
        mainFrame.setLocationRelativeTo(null);
        getSettingsPanel();

        mainFrame.add(skyPanel, "Center");
        mainFrame.add(settingsPanel, "East");
        mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
        mainFrame.setVisible(true);
    }

    private void getSettingsPanel(){
        int n = 7;
        JPanel settingsPanel1 = new JPanel();
        settingsPanel1.setLayout(new BoxLayout(settingsPanel1, BoxLayout.Y_AXIS));
        JPanel settingsPanel2 = new JPanel(new GridLayout(4,1,0,5));;
        JPanel settingsPanel3 = new JPanel(new GridLayout(1, 2, 5, 0));

        JLabel kepler = new JLabel("Кеплеровские элементы кометы");
        JLabel name = new JLabel("Название кометы");
        JLabel a = new JLabel("Большая полуось (в млн км)");
        JLabel e = new JLabel("Эксцентриситет");
        JLabel r = new JLabel("Радиус (в км)");
        JLabel i = new JLabel("Наклонение орбиты (в градусах)");
        JLabel w = new JLabel("Аргумент перицентра (в градусах)");
        JLabel q = new JLabel("Долгота восходящего узла (в градусах)");

        JTextField nameField = new JTextField();
        JTextField aField = new JTextField();
        JTextField eField = new JTextField();
        JTextField rField = new JTextField();
        JTextField iField = new JTextField();
        JTextField wField = new JTextField();
        JTextField qField = new JTextField();


        JButton use = new JButton("Сохранить");
        use.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean complete = true;
                String cometName = nameField.getText();
                String cometA = aField.getText();
                String cometE = eField.getText();
                String cometI = iField.getText();
                String cometW = wField.getText();
                String cometQ = qField.getText();
                String cometR = rField.getText();

                if ((cometA.trim().length() > 0) && (cometE.trim().length() > 0) && (cometI.trim().length() > 0) &&
                        (cometW.trim().length() > 0) && (cometQ.trim().length() > 0) && (cometR.trim().length() > 0)){
                    double aComet;
                    double eComet;
                    double iComet;
                    double wComet;
                    double qComet;
                    int rComet;

                    try{
                        aComet = (Double.parseDouble(cometA));
                        eComet = (Double.parseDouble(cometE));
                        iComet = (Double.parseDouble(cometI));
                        wComet = (Double.parseDouble(cometW));
                        qComet = (Double.parseDouble(cometQ));
                        rComet = (Integer.parseInt(cometR));
                        if (checkInput(aComet, eComet, iComet, wComet,qComet, rComet)) {
                            skyPanel.setMyComet(cometName, new double[]{aComet, eComet, iComet, wComet, qComet}, rComet);
                        }

                    }catch (Exception ex){
                        complete = false;
                    }
                }
                nameField.setEnabled(false);
                aField.setEnabled(false);
                eField.setEnabled(false);
                iField.setEnabled(false);
                wField.setEnabled(false);
                rField.setEnabled(false);
                use.setEnabled(false);
                qField.setEnabled(false);

            }
        });
        JButton random = new JButton("Случайно");
        random.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cometName = "My Comet";
                double[] res = getAEIWQ();
                int radius = 5 + (int)(Math.random() * 195);
                nameField.setText(cometName);
                aField.setText(Double.toString(res[0]));
                eField.setText(Double.toString(res[1]));
                iField.setText(Double.toString(res[2]));
                wField.setText(Double.toString(res[3]));
                rField.setText(Integer.toString(radius));
                qField.setText(Double.toString(res[4]));

                nameField.setEnabled(false);
                aField.setEnabled(false);
                eField.setEnabled(false);
                iField.setEnabled(false);
                wField.setEnabled(false);
                rField.setEnabled(false);
                random.setEnabled(false);
                use.setEnabled(false);
                qField.setEnabled(false);

                skyPanel.setMyComet(cometName, res, radius);
            }
        });



        JButton about = new JButton("Справка");
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Help help = new Help();
                help.setVisible(true);
            }
        });
        JButton start = new JButton("Старт");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skyPanel.setStart();
                random.setEnabled(false);
            }
        });
        JButton stop = new JButton("Стоп");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skyPanel.stopStart();
                nameField.setEnabled(true);
                aField.setEnabled(true);
                eField.setEnabled(true);
                iField.setEnabled(true);
                wField.setEnabled(true);
                rField.setEnabled(true);
                random.setEnabled(true);
                use.setEnabled(true);
                qField.setEnabled(true);
            }
        });
        JButton close = new JButton("Выйти");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(kepler);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(name);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(nameField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(a);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(aField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(e);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(eField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(r);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(rField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(i);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(iField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(w);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(wField);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(q);
        settingsPanel1.add(Box.createVerticalStrut(n));
        settingsPanel1.add(qField);
        settingsPanel1.add(Box.createVerticalStrut(n));

        settingsPanel3.add(use);
        settingsPanel3.add(random);

        settingsPanel2.add(about);
        settingsPanel2.add(Box.createVerticalStrut(n));
        settingsPanel2.add(start);
        settingsPanel2.add(Box.createVerticalStrut(n));
        settingsPanel2.add(stop);
        settingsPanel2.add(Box.createVerticalStrut(n));
        settingsPanel2.add(close);

        settingsPanel.add(settingsPanel1);
        settingsPanel.add(settingsPanel3);
        settingsPanel.add(Box.createVerticalStrut((int)(screenSize.getWidth()/6)));
        settingsPanel.add(settingsPanel2);
    }

    private double[] getAEIWQ(){
        double[] result = new double[5];
        result[0] = 2500 + Math.random() * 7500; //a
        result[1] = Math.random(); //e
        result[2] = Math.random() * 360; //i
        result[3] = Math.random() * 360; //w
        result[4] = Math.random() * 360; //q
        return result;
    }

    private boolean checkInput(double a, double e, double i, double w, double q, int r){
        boolean result;

        if ((a >= 2500) && (a <= 10000)){
            if ((e >= 0)){
                if ((i >= 0) && (i <= 360)){
                    if ((w >= 0) && (w <= 360)){
                        if ((q >= 0) && (q <= 360)){
                            if ((r >= 5) && (r <= 200))  result = true;
                            else result = false;
                        }else result = false;
                    }else result = false;
                }else result = false;
            }else result = false;
        }else result = false;

        return result;
    }
}
