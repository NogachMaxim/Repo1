import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int DOT_SIZE = 25;
    private final int ALL_DOTS = 196;
    private Image dot;
    private int countOnFlour = 0;
    private int[] part_x = new int[4];
    private int[] part_y = new int[4];
    private ArrayList<Integer> allpart_x = new ArrayList<>();
    private ArrayList<Integer> allpart_y = new ArrayList<>();
    private Timer timer;
    private boolean inGame;
    private boolean Left;
    private boolean Right;
    private boolean fullLine;
    private boolean isOnFlour;


    public GameField() {
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
    }


    public void initGame() {
        inGame = true;
        part_x[0] = 6 * DOT_SIZE;
        part_y[0] = 2 * DOT_SIZE;
        for (int i = 1; i < 4; i++) {
            CasePartI(i);
        }
        timer = new Timer(350, this);
        timer.start();
    }


    public void CasePartI(int indexPartI) {

        ArrayList<String> ZanNapr = new ArrayList<>();
        ZanNapr.add("Север");
        ZanNapr.add("Юг");
        ZanNapr.add("Запад");
        ZanNapr.add("Восток");
        for (int i = 0; i < indexPartI; i++) {
            if (part_x[indexPartI - 1] == part_x[indexPartI - i - 1] && part_y[indexPartI - 1] - DOT_SIZE == part_y[indexPartI - i - 1]) {
                //Проверка прошлого квадрата на Север
                ZanNapr.remove(0);
            }
            if (part_x[indexPartI - 1] == part_x[indexPartI - i - 1] && part_y[indexPartI - 1] + DOT_SIZE == part_y[indexPartI - i - 1]) {
                //Проверка прошлого квадрата на Юг
                ZanNapr.remove(1);
            }
            if (part_x[indexPartI - 1] - DOT_SIZE == part_x[indexPartI - i - 1] && part_y[indexPartI - 1] == part_y[indexPartI - i - 1]) {
                //Проверка прошлого квадрата на Запад
                ZanNapr.remove(2);
            }
            if (part_x[indexPartI - 1] + DOT_SIZE == part_x[indexPartI - i - 1] && part_y[indexPartI - 1] == part_y[indexPartI - i - 1]) {
                //Проверка прошлого квадрата на Восток
                ZanNapr.remove(3);
            }
        }
        int random = new Random().nextInt(ZanNapr.size());
        if (ZanNapr.toArray()[random] == "Север") { //Север
            part_x[indexPartI] = part_x[indexPartI - 1];
            part_y[indexPartI] = part_y[indexPartI - 1] - DOT_SIZE;
        }
        if (ZanNapr.toArray()[random] == "Юг") { //Юг

            part_x[indexPartI] = part_x[indexPartI - 1];
            part_y[indexPartI] = part_y[indexPartI - 1] + DOT_SIZE;
        }
        if (ZanNapr.toArray()[random] == "Запад") { //Запад
            part_x[indexPartI] = part_x[indexPartI - 1] - DOT_SIZE;
            part_y[indexPartI] = part_y[indexPartI - 1];
        }
        if (ZanNapr.toArray()[random] == "Восток") { //Восток
            part_x[indexPartI] = part_x[indexPartI - 1] + DOT_SIZE;
            part_y[indexPartI] = part_y[indexPartI - 1];
        }

    }

    public void loadImage() {
        ImageIcon ii = new ImageIcon("Квадрат.png");
        dot = ii.getImage();
    }

    public void move() {
        if (Left && (!checkLeft())) { //влево
            for (int i = 0; i < 4; i++) {
                part_x[i] = part_x[i] - DOT_SIZE;
            }
            Left = false;
        }
        if (Right && (!checkRight())) { //вправо
            for (int i = 0; i < 4; i++) {
                part_x[i] = part_x[i] + DOT_SIZE;
            }
            Right = false;
        }
        for (int i = 0; i < 4; i++) { //движение
            part_y[i] = part_y[i] + DOT_SIZE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            for (int i = 0; i < 4; i++) {
                g.drawImage(dot, part_x[i], part_y[i], this);
            }
            for (int i = 0; i < allpart_x.size(); i++) {
                g.drawImage(dot, allpart_x.get(i), allpart_y.get(i), this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        inGame = !checkNotInGame();
        if (inGame) {
            if (isOnFlour) {
                for (int i = 0; i < 4; i++) { //добавление в allpart
                    allpart_x.add(part_x[i]);
                    allpart_y.add(part_y[i]);
                }
                checkDel();
                countOnFlour++;
                part_x[0] = 7 * DOT_SIZE;
                part_y[0] = 2 * DOT_SIZE;
                for (int i = 1; i < 4; i++) {
                    CasePartI(i);
                }
            }
            move();
            isOnFlour = checkFlour();
        } else {
            timer.stop();
            JFrame frame = new JFrame();
            frame.setLocation(320, 320);
            frame.setSize(150, 150);
            JLabel label = new JLabel();
            label.setText("Игра проиграна");
            label.setVisible(true);
            frame.add(label);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }
        repaint();
        timer.setDelay(350);

    }

    public boolean checkFlour() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < allpart_x.size(); j++) {
                if (part_x[i] == allpart_x.get(j) && part_y[i] == allpart_y.get(j) - DOT_SIZE) {
                    return true;
                }
            }
            if (part_y[i] >= 300) { //упала ли на пол
                return true;
            }
        }
        return false;
    }

    public boolean checkLeft() {
        for (int i = 0; i < 4; i++) {
            if (part_x[i] < 25) {
                return true;
            }
        }
        return false;
    }

    public void checkDel() {
        int k = 300;
        for (int j = 0; j < 14; j++) { // 14 строк всего,  для каждой строки
            k = k - j * DOT_SIZE; //значение, которое может повторяться
            int t = 0; //счетчик количества одинаковых
            for (int i = 0; i < allpart_y.size(); i++) { //сколько раз встречаются в all_y одинаковые значения
                if (k == allpart_y.get(i)) {
                    t++; //нашли одинаковые
                }
            }
            if (t == 14) { //само много раз повторяется координата - строка полностью заполнена
                for (int i = 0; i < allpart_y.size(); i++) { //для каждого в all_y удаление
                    if (k == allpart_y.get(i)) {
                        allpart_y.remove(i); //удаляем все детали из строки
                        allpart_x.remove(i);
                        i--;
                    }
                }
                for (int i = 0; i < allpart_y.size(); i++) { //передвижение
                    if (k > allpart_y.get(i)) { //перемещаем те, что сверху вниз на 1
                        int m = allpart_y.get(i) + DOT_SIZE;
                        allpart_y.set(i, m);
                    }
                }
            j--;
            }
        }
    }


    public boolean checkRight() {
        for (int i = 0; i < part_x.length; i++) {
            if (part_x[i] >= 320) {
                return true;
            }
        }
        return false;
    }

    public boolean checkNotInGame() {
        if (allpart_x.size() == 0) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            if (part_x[i] == allpart_x.get(allpart_x.size() - 1) && part_y[i] == allpart_y.get(allpart_y.size() - 1)) {
                return true;
            }
        }
        return false;
    }

    public void turnOn90() {
        for (int i = 0; i < 4; i++) { //для всех частей. кроме 3
            if (i == 2) continue;
            if (part_x[i] == part_x[2] + DOT_SIZE && part_y[i] == part_y[2]) { //находится справа на 1
                part_x[i] = part_x[2];
                part_y[i] = part_y[2] + DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] + 2 * DOT_SIZE && part_y[i] == part_y[2]) { //находится справа на 2
                part_x[i] = part_x[2];
                part_y[i] = part_y[2] + 2 * DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] && part_y[i] == part_y[2] - DOT_SIZE) { //находится вверху на 1
                part_x[i] = part_x[2] + DOT_SIZE;
                part_y[i] = part_y[2];
                continue;
            }
            if (part_x[i] == part_x[2] && part_y[i] == part_y[2] - 2 * DOT_SIZE) { //находится вверху на 2
                part_x[i] = part_x[2] + 2 * DOT_SIZE;
                part_y[i] = part_y[2];
                continue;
            }
            if (part_x[i] == part_x[2] - DOT_SIZE && part_y[i] == part_y[2]) { //находится слева на 1
                part_x[i] = part_x[2];
                part_y[i] = part_y[2] - DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] - 2 * DOT_SIZE && part_y[i] == part_y[2]) { //находится слева на 2
                part_x[i] = part_x[2];
                part_y[i] = part_y[2] - 2 * DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] && part_y[i] == part_y[2] + DOT_SIZE) { //находится внизу на 1
                part_x[i] = part_x[2] - DOT_SIZE;
                part_y[i] = part_y[2];
                continue;
            }
            if (part_x[i] == part_x[2] && part_y[i] == part_y[2] + 2 * DOT_SIZE) { //находится внизу на 2
                part_x[i] = part_x[2] - 2 * DOT_SIZE;
                part_y[i] = part_y[2];
                continue;
            }
            if (part_x[i] == part_x[2] + DOT_SIZE && part_y[i] == part_y[2] - DOT_SIZE) { //находится вверху-справа на 1
                part_y[i] = part_y[2] + DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] - DOT_SIZE && part_y[i] == part_y[2] - DOT_SIZE) { //находится вверху-слева на 1
                part_x[i] = part_x[2] + DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] + DOT_SIZE && part_y[i] == part_y[2] + DOT_SIZE) { //находится внизу-справа на 1
                part_x[i] = part_x[2] - DOT_SIZE;
                continue;
            }
            if (part_x[i] == part_x[2] - DOT_SIZE && part_y[i] == part_y[2] + DOT_SIZE) { //находится внизу-слева на 1
                part_y[i] = part_y[2] - DOT_SIZE;
                continue;
            }
        }
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                Left = true;
                Right = false;
            }
            if (key == KeyEvent.VK_RIGHT) {
                Right = true;
                Left = false;
            }
            if (key == KeyEvent.VK_DOWN) {
                timer.setDelay(50);
            }
            if (key == KeyEvent.VK_SPACE) {
                turnOn90();
            }
        }
    }
}
