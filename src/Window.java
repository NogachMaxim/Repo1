import javax.swing.*;

public class Window extends JFrame {
    public Window(){
        setTitle("Тетрис");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(360, 360);
        setLocation(400, 400);
        add(new GameField());
        setVisible(true);
    }

    public static void main(String[] args) {
        Window w=new Window();
    }
}
