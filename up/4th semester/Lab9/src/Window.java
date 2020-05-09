import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private JPanel panel;
    private MyTable table;
    private final int ROWS = 10;
    private final int COLUMNS = 8;

    public Window() {
        setTitle("Lab9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 1000, 600);

        table = new MyTable(ROWS, COLUMNS);
        panel = new JPanel(new BorderLayout());
        panel.add(table);

        Container container = this.getContentPane();
        container.add(panel);
    }

}
