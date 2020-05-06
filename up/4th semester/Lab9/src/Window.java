import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class Window extends JFrame {
    private JPanel panel;
    private JTable table;
    private DefaultTableModel model;
    private final int ROWS = 31;
    private final int COLUMNS = 15;
    private String folmula[][];
    private Object data[][];
    private int x = -1, y = -1;

    public Window() {
        setTitle("Lab9");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 200, 1000, 600);

        folmula = new String[ROWS][COLUMNS];
        data = new Object[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                folmula[i][j] = "";
                data[i][j] = "";
            }
        }

        for (int i = 0; i < ROWS - 1; ++i) {
            data[i + 1][0] = String.valueOf(i + 1);
        }
        char cur = 'A';
        for (int i = 1; i < COLUMNS; ++i) {
            data[0][i] = String.valueOf(cur++);
        }
        String column[] = new String[COLUMNS];
        for (int i = 0; i < COLUMNS; ++i) {
            column[i] = "ID";
        }
        model = new DefaultTableModel(data, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return (row != 0 && column != 0);
            }
        };

        table = new JTable();
        table.setModel(model);
        table.setCellSelectionEnabled(true);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                repaintTable();
                x = table.getSelectedRow();
                y = table.getSelectedColumn();
                if (table.getModel().isCellEditable(x, y)) {
                    model.setValueAt(folmula[x][y], x, y);
                }
            }
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                repaintTable();
            }
        });

        panel = new JPanel(new BorderLayout());
        panel.add(table);

        Container container = this.getContentPane();
        container.add(panel);
    }

    private void repaintTable() {
        recalc();
        if (x != -1 && y != -1) {
            folmula[x][y] = (String) model.getValueAt(x, y);
        }
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                int x = table.getSelectedRow(), y = table.getSelectedColumn();
                if (x != i || y != j) {
                    model.setValueAt(data[i][j], i, j);
                }
            }
        }
    }

    private final Pattern DATE = Pattern.compile("(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+");
    private final Pattern SUM_CONST = Pattern.compile("=(((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+");
    private final Pattern SUM_FIELD = Pattern.compile("=[A-Z]+[0-9]+[+-]?[0-9]+");
    private final Pattern MIN_MAX = Pattern.compile("=(MIN|MAX)\\(((((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+|[A-Z]+[0-9]+)(,[ ]?((((0?[1-9]|[1-2][0-9]|3[0-1])[/.](1|3|5|7|8|10|12|01|03|05|07|08)|(0?[1-9]|[1-2][0-9]|30)[/.](4|6|9|11|04|06|09))|(0?[1-9]|1[0-9]|2[0-8])[/.](2|02))[/.][0-9]+[+-]?[0-9]+|[A-Z]+[0-9]+))+\\)");

    private void recalc() {
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>(ROWS + 1);
        for (int i = 0; i < ROWS; ++i) {
            for (int j = 0; j < COLUMNS; ++j) {
                if (table.isCellEditable(i, j)) {
                    String cur = (String) model.getValueAt(i, j);
                    if ()
                }
            }
        }
    }

}
