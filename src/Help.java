import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class Help extends JFrame {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private double width = screenSize.getWidth();
    private double height = screenSize.getHeight();
    private JTextPane mainPane;
    private StyledDocument doc;

    private String s1 = "На экране изображаются Солнце и восемь планет Солнечной системы (от Меркурия до Нептуна), а также их основные спутники и комета Галлея.\n";
    private String s2 = "Солнечная система - это планетарная система, включающая в себя центральную звезду - Солнеце - и все естественные космические " +
            "объекты, вращающиеся вокруг Солнца. \n" +
            "Планета - это небесное тело, которое:\n" +
            "1) Обращается по орбите вокруг Солнца\n" +
            "2) Имеет достаточную массу, такую, что собственные гравитационные силы первысили силы сохранения формытвердого тела и привели её к" +
            " гидродинамическиравновесной форме (близкой к шарообразной)\n" +
            "3) Очистило пространство вокруг собственной орбиты\n\n";
    private String s3 = "Моделирование движения планет базируется на 3 основных законах Кеплера, определяющие эллиптические орбиты планет и спутников." +
            "Сами законы:\n" +
            "1) Каждая планет обращается по эллипсу, в одном из фокусов которого находится Солнце.\n" +
            "2) (закон площадей) Радиус вектор планеты за одинаковые промежутки времени описывает равные площади.\n" +
            "3 ) Квадраты звездных периодов ображения планет относятся как кубы больших полуосей их орбит.\n" +
            "\n\n" +
            "Формулы, по которым происходит расчет положения планет и спутников:\n\n";
    private String s4 = "\nОсновные элементы управления:\n" +
            "Перемещение Солнечной системы : WASD\n" +
            "Увеличение/ уменьшение масштаба : QE\n" +
            "Увеличение/ уменьшение скорости : GT\n" +
            "Повороты вокруг осей : IU JH\n" +
            "Включение/ выключение названий : Z\n" +
            "Включение орбит : 1\n" +
            "Выключение орбит : 2\n" +
            "Включение/ выключение фона : CapsLock\n" +
            "Перемещение на планету : F1 - F8 соответственно на Меркурий - Нептун; F10 - на комету Галлея\n" +
            "Возвращение в исходное состояние - Space\n" +
            "Информация о планетах : Стрелки вправо и влево\n\n";
    private String s5 = "Для запуска собственной кометы следует либо задать параметры случайно, либо ввести, соблюдая следующие условия: \n" +
            "Имя любое\n" +
            "Большая полуось находится в следующих пределах : 2500 - 10000\n" +
            "Эксцентриситет : [0 , 1)\n" +
            "Радиус: 5 - 200\n" +
            "Наклонение, аргумент перицентра и долгота восходящего узла : 0 - 360 \n" +
            "после чего надо сохранить и запустить.";
    public Help(){
        setTitle("Помощь");
        setSize(800, 700);
        go();
        setLocationRelativeTo(null);
    }

    public void go(){
        mainPane = new JTextPane();
        doc = createDocument(mainPane);
        JScrollPane scroll = new JScrollPane(mainPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        int w = (int) Math.round(width*0.6);
        int h = (int) Math.round(height*0.75);
        scroll.setBounds(0, 20, w, h);
        scroll.setPreferredSize(new Dimension(w, h));
        this.add(scroll);
    }

    private StyledDocument createDocument(JTextPane tp){
        Style normal = tp.addStyle("Normal" , null);
        StyleConstants.setFontFamily(normal,  "Verdana");
        StyledDocument doc = tp.getStyledDocument();
        Style style = doc.addStyle("StyleName", null);


        Style heading = tp.addStyle("Heading", normal);
        StyleConstants.setFontSize(heading, 20);
        StyleConstants.setBold(heading, true);

        insertString("Компьютерная модель Солнечной Системы \n", tp, heading);
        insertString(s1, tp, normal);
        insertString(s2, tp, normal);
        tp.insertIcon ( new ImageIcon ( "pictures/09.png" ));
        insertString(s3, tp, normal);
        tp.insertIcon ( new ImageIcon ( "pictures/11.png" ));
        insertString(s4, tp, normal);
        insertString(s5, tp, normal);
        tp.setEditable(false);
        return doc;
    }

    private void insertString(String s, JTextPane tp, Style style) {
        try {
            Document doc = tp.getDocument();
            doc.insertString(doc.getLength(), s + "\n", style);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}
